package com.sample.component;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.sample.entity.User;



//-------   ログオンユーザー用セッション管理クラス ------------

@Component
@Scope(value= "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionLoginUser implements Serializable{


	private User loginUser;

	public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User loginUser) {
		this.loginUser=loginUser;
	}
}
