package com.sample.bean;

import java.sql.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

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
		this.importance=(short) task.getImportance();
		if(!task.getCompletion_date().equals("")) {
			this.completion_date=Date.valueOf(task.getCompletion_date());  //String型→sql.Date型に変換してセット。
		}else {
			this.completion_date=null;
		}
		this.update_user=task.getUpdate_user();
		this.update_date=Date.valueOf(task.getUpdate_date());  //String型→sql.Date型に変換してセット。
		this.insert_user=task.getInsert_user();
		this.insert_date=Date.valueOf(task.getInsert_date());  //String型→sql.Date型に変換してセット。
	}

	@NotNull(message="IDを入力してください。")
	private int id;

	@NotNull
	@NotEmpty(message="ユーザーを選択してください。")
	private String user;

	@Length(max=150)
	private String title;

	@Length(max=10000)
	private String detail;

	@NotNull(message="期限を決定してください")
	private Date due_date;


	private short importance;

	private Date completion_date;

	private String update_user;

	private Date update_date;

	private String insert_user;

	private Date insert_date;

	private String remnant_date;


}
