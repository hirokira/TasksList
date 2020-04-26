package com.sample.entity;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Task {

	private int id;

	private String user;

	private String title;

	private String detail;

	private String due_date;

	private int importance;

	private String completion_date;

	private String update_user;

	private String update_date;

	private String insert_user;

	private String insert_date;

}
