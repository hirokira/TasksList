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

	List<User> selectName(@Param("name")String name);

	List<User> selectNameOnly(@Param("name")String name);

	User findSelectUser(String user);

	void insert(User user);

	void update(User user);

	User select(int id);

	void delete(int id);

	User checkResult(@Param("name")String name,@Param("password") String password);

	User checkIdResult(@Param("id")Integer id);

	User checkNameResult(@Param("name")String name);



}
