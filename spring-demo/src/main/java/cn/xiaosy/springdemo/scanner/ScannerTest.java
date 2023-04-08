package cn.xiaosy.springdemo.scanner;

import cn.xiaosy.springdemo.scanner.service.AbsUserService;
import cn.xiaosy.springdemo.scanner.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ScannerTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
//		AbsUserService absUserService = (AbsUserService) applicationContext.getBean("absUserService");
//		absUserService.testC();

//		System.out.println(applicationContext.getBean("userService.SVipService"));


		System.out.println(applicationContext.getBean("AService"));
	}

}
