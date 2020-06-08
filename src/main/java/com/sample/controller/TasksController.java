package com.sample.controller;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sample.bean.TaskBean;
import com.sample.bean.UserBean;
import com.sample.component.SessionLoginUser;
import com.sample.service.TaskService;
import com.sample.service.UserService;

@Controller
public class TasksController {

	@Autowired
	HttpSession session;

	@Autowired
	private TaskService tasksService;

	@Autowired
	private UserService userService;

	@Autowired
	private SessionLoginUser sessionUser;


	 @InitBinder
	 public void initBinder(WebDataBinder binder) {
	        // 未入力のStringをnullに設定する
	        binder.registerCustomEditor(java.util.Date.class, new StringTrimmerEditor(true));
	    }

	// task 一覧GET
	@RequestMapping(value="/tasks",method=RequestMethod.GET)
	public ModelAndView tasks_index(ModelAndView mav) {
		mav.setViewName("task_index");
		//ログインユーザー名の取得
		mav.addObject("flush", session.getAttribute("flush"));
		mav.addObject("loginUser", sessionUser.getLoginUser());

		//ユーザーを選択するため、ユーザーテーブルからユーザー一覧を取得しadd
		List<UserBean> userList =userService.findAllBean();
		mav.addObject("userList", userList);

		List<TaskBean> listBean = tasksService.findAllBean(); //TaskList一覧をTaskBean型に変換を行った状態で取得。
		mav.addObject("list", listBean);

		//セッションスコープにflushメッセージが存在していれば削除する。
		if(session.getAttribute("flush")!=null) {
			session.removeAttribute("flush");
		}
		return mav;
	}

	//task一覧　フィルター状態での一覧
	@RequestMapping(value="/tasks",method=RequestMethod.POST)
	public ModelAndView tasks_filter(@RequestParam("name")String name,
									@RequestParam(value="check1",required=false)boolean check1,
									@RequestParam(value="check2",required=false)boolean check2,
									@RequestParam(value="check3",required=false)boolean check3,
									@RequestParam(value="check4",required=false)boolean check4,
									ModelAndView mav) {
		mav.setViewName("task_index");
		//ログインユーザー名の取得
		mav.addObject("flush", session.getAttribute("flush"));
		mav.addObject("loginUser", sessionUser.getLoginUser());

		//ユーザーを選択するため、ユーザーテーブルからユーザー一覧を取得しadd
		List<UserBean> userList =userService.findAllBean();
		mav.addObject("userList", userList);

		//--名前とチェックボックスを条件にマッチするタスクリストを作成。
		List<TaskBean> listBean = tasksService.selectDateBean(name, check1, check2,check3,check4);
		mav.addObject("list", listBean);

		return mav;

	}

	// Task新規作成
	@RequestMapping(value="/tasks/new",method=RequestMethod.GET)
	public ModelAndView tasks_new(ModelAndView mav) {

		//ユーザーを選択するため、ユーザーテーブルからユーザー一覧を取得しadd
		List<UserBean> listBean =userService.findAllBean();
		mav.addObject("listBean", listBean);

		mav.setViewName("task_new");

		TaskBean bean = new TaskBean();
		mav.addObject("loginUser", sessionUser.getLoginUser());
		bean.setDue_date(null);
		//bean.setDue_date(new Date(System.currentTimeMillis()));
		bean.setInsert_date(new Date(System.currentTimeMillis()));
		bean.setUpdate_date(new Date(System.currentTimeMillis()));

		//---登録ユーザーと更新ユーザーにログインユーザー情報をセットする。
		bean.setInsert_user(sessionUser.getLoginUser().getName());
		bean.setUpdate_user(sessionUser.getLoginUser().getName());

		//---登録時のバージョンは"1"固定なので、セットする。
		bean.setVersion(1);

		mav.addObject("formModel", bean);
		return mav;
	}

	// Task新規作成(強制)
	/*@RequestMapping(value="/tasks/new2",method=RequestMethod.GET)
	public ModelAndView tasks_new2(ModelAndView mav) {
		TaskBean bean = new TaskBean();
		bean.setId(2);
		bean.setTitle("タイトル");
		bean.setDetail("内容");
		bean.setImportance(1);
		bean.setCompletion_date(new Date(System.currentTimeMillis()));
		bean.setUser(sessionUser.getLoginUser().getName());
		bean.setInsert_user(sessionUser.getLoginUser().getName());
		bean.setInsert_date(new Date(System.currentTimeMillis()));
		bean.setUpdate_user(sessionUser.getLoginUser().getName());
		bean.setUpdate_date(new Date(System.currentTimeMillis()));
		//mav.addObject("loginUser", sessionUser.getLoginUser());
		bean.setDue_date(new Date(System.currentTimeMillis()));

		//---ToDo---2020/04/25 追加　beanをEntityに変換してInsertに凸る。
		//tasksService.changeTask(bean);
		tasksService.insert(tasksService.changeTask(bean));
		//mav.addObject("formModel", bean);
		ModelAndView res = new ModelAndView("redirect:/tasks");
		return res;
	}*/

