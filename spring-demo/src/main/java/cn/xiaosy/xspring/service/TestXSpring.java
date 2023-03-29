package cn.xiaosy.xspring.service;

import cn.xiaosy.xspring.spring.XiaosySpringContext;

public class TestXSpring {

    public static void main(String[] args) {

		System.out.println("hi");

        // 一般传入 spring.xml 或者 配置类AppConfig
        XiaosySpringContext xiaosySpringContext = new XiaosySpringContext(AppConfig.class);
//        UserService userService = (UserService) xiaosySpringContext.getBean("userService");

        // 5. AOP 测试
        // 不用接口，用类 会报错 com.sun.proxy.$Proxy5 cannot be cast to cn.xiaosy.xSpring.service.UserService
        UserInterface userService = (UserInterface) xiaosySpringContext.getBean("userService");
        userService.test();

        // 4. 测试 Autowired 注解
//        UserService userService = (UserService) xiaosySpringContext.getBean("userService");
//        userService.test();


        // 3.测试 Autowired 注解
//        UserService userService = (UserService) xiaosySpringContext.getBean("userService");
//        userService.test();

        // 2. 测试默认 beanName
//        System.out.println(xiaosySpringContext.getBean("orderService"));

        // 1. 测试是否创建的是单例 bean 和 多例 bean
//        System.out.println(xiaosySpringContext.getBean("userService"));
//        System.out.println(xiaosySpringContext.getBean("userService"));
//        System.out.println(xiaosySpringContext.getBean("userService"));
//        System.out.println(xiaosySpringContext.getBean("userService"));



    }

}
