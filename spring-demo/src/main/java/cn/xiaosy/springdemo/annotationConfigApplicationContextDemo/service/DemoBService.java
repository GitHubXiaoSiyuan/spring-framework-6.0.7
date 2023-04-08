package cn.xiaosy.springdemo.annotationConfigApplicationContextDemo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class DemoBService {

	@Autowired
	@Lazy
	DemoAService demoAService;

	@Lazy
	public  DemoBService(DemoAService demoAService) {

	}

}
