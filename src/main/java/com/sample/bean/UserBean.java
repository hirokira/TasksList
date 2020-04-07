package com.sample.bean;

import java.sql.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.sample.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBean {

	public UserBean(){

	}

	public UserBean(User user){
		this.id = Integer.toString(user.getId());
		this.name = user.getName();
		this.nickName = user.getNickName();
		this.active_from =Date.valueOf(user.getActive_from());
		this.active_to = Date.valueOf(user.getActive_to());
	}

	@NotNull
	@NotEmpty(message="IDを入力してください。")
	private String id;

	@NotNull
	@NotEmpty(message="名前を入力してください。")
	private String name;

	@NotNull
	@NotEmpty(message="パスワードを入力してください。")
	private String password;

	private String nickName;

	private Date active_from;

	private Date active_to;

}
