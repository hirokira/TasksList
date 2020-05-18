package com.sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.sample.entity.LoginUser;

@Mapper
@Component
public interface LoginUserMapper {
	List<LoginUser> findAll();

	LoginUser findUser(String userName);



}
