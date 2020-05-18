package com.sample.entity;


/**
 * ログインユーザのユーザ名、パスワードを格納するためのEntity
 * @author aoi
 *
 */
public class LoginUser {

	private Long userId;

	private String userName;

	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}




}
