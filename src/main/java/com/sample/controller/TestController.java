package com.sample.controller;
/**
 * hello画面のコントローラクラス
 * 今回はログイン済みユーザのユーザ名をビューに渡す処理のみを行う
 * @author aoi
 *
 */

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sample.component.SessionLoginUser;
import com.sample.mapper.UserMapper;

@Controller
public class TestController {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	HttpSession session;

	@Autowired
	private SessionLoginUser sessionUser;

	/**
	 * ログイン成功時に呼び出されるメソッド
	 * SecurityContextHolderから認証済みユーザの情報を取得しモデルへ追加する
	 * @param model リクエストスコープ上にオブジェクトを載せるためのmap
	 * @return helloページのViewName
	 */
//	@RequestMapping("/hello")
//	private String init(Model model) {
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		//Principalからログインユーザの情報を取得
//		String userName = auth.getName();
//		model.addAttribute("userName", userName);
//
//		User user =userMapper.findSelectUser(userName);
//		model.addAttribute("formModel", user);
//
//		sessionUser.setLoginUser(user);
//
//		session.setAttribute("loginUser", user); //ログインユーザー名
//		session.setAttribute("flush", "ログインに成功しました。"); //フラッシュメッセージ
//
//		return "redirect:/tasks";
//
//	}



}