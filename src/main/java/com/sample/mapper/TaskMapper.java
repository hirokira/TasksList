package com.sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.sample.entity.Task;

@Mapper
@Component
public interface TaskMapper {

	List<Task> findAll();

	Task select(int id);

	void insert(@Param("id")int id,@Param("user")int user,@Param("title")String title,@Param("detail")String detail,@Param("due_date")String due_date,
			@Param("importance")int importance,@Param("completion_date")String completion_date,
			@Param("insert_user")int insert_user,@Param("insert_date")String insert_date,
			@Param("update_user")int update_user,@Param("update_date")String update_date);

}
