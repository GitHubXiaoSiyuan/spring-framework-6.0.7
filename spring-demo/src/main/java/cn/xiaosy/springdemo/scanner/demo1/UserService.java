package cn.xiaosy.springdemo.scanner.demo1;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component("userServiceDemo1")
public class UserService {


	@Autowired
	public OrderService orderServiceDemo1;

	public void test() {
		System.out.println(orderServiceDemo1);
	}

	public void testA() {
		System.out.println(getPrototypeOrderService());
	}

	@Lookup("orderServiceDemo1")
	public OrderService getPrototypeOrderService() {
		return null;
	}

}
