package cn.xiaosy.springdemo.scanner.demo2;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class OrderService {
}
