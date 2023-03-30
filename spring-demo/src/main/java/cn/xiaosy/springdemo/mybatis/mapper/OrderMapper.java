package cn.xiaosy.springdemo.mybatis.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface OrderMapper {

	@Select("SELECT user_name FROM x_sys_tb_user WHERE user_id = #{userId}")
//	@Select("SELECT * FROM x_sys_tb_user")
	String getUserNameById(@Param("userId") String userId);

}
