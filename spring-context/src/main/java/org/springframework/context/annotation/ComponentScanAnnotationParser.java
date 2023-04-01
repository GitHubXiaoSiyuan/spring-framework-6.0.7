/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context.annotation;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * Parser for the @{@link ComponentScan} annotation.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 3.1
 * @see ClassPathBeanDefinitionScanner#scan(String...)
 * @see ComponentScanBeanDefinitionParser
 */
class ComponentScanAnnotationParser {

	private final Environment environment;

	private final ResourceLoader resourceLoader;

	private final BeanNameGenerator beanNameGenerator;

	private final BeanDefinitionRegistry registry;


	public ComponentScanAnnotationParser(Environment environment, ResourceLoader resourceLoader,
			BeanNameGenerator beanNameGenerator, BeanDefinitionRegistry registry) {

		this.environment = environment;
		this.resourceLoader = resourceLoader;
		this.beanNameGenerator = beanNameGenerator;
		this.registry = registry;
	}


	/**
	 *
	 * @param componentScan 表示 @ComponentScan 注解的属性值
	 * @param declaringClass 表示 @ComponentScan 注解的所在的类
	 * @return
	 */
	public Set<BeanDefinitionHolder> parse(AnnotationAttributes componentScan, String declaringClass) {
		// 构建扫描器
		// this.registry 表示 spring 容器 , BeanDefinitionRegistry registry ，各种 applicationContext 实现 BeanDefinitionRegistry 接口
		// componentScan.getBoolean("useDefaultFilters"), @ComponentScan 注解的 useDefaultFilters 属性 ，默认为 true
		// 注册
		ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(this.registry,
				componentScan.getBoolean("useDefaultFilters"), this.environment, this.resourceLoader);


		// ----------------------向扫描器里面添加东西------------------------------

		// @ComponentScan 注解的 nameGenerator 属性  ， beanName 生成器
		// Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;
		// 默认值 BeanNameGenerator.class
		Class<? extends BeanNameGenerator> generatorClass = componentScan.getClass("nameGenerator");
		boolean useInheritedGenerator = (BeanNameGenerator.class == generatorClass);
		// 默认为 AnnotationBeanNameGenerator
		// 相等用默认的 this.beanNameGenerator，不相等用自定义的 BeanUtils.instantiateClass(generatorClass)
		/** @see AnnotationBeanNameGenerator */
		scanner.setBeanNameGenerator(useInheritedGenerator ? this.beanNameGenerator :
				BeanUtils.instantiateClass(generatorClass));

		// @ComponentScan 注解的 scopedProxy 属性
		ScopedProxyMode scopedProxyMode = componentScan.getEnum("scopedProxy");
		if (scopedProxyMode != ScopedProxyMode.DEFAULT) {
			scanner.setScopedProxyMode(scopedProxyMode);
		}
		else {
			Class<? extends ScopeMetadataResolver> resolverClass = componentScan.getClass("scopeResolver");
			scanner.setScopeMetadataResolver(BeanUtils.instantiateClass(resolverClass));
		}

		// @ComponentScan 注解的 resourcePattern 属性
		scanner.setResourcePattern(componentScan.getString("resourcePattern"));

		// @ComponentScan 注解的 includeFilters 属性
		for (AnnotationAttributes includeFilterAttributes : componentScan.getAnnotationArray("includeFilters")) {
			List<TypeFilter> typeFilters = TypeFilterUtils.createTypeFiltersFor(includeFilterAttributes, this.environment,
					this.resourceLoader, this.registry);
			for (TypeFilter typeFilter : typeFilters) {
				scanner.addIncludeFilter(typeFilter);
			}
		}
		// @ComponentScan 注解的 excludeFilters 属性
		for (AnnotationAttributes excludeFilterAttributes : componentScan.getAnnotationArray("excludeFilters")) {
			List<TypeFilter> typeFilters = TypeFilterUtils.createTypeFiltersFor(excludeFilterAttributes, this.environment,
				this.resourceLoader, this.registry);
			for (TypeFilter typeFilter : typeFilters) {
				scanner.addExcludeFilter(typeFilter);
			}
		}

		// @ComponentScan 注解的 lazyInit 属性
		// 默认情况下bean都是非懒加载 （lazyInit = false）
		boolean lazyInit = componentScan.getBoolean("lazyInit");
		if (lazyInit) {
			scanner.getBeanDefinitionDefaults().setLazyInit(true);
		}

		// @ComponentScan 注解的 basePackages 属性 (value 等价于 basePackages)
		Set<String> basePackages = new LinkedHashSet<>();
		String[] basePackagesArray = componentScan.getStringArray("basePackages");
		for (String pkg : basePackagesArray) {
			String[] tokenized = StringUtils.tokenizeToStringArray(this.environment.resolvePlaceholders(pkg),
					ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
			Collections.addAll(basePackages, tokenized);
		}
		// 获取指定类所在的包名作为扫描路径 比如： cn.xiaosy.springdemo.scanner.AppConfig ， ClassUtils.getPackageName(clazz) = cn.xiaosy.springdemo.scanner
		for (Class<?> clazz : componentScan.getClassArray("basePackageClasses")) {
			basePackages.add(ClassUtils.getPackageName(clazz));
		}
		// 如果为空就会把 @ComponentScan 注解 所在的类 declaringClass，当做 basePackageClasses
		// 然后获取到扫描路径
		if (basePackages.isEmpty()) {
			basePackages.add(ClassUtils.getPackageName(declaringClass));
		}

		scanner.addExcludeFilter(new AbstractTypeHierarchyTraversingFilter(false, false) {
			@Override
			protected boolean matchClassName(String className) {
				// 如果 扫描到的类，其类名 className 等于 @ComponentScan 注解 所在的类 declaringClass 的类名
				// 则排除，为 true 即排除 （false不排除）
				// 比如：
				// @ComponentScan
				// public class AppConfig {
				// }
				// AppConfig 早就通过 registerBean 注册进去了，这里把 AppConfig 排除不再扫描
				return declaringClass.equals(className);
			}
		});
		// 扫描
		return scanner.doScan(StringUtils.toStringArray(basePackages));
	}

}
