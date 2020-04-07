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
import org.springframework.web.servlet.ModelAndView;

import com.sample.bean.TaskBean;
import com.sample.component.SessionLoginUser;
import com.sample.service.TaskService;

@Controller
public class TasksController {

	@Autowired
	HttpSession session;

	@Autowired
	private TaskService tasksService;

	@Autowired
	private SessionLoginUser sessionUser;


	// task 一覧
	@RequestMapping(value="/tasks",method=RequestMethod.GET)
	public ModelAndView tasks_index(ModelAndView mav) {
		mav.setViewName("task_index");
		//ログインユーザー名の取得
		mav.addObject("flush", session.getAttribute("flush"));
		//mav.addObject("loginUser", session.getAttribute("loginUser"));
		mav.addObject("loginUser", sessionUser.getLoginUser());

		List<TaskBean> listBean = tasksService.findAllBean(); //TaskList一覧を型変換を行った状態で取得。
		Date nowTime =new Date(System.currentTimeMillis());
		mav.addObject("nowTime", nowTime);
		mav.addObject("list", listBean);

		return mav;
	}


	// Task詳細
	@RequestMapping(value="/tasks/show/{id}",method=RequestMethod.GET)
	public ModelAndView tasks_show(@PathVariable int id,ModelAndView mav) {

		//mav.addObject("loginUser", session.getAttribute("loginUser"));
		mav.addObject("loginUser", sessionUser.getLoginUser());
		mav.setViewName("task_show");
		//Task task = tasksService.select(id);
		TaskBean bean = tasksService.selectBean(id);
		mav.addObject("formModel", bean);
		return mav;
	}

	// Task新規作成
	@RequestMapping(value="/tasks/new",method=RequestMethod.GET)
	public ModelAndView tasks_new(ModelAndView mav) {
		mav.setViewName("task_new");
		TaskBean bean = new TaskBean();
		mav.addObject("loginUser", sessionUser.getLoginUser());
		bean.setDue_date(new Date(System.currentTimeMillis()));
		bean.setInsert_date(new Date(System.currentTimeMillis()));
		bean.setUpdate_date(new Date(System.currentTimeMillis()));
		mav.addObject("formModel", bean);
		return mav;
	}

	// Task新規作成Create
	@RequestMapping(value="/tasks/create",method=RequestMethod.POST)
	public ModelAndView tasks_create(@ModelAttribute("formModel") @Validated TaskBean task,
									BindingResult result , ModelAndView mav) {

		ModelAndView res = null;
		mav.setViewName("task_new");
		tasksService.insert(task);

		res = new ModelAndView("redirect:/tasks");

		return res;

	}

}
