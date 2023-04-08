package cn.xiaosy.springdemo.scanner.demo2;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("userServiceDemo2")
public class UserService {


	@Autowired
	public OrderService orderServiceDemo2;

	public void test() {
		System.out.println(orderServiceDemo2);
	}


}
