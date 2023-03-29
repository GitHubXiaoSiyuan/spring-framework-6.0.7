package cn.xiaosy.springdemo.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class UserService implements InitializingBean {

    @Autowired
    private OrderService orderService;


    private User admin;

//    @PostConstruct
    public void initAdmin() {
        // 从数据库查数据 找到管理员信息
        // 模拟数据
        User admin = new User();
        admin.setUserId("01");
        admin.setUserName("管理员");
        this.admin = admin;
    }



    public void test() {
        System.out.println("UserService.test 原有逻辑:" + orderService + "  " + admin.getUserName() + admin.getUserId());
    }



    public void afterPropertiesSet() throws Exception {
        initAdmin();
    }
}
