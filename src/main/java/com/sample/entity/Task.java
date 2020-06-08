package com.sample.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Task {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

	private int version;

}
