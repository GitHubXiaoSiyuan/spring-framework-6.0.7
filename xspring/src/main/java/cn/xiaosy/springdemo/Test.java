package cn.xiaosy.springdemo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import cn.xiaosy.springdemo.service.UserService;

public class Test {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = (UserService) annotationConfigApplicationContext.getBean("userService");
        userService.test();

//        System.out.println(annotationConfigApplicationContext.getBean("userService"));
//        System.out.println(annotationConfigApplicationContext.getBean("userService"));
//        System.out.println(annotationConfigApplicationContext.getBean("userService"));
//        System.out.println(annotationConfigApplicationContext.getBean("userService"));
    }

}
