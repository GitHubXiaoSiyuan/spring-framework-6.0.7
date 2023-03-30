package cn.xiaosy.springdemo.annotationConfigApplicationContextDemo;

import cn.xiaosy.springdemo.annotationConfigApplicationContextDemo.service.DemoAService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnotationConfigApplicationContextTest {

    public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AppConfig.class);



		// 测试 @Async 注解 和 循环依赖
		DemoAService demoAService = (DemoAService) annotationConfigApplicationContext.getBean("demoAService");
		demoAService.test();

//		UserService userService = (UserService) annotationConfigApplicationContext.getBean("userService");



		// 测试事务
//		userService.testTransactionManager();
//		userService.testTransactionManagerA();
//		userService.testTransactionManagerB();


//        userService.test();

//        System.out.println(annotationConfigApplicationContext.getBean("userService"));
//        System.out.println(annotationConfigApplicationContext.getBean("userService"));
//        System.out.println(annotationConfigApplicationContext.getBean("userService"));
//        System.out.println(annotationConfigApplicationContext.getBean("userService"));
    }

}
