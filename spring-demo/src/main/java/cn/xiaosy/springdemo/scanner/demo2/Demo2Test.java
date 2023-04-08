package cn.xiaosy.springdemo.scanner.demo2;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Demo2Test {


	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
		UserService userService = (UserService) applicationContext.getBean("userServiceDemo2");
		userService.test();

	}

}
