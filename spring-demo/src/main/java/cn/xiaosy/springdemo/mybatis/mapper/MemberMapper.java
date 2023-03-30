package cn.xiaosy.springdemo.mybatis.mapper;

import org.apache.ibatis.annotations.Select;

public interface MemberMapper {

	@Select("select 1+1;")
	public String getResult();

}
