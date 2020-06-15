package com.sample.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User {

	@Id
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
	//---2020/06/08 add 楽観ロック実装の為、バージョン追加。
	private int version;

}
