package cn.xiaosy.springdemo.scanner.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
public abstract class AbsUserService {

	// No qualifying bean of type 'void' available
	@Lookup
	public void test() {
		System.out.println("test");
	}

	// No qualifying bean of type 'java.lang.Object' available
	@Lookup
	public Object testA() {
		System.out.println("testA");
		return null;
	}

	// 不会报错
	@Lookup
	public OrderService testB() {
		System.out.println("testB");
		return null;
	}

	// 不会报错
	@Lookup("orderService")
	public void testC() {
		System.out.println("testC");
	}


}
