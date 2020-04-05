package com.sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.sample.entity.Task;

@Mapper
@Component
public interface TaskMapper {

	List<Task> findAll();

	Task select(int id);

}
