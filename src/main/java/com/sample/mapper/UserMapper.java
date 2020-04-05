package com.sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.sample.entity.User;

@Mapper
@Component
public interface UserMapper {

	List<User> findAll();

	User select(int id);

	User checkResult(@Param("name")String name,@Param("password") String password);



}
