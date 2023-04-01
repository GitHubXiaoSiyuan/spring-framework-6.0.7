package cn.xiaosy.springdemo.scanner.demo1;

import cn.xiaosy.springdemo.scanner.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Demo1Test {


	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
		UserService userService = (UserService) applicationContext.getBean("userServiceDemo2");
		// 单例
		userService.test();
		userService.test();
		userService.test();

		// 多例
		userService.testA();
		userService.testA();
		userService.testA();

	}

}
