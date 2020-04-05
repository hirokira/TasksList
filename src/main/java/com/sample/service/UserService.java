package com.sample.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.entity.User;
import com.sample.mapper.UserMapper;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;



	public List<User> findAll(){
		return userMapper.findAll();
	}

	public User select(int id) {
		return userMapper.select(id);
	}

	public User checkResult(String name,String password) {
		return userMapper.checkResult(name,password);
	}

}
