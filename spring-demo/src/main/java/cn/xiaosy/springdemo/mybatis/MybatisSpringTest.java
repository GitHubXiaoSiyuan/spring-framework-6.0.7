package cn.xiaosy.springdemo.mybatis;

import cn.xiaosy.springdemo.mybatis.mapper.OrderMapper;
import cn.xiaosy.springdemo.mybatis.mapper.UserMapper;
import cn.xiaosy.springdemo.mybatis.service.UserService;
import cn.xiaosy.springdemo.mybatis.springmybatis.CustomerFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MybatisSpringTest {

	public static void main(String[] args) {

		// method 3
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MyBatisConfig.class);
		UserService userService = (UserService) applicationContext.getBean("userService");
		userService.test("1");

		// method 2
		// 指定bean的构造方法的参数
/*		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		// 把3步拆分出来
		// 1
		applicationContext.register(MyBatisConfig.class);
		// 2
		// 这里生成 2 个 Bean ： CustomerFactoryBean 和 UserMapper
		AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
		beanDefinition.setBeanClass(CustomerFactoryBean.class);
		beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(UserMapper.class);
		applicationContext.registerBeanDefinition("userMapper", beanDefinition);

		// 这里生成 2 个 Bean ： CustomerFactoryBean 和 OrderMapper
		AbstractBeanDefinition beanDefinitionB = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
		beanDefinitionB.setBeanClass(CustomerFactoryBean.class);
		beanDefinitionB.getConstructorArgumentValues().addGenericArgumentValue(OrderMapper.class);
		applicationContext.registerBeanDefinition("orderMapper", beanDefinitionB);

		// 3
		applicationContext.refresh();


		UserService userService = (UserService) applicationContext.getBean("userService");
		userService.test("1");*/


		// method 1 需要放开注释，或者拆分 customerFactoryBean
//		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MyBatisConfig.class);
//		UserMapper userMapper = (UserMapper) applicationContext.getBean("customerFactoryBean");
//		System.out.println(userMapper.getUserNameById("1"));


//		// FactoryBean机制
//
//		// FactoryBean 的名字 获取的是 getObject 方法返回的 bean
//		System.out.println(applicationContext.getBean("customerFactoryBean").toString());
//
//		// & + FactoryBean 的名字 获取的是 FactoryBean 本身
//		System.out.println(applicationContext.getBean("&customerFactoryBean").toString());
//


	}

}
