package cn.xiaosy.springdemo.annotationConfigApplicationContextDemo.service;

import jakarta.annotation.PostConstruct;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope("singleton")
public class UserService implements InitializingBean {


	@Autowired(required = false)
    private OrderService orderService;



    private User admin;




	public UserService() {
		System.out.println("使用无参构造方法");
	}

	@Autowired
	public UserService(OrderService orderService) {
		this.orderService = orderService;
		System.out.println("使用有参构造方法1");
	}


//	@Autowired
	public UserService(OrderService orderService, OrderService orderService2) {
		this.orderService = orderService;
		System.out.println("使用有参构造方法2");
	}

//	@Autowired
	public UserService(OrderService orderService, String demoStr) {
		this.orderService = orderService;
		System.out.println("使用有参构造方法2");
	}

	@PostConstruct
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







	@Autowired
	public JdbcTemplate jdbcTemplate;

	/**
	 * 表结构
	 * CREATE TABLE `x_sys_tb_user` (
	 *   `user_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
	 *   `user_name` varchar(255) COLLATE utf8_bin DEFAULT NULL
	 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
	 */

	@Transactional
	public void testTransactionManager() {

		// 例子1
//		jdbcTemplate.execute("INSERT INTO `demo_db`.`x_sys_tb_user`(`user_id`, `user_name`) VALUES ('1', '1');");
//		throw new NullPointerException();

		// 例子2
		jdbcTemplate.execute("INSERT INTO `demo_db`.`x_sys_tb_user`(`user_id`, `user_name`) VALUES ('1', '1');");
		subA();
	}

	// propagation 传播
	// 设置传播类型
	@Transactional(propagation = Propagation.NEVER)
	public void subA() {
		jdbcTemplate.execute("INSERT INTO `demo_db`.`x_sys_tb_user`(`user_id`, `user_name`) VALUES ('2', '2');");
	}


	// 例子 3
	@Autowired
	public UserService userServiceBase;

	@Transactional
	public void testTransactionManagerA() {
		jdbcTemplate.execute("INSERT INTO `demo_db`.`x_sys_tb_user`(`user_id`, `user_name`) VALUES ('1', '1');");
		userServiceBase.subA();
	}

	@Transactional
	public void testTransactionManagerB() {
		jdbcTemplate.execute("INSERT INTO `demo_db`.`x_sys_tb_user`(`user_id`, `user_name`) VALUES ('1', '1');");
		UserService curUserService = (UserService) AopContext.currentProxy();
		curUserService.subA();
	}
}
