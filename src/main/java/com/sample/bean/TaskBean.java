package com.sample.bean;

import java.sql.Date;

import com.sample.entity.Task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskBean {


	public TaskBean() {

	}

	//コンストラクタを設定
	public TaskBean(Task task) {

		this.id=task.getId();
		this.user=task.getUser();
		this.title=task.getTitle();
		this.detail=task.getDetail();
		this.due_date=Date.valueOf(task.getDue_date());  //String型→sql.Date型に変換してセット。
		this.importance=task.getImportance();
		this.completion_date=Date.valueOf(task.getCompletion_date());  //String型→sql.Date型に変換してセット。
		this.update_user=task.getUpdate_user();
		this.update_date=Date.valueOf(task.getUpdate_date());  //String型→sql.Date型に変換してセット。
		this.insert_user=task.getInsert_user();
		this.insert_date=Date.valueOf(task.getInsert_date());  //String型→sql.Date型に変換してセット。
	}

	private int id;

	private String user;

	private String title;

	private String detail;

	private Date due_date;

	private int importance;

	private Date completion_date;

	private String update_user;

	private Date update_date;

	private String insert_user;

	private Date insert_date;



}
