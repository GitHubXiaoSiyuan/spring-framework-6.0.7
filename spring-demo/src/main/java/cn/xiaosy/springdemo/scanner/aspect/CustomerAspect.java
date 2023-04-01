package cn.xiaosy.springdemo.scanner.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CustomerAspect {

	@Before("execution(public void cn.xiaosy.springdemo.scanner.service.UserService.test())")
	public void customerBefore(JoinPoint joinPoint) {
		System.out.println("customerBefore");
	}


}
