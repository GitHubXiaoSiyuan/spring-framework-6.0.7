package cn.xiaosy.springdemo.mybatis.springmybatis;

import cn.xiaosy.springdemo.mybatis.mapper.OrderMapper;
import cn.xiaosy.springdemo.mybatis.mapper.UserMapper;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.Map;

public class CustomerImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {

		// method 4
		Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(CustomerScan.class.getName());
		String path = (String) annotationAttributes.get("value");

		CustomerScanner scanner = new CustomerScanner(registry);

		scanner.addIncludeFilter(new TypeFilter() {
			@Override
			public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
				return true;
			}
		});

		// 扫描的时候自动添加到了 bean 容器中
		scanner.doScan(path);


		// method 3
/*		//		ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry, importBeanNameGenerator);


		// registry 就是 method2 中的 applicationContext

		// 这里生成 2 个 Bean ： CustomerFactoryBean 和 UserMapper
		AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
		beanDefinition.setBeanClass(CustomerFactoryBean.class);
		beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(UserMapper.class);
		registry.registerBeanDefinition("userMapper", beanDefinition);

		// 这里生成 2 个 Bean ： CustomerFactoryBean 和 OrderMapper
		AbstractBeanDefinition beanDefinitionB = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
		beanDefinitionB.setBeanClass(CustomerFactoryBean.class);
		beanDefinitionB.getConstructorArgumentValues().addGenericArgumentValue(OrderMapper.class);
		registry.registerBeanDefinition("orderMapper", beanDefinitionB);*/
	}
}
