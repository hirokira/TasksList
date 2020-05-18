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

	List<Task> findByUser(@Param("user")String user);

	void insert(Task task);

	void update(Task task);

	Task checkIdResult(@Param("id")Integer id);

}
