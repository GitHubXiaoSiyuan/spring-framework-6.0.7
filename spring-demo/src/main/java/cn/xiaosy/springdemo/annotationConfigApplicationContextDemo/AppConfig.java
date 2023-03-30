package cn.xiaosy.springdemo.annotationConfigApplicationContextDemo;

import cn.xiaosy.springdemo.annotationConfigApplicationContextDemo.service.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@ComponentScan("cn.xiaosy.springdemo.annotationConfigApplicationContextDemo")

//@EnableAspectJAutoProxy // 开启 aop
@EnableTransactionManagement  // 开启事务 需要 EnableTransactionManagement 和 Configuration，只有 EnableTransactionManagement不行
@Configuration
@EnableAspectJAutoProxy(exposeProxy = true) // 公开 spring aop 的代理
@EnableAsync
public class AppConfig {

	@Bean
	public OrderService orderService1() {
		return new OrderService();
	}

	@Bean
	public OrderService orderService2() {
		return new OrderService();
	}


	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}


	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(dataSource());
		return transactionManager;
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl("jdbc:mysql://localhost:3306/demo_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai");
		dataSource.setUsername("root");
		dataSource.setPassword("123456");
		return dataSource;
	}


}
