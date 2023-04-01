package cn.xiaosy.springdemo.scanner;

import cn.xiaosy.springdemo.scanner.service.*;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.*;

@ComponentScan(
		// 源码位置
		/** @see org.springframework.context.annotation.ComponentScanAnnotationParser#parse */

		value = "cn.xiaosy.springdemo.scanner"

		, basePackageClasses = AppConfig.class
		// 默认情况下bean都是非懒加载 （lazyInit = false）
		// 这里可以改为懒加载 （lazyInit = true）
//		, lazyInit = true
		, excludeFilters = {
		// 排除指定类型 UserService.class
		// 注：再获取的时候就会报错 No bean named 'userService' available
//			@ComponentScan.Filter(
//					type = FilterType.ASSIGNABLE_TYPE,
//					classes = UserService.class
//			)

		// 排除有 @CustomerExclude 注解的类
//			@ComponentScan.Filter(
//					type = FilterType.ANNOTATION,
//					classes = CustomerExclude.class
//			)

		// 排除 type=ASPECTJ type=REGEX 匹配的表达式

		// type=CUSTOM 指定一个自定义的类，实现 TypeFilter 接口，在 match 方法中写匹配逻辑
//			@ComponentScan.Filter(
//					type = FilterType.CUSTOM,
//					classes = CustomerExcludeMather.class
//			)
		}
		// 注：没有遇到什么场景需要修改这个属性
//		, resourcePattern = ClassPathScanningCandidateComponentProvider.DEFAULT_RESOURCE_PATTERN

		, scopedProxy = ScopedProxyMode.DEFAULT

//		nameGenerator = CustomerBeanNameGenerator.class
)
/**
 * 扫描的最终结果不是 bean 而是 BeanDefinition
 *
 * @see ConfigurationClassPostProcessor //  扫描的源码位置
 *
 * @see ConfigurationClassPostProcessor#postProcessBeanDefinitionRegistry(BeanDefinitionRegistry)	// 关键方法
 */
/**
 * 可以指定两个扫描路径
 *
 * @ComponentScan("cn.xiaosy.springdemo.scanner")
 * @ComponentScan("cn.xiaosy.springdemo.mybatis")
 *
 * 或
 *
 * @ComponentScans(value = {@ComponentScan("cn.xiaosy.springdemo.scanner"),@ComponentScan("cn.xiaosy.springdemo.mybatis")})
 */
@EnableAspectJAutoProxy
public class AppConfig {



}
