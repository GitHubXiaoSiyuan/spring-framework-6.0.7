package cn.xiaosy.springdemo.scanner.demo1;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("orderServiceDemo1")
@Scope("prototype")
public class OrderService {
}
