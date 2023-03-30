package cn.xiaosy.springdemo.mybatis.service;

import cn.xiaosy.springdemo.mybatis.dto.User;
import cn.xiaosy.springdemo.mybatis.mapper.MemberMapper;
import cn.xiaosy.springdemo.mybatis.mapper.OrderMapper;
import cn.xiaosy.springdemo.mybatis.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

	@Autowired
	public UserMapper userMapper;


	@Autowired
	public OrderMapper orderMapper;

	@Autowired
	public MemberMapper memberMapper;

	public void test(String userId) {
		System.out.println(userMapper.getUserNameById(userId));
		System.out.println(orderMapper.getUserNameById(userId));

		System.out.println(memberMapper.getResult());
	}

}
