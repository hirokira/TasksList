package com.sample.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.bean.TaskBean;
import com.sample.component.TimeFilterLogic;
import com.sample.entity.Task;
import com.sample.mapper.TaskMapper;

@Service
public class TaskService {

	@Autowired
	private TaskMapper tasksMapper;

	@Autowired
	private TimeFilterLogic timeFilter;


	// Entityの箱に入ったリスト一覧を取得
	public List<Task> findAll(){
		return tasksMapper.findAll();
	}


	// Entity型のリストをTaskBean型のリストに変換して戻す。
	public List<TaskBean> findAllBean(){
		List<Task> list = tasksMapper.findAll(); //マッパーでTask一覧取得
		List<TaskBean> listBean = new ArrayList<>(); //TaskBean型のList、listBean変数を宣言。

		if(list!=null) {
			for(int i = 0; i < list.size(); i++) {  //Entity型のリストをTaskBean型のリストに入れ替え処理
				listBean.add(new TaskBean(list.get(i)));
				/*---Add 2020/05/31 completion_dateがnull以外の際はRemnant_dateを"完了済"
				                     nullの際はRemant_dateに残日数算出の処理(timeFilter.remnantTimeTaskメソッドを通す。*/
				if((!list.get(i).getCompletion_date().equals(""))) {
					listBean.get(i).setRemnant_date("完了済");
				}else {
					listBean.get(i).setRemnant_date(timeFilter.remnantTimeTask(list.get(i)));
				}
			}
		}
		return listBean;
	}


	// 選んだIDのレコードを取得
	public Task select(int id) {

		return tasksMapper.select(id);
	}

	// 選んだIDのレコードを取得
	public TaskBean selectBean(int id) {
		TaskBean bean = new TaskBean(tasksMapper.select(id));
		return bean;
	}

	// 新規TaskINSERT
	public void insert(Task task) {
		tasksMapper.insert(task);
	}

	// Task更新(UPDATE)
	public void update(Task task) {
		tasksMapper.update(task);
	}

	//---ID登録済みチェック
	public boolean checkIdResult(int id) {
		if(tasksMapper.checkIdResult(id)==null) {
			return false;
		}else {
			return true;
		}
	}

	//---選択したNAMEと条件(期限までの日数)にあった一覧を抽出。(Nameが空白("")だった場合はnullを返す。
	public List<TaskBean> selectDateBean(String name ,boolean check1,boolean check2,boolean check3,boolean check4) {
		List<Task> list = new ArrayList<>();
		List<Task> lists = new ArrayList<>();
		List<TaskBean> listBean = new ArrayList<>();

		if(!name.equals("")) {  //---nameが空白("")以外のみ、以下の処理を行う。
			list = tasksMapper.findByUser(name);  //---選択したUserのユーザー情報一覧抽出。
			lists = timeFilter.timeFilter(list, check1, check2,check3,check4);//---check1,check2のFlagに法って、有効期限までの日数が該当するものを抽出。
			if(lists!=null) {
				for(int i = 0;i<lists.size(); i++) {
					listBean.add(new TaskBean(lists.get(i)));
					listBean.get(i).setRemnant_date(timeFilter.remnantTimeTask(lists.get(i)));
				}
			}
		}else {
			listBean = null;
		}
		return listBean;
	}

	//---TaskBean　→　Taskに変換ロジック
	public Task changeTask(TaskBean bean) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		Task task = new Task();
		task.setId(bean.getId());
		task.setUser(bean.getUser());
		task.setTitle(bean.getTitle());
		task.setDetail(bean.getDetail());
		task.setDue_date(df.format(bean.getDue_date()));
		task.setImportance(bean.getImportance());
		if(bean.getCompletion_date()!=null) {
			task.setCompletion_date(df.format(bean.getCompletion_date()));
		}else {
			task.setCompletion_date("");
		}
		task.setUpdate_user(bean.getUpdate_user());
		task.setUpdate_date(df.format(bean.getUpdate_date()));
		task.setInsert_user(bean.getInsert_user());
		task.setInsert_date(df.format(bean.getInsert_date()));
		return task;
	}
}
