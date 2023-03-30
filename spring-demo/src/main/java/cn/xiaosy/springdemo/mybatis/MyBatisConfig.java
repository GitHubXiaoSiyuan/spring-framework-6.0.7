package cn.xiaosy.springdemo.mybatis;

import cn.xiaosy.springdemo.mybatis.mapper.UserMapper;
import cn.xiaosy.springdemo.mybatis.springmybatis.CustomerImportBeanDefinitionRegistrar;
import cn.xiaosy.springdemo.mybatis.springmybatis.CustomerScan;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.InputStream;

@ComponentScan("cn.xiaosy.springdemo.mybatis")
//@Configuration
//@Import(CustomerImportBeanDefinitionRegistrar.class)	// method 3

// method 4
@CustomerScan("cn.xiaosy.springdemo.mybatis.mapper")
public class MyBatisConfig {



	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		InputStream is = Resources.getResourceAsStream("mybatis.xml");
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
		return sqlSessionFactory;
	}







/*	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource());
		return factoryBean.getObject();
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl("jdbc:mysql://localhost:3306/demo_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai");
		dataSource.setUsername("root");
		dataSource.setPassword("123456");
		return dataSource;
	}*/



//
//	@Bean
//	public UserMapper userMapper() throws Exception {
//		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory());
//		return sqlSessionTemplate.getMapper(UserMapper.class);
//	}

}
