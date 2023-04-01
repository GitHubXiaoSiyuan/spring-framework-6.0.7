package cn.xiaosy.springdemo.scanner.demo2;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {


	@Autowired
	public OrderService orderServiceDemo1;

	public void test() {
		System.out.println(orderServiceDemo1);
	}


}
