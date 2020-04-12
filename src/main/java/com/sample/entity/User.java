package com.sample.entity;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {


	@Length(max=10)
	private Integer id;

	@Length(max=100)
	private String name;

	@Length(max=100)
	private String password;

	@Length(max=16)
	private String nickName;

	private String active_from;

	private String active_to;

	private String update_user;

	private String update_date;

	private String insert_user;

	private String insert_date;

}
