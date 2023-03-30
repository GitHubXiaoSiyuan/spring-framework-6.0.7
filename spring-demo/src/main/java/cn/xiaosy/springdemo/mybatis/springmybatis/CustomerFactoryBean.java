package cn.xiaosy.springdemo.mybatis.springmybatis;

import cn.xiaosy.springdemo.mybatis.mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

//@Component
// 因为 applicationContext.registerBeanDefinition("userMapper", beanDefinition); 生成了2 个bean 所以这里不用在用注解了
public class CustomerFactoryBean implements FactoryBean {

	private SqlSession sqlSession;

	private Class mapperClass;	// UserMapper OrderMapper


	// 这里最多只能定义一个 Mapper ，所以 method 2 行不通
	public CustomerFactoryBean(Class mapperClass) {
		this.mapperClass = mapperClass;
	}

	@Autowired
	public void setSqlSession(SqlSessionFactory sqlSessionFactory) {
		// method1
//		sqlSessionFactory.getConfiguration().addMapper(UserMapper.class);

		// method2
		sqlSessionFactory.getConfiguration().addMapper(mapperClass);

		this.sqlSession = sqlSessionFactory.openSession();
	}

	@Override
	public Object getObject() throws Exception {
//		Object proxyInstance = Proxy.newProxyInstance(CustomerFactoryBean.class.getClassLoader(), new Class[]{ UserMapper.class}, new InvocationHandler() {
//			@Override
//			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//				System.out.println(method.getName());
//				return null;
//			}
//		});
//		return proxyInstance;


		// mybatis
		// method1
//		UserMapper mapper = sqlSession.getMapper(UserMapper.class);


		//
		Object mapper = sqlSession.getMapper(mapperClass);
		return mapper;
	}

	@Override
	public Class<?> getObjectType() {
		// method1
//		return UserMapper.class;
		
		// method2
		return mapperClass;
	}

	@Override
	public boolean isSingleton() {
		return FactoryBean.super.isSingleton();
	}
}