	// Task新規作成Create
	@RequestMapping(value="/tasks/create",method=RequestMethod.POST)
	public ModelAndView tasks_create(@ModelAttribute("formModel") @Validated TaskBean task,
									BindingResult result , ModelAndView mav) {
		ModelAndView res = null;

		if(!result.hasErrors() /*&& !tasksService.checkIdResult(task.getId())*/) {//---バリデーション結果にエラーがないかつ重複IDがない
			tasksService.insert(tasksService.changeTask(task));//---TaskBean型のオブジェクトをTask型に変換し、INSERT処理を行う。
			session.setAttribute("flush", "タスクの登録が完了しました。");
			res = new ModelAndView("redirect:/tasks");
		}else {
			mav.setViewName("task_new");
			List<UserBean> listBean =userService.findAllBean();
			mav.addObject("listBean", listBean);
			mav.addObject("msg", "IDが既に登録されています。");
			mav.addObject("formModel", task);
			res = mav;
		}
		return res;
	}

	// Task詳細画面
	@RequestMapping(value="/tasks/show/{id}",method=RequestMethod.GET)
	public ModelAndView tasks_show(@PathVariable int id,ModelAndView mav) {
		mav.addObject("loginUser", sessionUser.getLoginUser());
		mav.setViewName("task_show");
		//---特定のIDのTask情報を取得
		TaskBean bean = tasksService.selectBean(id);
		mav.addObject("editFlag", true);
		mav.addObject("formModel", bean);
		return mav;
	}

	//--Task編集画面(編集可能)
	@RequestMapping(value="/tasks/show/{id}", params = "edit",method=RequestMethod.POST)
	public ModelAndView tasks_edit(@RequestParam("id") int id,ModelAndView mav) {

		boolean editMode=true;//---編集モードをONにする。
		mav.addObject("editFlag", false);//---編集ボタンフラグをOFFにする。

		mav.setViewName("task_show");
		mav.addObject("editMode",editMode);//---編集モードをセットする。

		//ユーザーを選択するため、ユーザーテーブルからユーザー一覧を取得しadd
		List<UserBean> listBean =userService.findAllBean();
		mav.addObject("listBean", listBean);

		TaskBean bean = tasksService.selectBean(id);//---編集するIDのtask情報をTaskBean化して取得。

		bean.setUpdate_user(sessionUser.getLoginUser().getName());//---ログインしているユーザー情報を更新ユーザー情報にセットする。
		bean.setUpdate_date(new Date(System.currentTimeMillis()));//---更新日時情報に現在日時をセットする。

		mav.addObject("formModel", bean);

		return mav;
	}

	//---Task更新処理Update
	@RequestMapping(value="/tasks/update",params="update",method=RequestMethod.POST)
	public ModelAndView tasks_update(@ModelAttribute("formModel") @Validated TaskBean taskBean,
										BindingResult result,ModelAndView mav) {
		ModelAndView res = null;
		if(!result.hasErrors()) {
			try {
				tasksService.update(tasksService.changeTask(taskBean));
			}catch(OptimisticLockingFailureException e) {
				session.setAttribute("flush", "Updateしたレコードのバージョンが古いため、更新できませんでした。");
				res = new ModelAndView("redirect:/tasks");
				return res;
			}
			session.setAttribute("flush", "タスクの更新が完了しました。");
			res = new ModelAndView("redirect:/tasks");
		}else {
			mav.setViewName("task_show");
			//ユーザーを選択するため、ユーザーテーブルからユーザー一覧を取得しadd
			List<UserBean> listBean =userService.findAllBean();
			mav.addObject("listBean", listBean);
			mav.addObject("editFlag", false);//---編集ボタンをOFFに設定
			mav.addObject("editMode", true);//---編集モードをONに設定
			mav.addObject("formModel", taskBean);
			res = mav;
		}
		return res;
	}

	//---Task完了処理Update_complete
	@RequestMapping(value="/tasks/update",params="complete",method=RequestMethod.POST)
	public ModelAndView tasks_update_complete(@RequestParam("id")int id,ModelAndView mav) {
		ModelAndView res = null;
		TaskBean bean = tasksService.selectBean(id);//---完了するIDのtask情報をTaskBean化して取得。
		bean.setCompletion_date(new Date(System.currentTimeMillis()));//---完了日時情報に現在日時をセットする。
		tasksService.complateButtonUpdate(tasksService.changeTask(bean));
		session.setAttribute("flush", "タスクが完了しました。");
		res = new ModelAndView("redirect:/tasks");
		return res;
	}

	//---ToDo 2020/5/31 "未完了"画像クリック時にUpdate出来るようにする。
	//---Task完了処理Update_index_complete
	@RequestMapping(value="/tasks/hello",method=RequestMethod.POST)
	public ModelAndView tasks_index_complete(@RequestParam("id")int id,ModelAndView mav) {
		ModelAndView res = null;
		mav.setViewName("hello");
		//TaskBean bean = tasksService.selectBean(id);//---完了するIDのtask情報をTaskBean化して取得。
		//bean.setCompletion_date(new Date(System.currentTimeMillis()));//---完了日時情報に現在日時をセットする。
		//tasksService.update(tasksService.changeTask(bean));
		session.setAttribute("flush", "タスクが完了しました。");
		//res = new ModelAndView("redirect:/tasks");
		res = mav;
		return res;
	}

}
