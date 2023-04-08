package cn.xiaosy.springdemo.annotationConfigApplicationContextDemo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderService {

	@Autowired(required = false)
	UserService userService;

//	@Autowired
//	public OrderService(UserService userService) {
//		this.userService = userService;
//	}


}
