package com.sample.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.bean.UserBean;
import com.sample.entity.User;
import com.sample.mapper.UserMapper;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;



	public List<User> findAll(){
		return userMapper.findAll();
	}


	public void insert(User user) {
		userMapper.insert(user.getId(),user.getName(), user.getPassword(), user.getNickName(), user.getActive_from(), user.getActive_to());
	}

	public User select(int id) {
		return userMapper.select(id);
	}

	public UserBean selectBean(int id) {
		UserBean bean = new UserBean(userMapper.select(id));
		return bean;
	}


	public User checkResult(String name,String password) {
		return userMapper.checkResult(name,password);
	}

	public boolean checkIdResult(int id) {
		if(userMapper.checkIdResult(id)==null) {
			return false;
		}else {
			return true;
		}
	}

	public boolean checkNameResult(String name) {
		if(userMapper.checkNameResult(name)==null) {
			return false;
		}else {
			return true;
		}
	}


	public User changeUser(UserBean bean) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		User user = new User();
		user.setId(Integer.parseInt(bean.getId()));
		user.setName(bean.getName());
		user.setNickName(bean.getNickName());
		user.setPassword(bean.getPassword());
		user.setActive_from(df.format(bean.getActive_from()));
		user.setActive_to(df.format(bean.getActive_to()));
		return user;
	}

}
