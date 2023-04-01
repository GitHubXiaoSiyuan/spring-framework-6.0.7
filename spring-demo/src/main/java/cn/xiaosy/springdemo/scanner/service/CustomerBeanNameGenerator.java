package cn.xiaosy.springdemo.scanner.service;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;

public class CustomerBeanNameGenerator implements BeanNameGenerator {
	@Override
	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		// beanName 从 userService 变成了 customerPrefixcn.xiaosy.springdemo.scanner.service.UserService
		return "customerPrefix" + definition.getBeanClassName();
	}
}
