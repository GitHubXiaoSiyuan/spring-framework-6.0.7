package cn.xiaosy.springdemo.annotationConfigApplicationContextDemo.aspect;

import cn.xiaosy.springdemo.annotationConfigApplicationContextDemo.service.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class XiaosyAspect {

    @Before("execution(public void cn.xiaosy.springdemo.annotationConfigApplicationContextDemo.service.UserService.test())")
    public void xiaosyBefore(JoinPoint joinPoint) {
		System.out.println("UserService.test aop调用前逻辑");
    }

}
