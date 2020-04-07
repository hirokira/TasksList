package com.sample.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sample.component.SessionLoginUser;
import com.sample.entity.User;
import com.sample.service.UserService;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	@Autowired
	HttpSession session;

	@Autowired
	private SessionLoginUser sessionUser;




	// ログイン画面(/)
	@RequestMapping(value="/",method=RequestMethod.GET)
	public ModelAndView login(ModelAndView mav) {

		mav.setViewName("login");
		mav.addObject("msg", "ログイン画面");
		return mav;
	}

	// ログイン結果(/)
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public ModelAndView loginCheck(@RequestParam(value="name") String name,
									@RequestParam(value="password") String password,
									ModelAndView mav) {

		boolean checkFlag = false;
		ModelAndView res = null;
		User user = null;

		// ログインチェック　(true:checkFlag=true)
		if(name!=null && !name.equals("") && password!=null && !password.equals("")) {
			try {
				user = userService.checkResult(name, password);
			}catch(Exception e) {}

			if(user!=null) {
				checkFlag=true;
			}
		}

		if(checkFlag) {
			//セッションスコープに保存
			if(user.getNickName()==null) {  //ニックネーム(表示名)がNullならば、ユーザー名をニックネームにする。
				user.setNickName(user.getName());
			}
			sessionUser.setLoginUser(user);

			session.setAttribute("loginUser", user); //ログインユーザー名
			session.setAttribute("flush", "ログインに成功しました。"); //フラッシュメッセージ
			//リダイレクト /tasks
			res = new ModelAndView("redirect:/tasks");
		}else {
			mav.addObject("msg", "ログイン画面");
			mav.addObject("msg2", "ユーザー名かパスワードが異なります。");
			mav.addObject("user", user);
			mav.setViewName("login");
			res = mav;
		}
		return res;
	}



}
