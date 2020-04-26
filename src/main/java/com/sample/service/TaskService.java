package com.sample.service;

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
	public void insert(Task task) {
		tasksMapper.insert(task);

	}
}
