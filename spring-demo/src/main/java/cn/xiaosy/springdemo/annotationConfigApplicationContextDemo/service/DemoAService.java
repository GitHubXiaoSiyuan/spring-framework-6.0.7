package cn.xiaosy.springdemo.annotationConfigApplicationContextDemo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class DemoAService {

	@Autowired
	@Lazy
	DemoAService bService;


	@Lazy
	public  DemoAService(DemoBService demoBService) {

	}

	@Async
	public void test() {
		System.out.println(bService);
	}

}
