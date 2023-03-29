package org.springframework.demo;


import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.springframework.util.StringUtils;


public class Test {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
		Person person = (Person) applicationContext.getBean("person");
		System.out.println(person);
		System.out.println("hi");
		System.out.println(StringUtils.capitalize("hi"));
	}

}
