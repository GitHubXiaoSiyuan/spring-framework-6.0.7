package cn.xiaosy.xspring.service;

import cn.xiaosy.xspring.spring.*;

@Component("userService")
@Scope("prototype")
public class UserService implements BeanNameAware, InitializingBean,UserInterface {

    @Autowired
    private OrderService orderService;


    private String beanName;

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }


    private String initFieldDemo;

    public void afterPropertiesSet() {
        // 初始化
        initFieldDemo = "demo";
    }

    public void test() {
        System.out.println(orderService);
    }



}
