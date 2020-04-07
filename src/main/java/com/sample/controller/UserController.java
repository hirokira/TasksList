package com.sample.controller;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sample.bean.UserBean;
import com.sample.entity.User;
import com.sample.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	// ユーザー一覧画面(/user/index)
	@RequestMapping(value="/user/index",method=RequestMethod.GET)
	public ModelAndView tasks_index(ModelAndView mav) {

		mav.setViewName("user_index");
		List<User> list=userService.findAll();
		mav.addObject("list", list);
		mav.addObject("msg", "タスクリスト一覧");
		return mav;
	}

	//ユーザー強制作成(/user/new2)
/*	@RequestMapping(value="/user/new2",method=RequestMethod.GET)
	public ModelAndView user_new2(ModelAndView mav) {

		User user = new User();
		user.setId(4);
		user.setName("asrun");
		user.setNickName("asss");
		user.setPassword("1234");
		user.setActive_from("2020-03-31");
		user.setActive_to("2021-12-31");
		userService.insert(user);
		mav = new ModelAndView("redirect:/user/index");
		return mav;
	}
*/

	//ユーザー新規作成(/user/new)
	@RequestMapping(value="/user/new",method=RequestMethod.GET)
	public ModelAndView user_new(ModelAndView mav) {

		UserBean user = new UserBean();
		Date date = new Date(System.currentTimeMillis());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		user.setActive_from(date);
		user.setActive_to(date);
		mav.setViewName("user_new");
		mav.addObject("formModel",user);
		return mav;
	}

	//ユーザー新規作成登録(/user/create)
	@RequestMapping(value="/user/create",method=RequestMethod.POST)
	public ModelAndView user_create(@ModelAttribute("formModel") @Validated UserBean userBean,
									BindingResult result, ModelAndView mav) {
		ModelAndView res = null;
		boolean flag=false;
		User user = new User();
		if(!result.hasErrors()) {  //---Validateにエラーがない　かつ、IDとNAMEの重複がなければINSERT可能とする(flag=true)
			user = userService.changeUser(userBean);
			if(!userService.checkIdResult(user.getId()) && !userService.checkNameResult(user.getName())) {  // IDとNameの重複チェック
				flag=true;
			}else {
				mav.addObject("msg", "IDか名前が既に登録されています。");
			}
		}

		if(flag) {  //---INSERT処理
				userService.insert(user);
				res = new ModelAndView("redirect:/user/index");
		}else {    //---flagがfalseの場合
			mav.setViewName("user_new");
			mav.addObject("formModel", userBean);
			res = mav;
		}
		return res;
	}

	//ユーザー詳細画面(/user/show/{id})
	@RequestMapping(value="/user/show/{id}",method=RequestMethod.GET)
	public ModelAndView user_show(@PathVariable int id,ModelAndView mav) {
		mav.setViewName("user_show");
		UserBean user = userService.selectBean(id);
		mav.addObject("formModel", user);
		return mav;

	}


}
