package com.sample.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.bean.TaskBean;
import com.sample.entity.Task;
import com.sample.mapper.TaskMapper;

@Service
public class TaskService {

	@Autowired
	private TaskMapper tasksMapper;


	// Entityの箱に入ったリスト一覧を取得
	public List<Task> findAll(){
		return tasksMapper.findAll();
	}


	// Entity型のリストをTaskBean型のリストに変換して戻す。
	public List<TaskBean> findAllBean(){
		List<Task> list = tasksMapper.findAll(); //マッパーでTask一覧取得
		List<TaskBean> listBean = new ArrayList<TaskBean>(); //TaskBean型のList、listBean変数を宣言。

		for(int i = 0; i < list.size(); i++) {  //Entity型をTaskBean型のリストに入れ替え処理
			TaskBean bean = new TaskBean(list.get(i));
			listBean.add(bean);
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
	public void insert(TaskBean task) {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		DateFormat df2 = new SimpleDateFormat("yyyy/MM/dd");
		tasksMapper.insert(task.getId(),task.getUser(), task.getTitle(), task.getDetail(), df.format(task.getDue_date()),
				task.getImportance(), df2.format(task.getCompletion_date()), task.getInsert_user(), df2.format(task.getInsert_date()),
				task.getUpdate_user(),df2.format(task.getUpdate_date()));

	}
}
