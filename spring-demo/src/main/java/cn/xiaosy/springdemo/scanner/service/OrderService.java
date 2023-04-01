package cn.xiaosy.springdemo.scanner.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.RequestScope;

@Component
//@Scope(WebApplicationContext.SCOPE_REQUEST)
// @RequestScope 等价于  @Scope(WebApplicationContext.SCOPE_REQUEST)
// WebApplicationContext.SCOPE_REQUEST 接收到某个请求的时候才需要创建
public class OrderService {
}
