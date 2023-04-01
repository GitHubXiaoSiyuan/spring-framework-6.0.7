package cn.xiaosy.springdemo.scanner.service;

import jakarta.annotation.ManagedBean;
import jakarta.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;



@Component
@Conditional(CustomerCondition.class)
//@CustomerExclude
//@ManagedBean // 有 @ManagedBean 注解，也可以成为 Bean
//@Named	// 有 @Named 注解，也可以成为 Bean
public class UserService {

	@Component
	public static class  SVipService {

	}


	/**
	 * @Autowired 的 required 属性 默认为 true，必须要有
	 * boolean required() default true;
	 *
	 *
	 * @Scope(WebApplicationContext.SCOPE_REQUEST)
	 * WebApplicationContext.SCOPE_REQUEST 接收到某个请求的时候才需要创建
	 *
	 * 这两个互相矛盾，spring 的处理逻辑是
	 *
	 * 先 生成一个代理对象来给这个属性赋值
	 * 真正收到请求的时候，再利用这个代理对象去创建 OrderService 的 bean
	 *
	 *
	 * @Scope 注解的 proxyMode 属性来配置这个代理对象 的代理机制 （比如：CGLIB）
	 * ScopedProxyMode proxyMode() default ScopedProxyMode.DEFAULT;
	 *
	 * @Scope 注解的 proxyMode 属性没有配置就按照 @ComponentScan 注解的 scopedProxy 属性来配置， scopedProxy = ScopedProxyMode.DEFAULT
	 *
	 * @ComponentScan 注解的 scopedProxy 属性批量配置所有的 bean 在出现 request 和 session 时候创建的临时代理对象，用这个代理对象给属性赋值
	 * （注：和AOP 没有关系）
	 *
	 */
	@Autowired
	public OrderService orderService;

	public void test() {
		System.out.println("UserService.test()");
	}

}
