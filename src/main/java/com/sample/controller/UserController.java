package com.sample.controller;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sample.bean.UserBean;
import com.sample.component.SessionLoginUser;
import com.sample.component.TimeFilterLogic;
import com.sample.entity.User;
import com.sample.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private TimeFilterLogic timeLogic;

	@Autowired
	HttpSession session;

	@Autowired
	private SessionLoginUser sessionUser;

	// ユーザー一覧画面(/user/index)---GET
	@RequestMapping(value="/user/index",method=RequestMethod.GET)
	public ModelAndView user_index(ModelAndView mav) {

		mav.setViewName("user_index");
		//List<User> list=userService.findAll();
		//mav.addObject("list", list);
		List<UserBean> listBean =userService.findAllBean();

		mav.addObject("listBean", listBean);
		mav.addObject("msg", "タスクリスト一覧");
		mav.addObject("loginUser", sessionUser.getLoginUser());

		mav.addObject("flush", session.getAttribute("flush"));
		//セッションスコープにflushメッセージが存在していれば削除する。
		if(session.getAttribute("flush")!=null) {
			session.removeAttribute("flush");
		}
		return mav;
	}

	// ユーザー一覧画面(フォームからName検索)(/user/index)---POST
	@RequestMapping(value="/user/index",method=RequestMethod.POST)
	public ModelAndView userfindindex(@RequestParam("name")String name,
										@RequestParam(value="check1",required=false)boolean check1,
										@RequestParam(value="check2",required=false)boolean check2,ModelAndView mav) {
		mav.setViewName("user_index");
		mav.addObject("check1", check1);
		mav.addObject("check2", check2);
		//List<User> lists = userService.selectDate(name,check1,check2);//---
		List<UserBean> listBean = userService.selectDateBean(name,check1,check2);//---

		mav.addObject("listBean", listBean);

		return mav;
	}

	//ユーザー強制作成(/user/new2)
	@RequestMapping(value="/user/new2",method=RequestMethod.GET)
	public ModelAndView user_new2(ModelAndView mav) {

		UserBean userBean = new UserBean();
		Date date = new Date(System.currentTimeMillis());
		User user = new User();
		userBean.setId("1");
		userBean.setName("admin");
		userBean.setNickName("管理者");
		userBean.setPassword("admin");
		userBean.setActive_from(date);
		userBean.setActive_to(date);
		userBean.setUpdate_user("admin");
		userBean.setUpdate_date(date);
		userBean.setInsert_user("admin");
		userBean.setInsert_date(date);
		user = userService.changeUser(userBean);
		userService.insert(user);
		mav = new ModelAndView("redirect:/user/index");
		return mav;
	}


	//ユーザー新規作成(/user/new)
	@RequestMapping(value="/user/new",method=RequestMethod.GET)
	public ModelAndView user_new(ModelAndView mav) {

		UserBean user = new UserBean();
		Date date = new Date(System.currentTimeMillis());
		user.setActive_from(date);
		user.setActive_to(date);
		user.setInsert_user(sessionUser.getLoginUser().getName());
		user.setInsert_date(date);
		user.setUpdate_user(sessionUser.getLoginUser().getName());
		user.setUpdate_date(date);
		mav.setViewName("user_new");
		mav.addObject("flag", false);
		mav.addObject("formModel",user);
		return mav;
	}

	//ユーザー新規作成登録(/user/create)
	@RequestMapping(value="/user/create",method=RequestMethod.POST)
	public ModelAndView user_create(@ModelAttribute("formModel") @Validated UserBean userBean,
									BindingResult result, ModelAndView mav) {
		ModelAndView res = null;
		boolean errorFlag=false;
		User user = new User();

		if(!result.hasErrors()) {  //---Validateにエラーがない　かつ、IDとNAMEの重複がなければINSERT可能とする(flag=true)
			user = userService.changeUser(userBean);
			if(!userService.checkIdResult(user.getId()) && !userService.checkNameResult(user.getName())) {  // IDとNameの重複チェック
				errorFlag=true;
			}else {
				mav.addObject("msg", "IDか名前が既に登録されています。");
			}
		}

		if(errorFlag) {  //---INSERT処理
				userService.insert(user);
				session.setAttribute("flush", "登録が完了しました。");
				res = new ModelAndView("redirect:/user/index");
		}else {    //---flagがfalseの場合
			mav.setViewName("user_new");
			mav.addObject("formModel", userBean);
			res = mav;
		}
		return res;
	}

	//ユーザー詳細画面(/user/show/{id}) GET
	@RequestMapping(value="/user/show/{id}",method=RequestMethod.GET)
	public ModelAndView user_show(@PathVariable int id,ModelAndView mav) {
		mav.setViewName("user_show");
		UserBean user = userService.selectBean(id);
		mav.addObject("formModel", user);
		mav.addObject("flag", true);
		return mav;
	}

	//ユーザ詳細(編集可)画面(/user/show/{id}) POST
	@RequestMapping(value="/user/show/{id}",method=RequestMethod.POST)
	public ModelAndView user_edit(@RequestParam("id")int id,ModelAndView mav) {

		boolean editFlag=true; //--編集モードをONにする。
		mav.addObject("flag", false);  //---編集ボタンをOFFにする。

		mav.setViewName("user_show");
		mav.addObject("editFlag", editFlag); //編集モードをセット

		UserBean user = userService.selectBean(id);  //---編集するユーザー(UserTable)から情報をDBから取得し、UserBeanにラップする。
		//Date date = new Date(System.currentTimeMillis()); //---現在時刻を取得。
		user.setUpdate_user(sessionUser.getLoginUser().getName()); //---ログインしているユーザーを、Update_userにセットする。
		user.setUpdate_date(new Date(System.currentTimeMillis()));  //---現在時刻をupdate_dateにセットする。
		mav.addObject("formModel", user);

		return mav;

	}

	//ユーザー情報更新処理(/user/update)
	@RequestMapping(value="/user/update",method=RequestMethod.POST)
	public ModelAndView user_update(@ModelAttribute("formModel") @Validated UserBean userBean,
									BindingResult result, ModelAndView mav) {
		ModelAndView res = null;
		boolean errorFlag=false;
		User user = new User();

		if(!result.hasErrors()) {  //---Validateにエラーがない　かつ、IDとNAMEの重複がなければINSERT可能とする(flag=true)
			user = userService.changeUser(userBean);
			errorFlag = true;
		}

		if(errorFlag) {  //---UPDATE処理
				userService.update(user);
				session.setAttribute("flush", "更新が完了しました。");
				res = new ModelAndView("redirect:/user/index");
		}else {    //---errorFlagがfalseの場合
			mav.setViewName("user_new");
			mav.addObject("formModel", userBean);
			res = mav;
		}
		return res;
	}

	//ユーザー情報削除(/user/delete/{id})
	@RequestMapping(value="/user/delete/{id}",method=RequestMethod.GET)
	public ModelAndView user_delete(@PathVariable int id,ModelAndView mav) {
		userService.delete(id);
		session.setAttribute("flush", "削除が完了しました。");
		ModelAndView res = new ModelAndView("redirect:/user/index");
		return res;
	}

}
