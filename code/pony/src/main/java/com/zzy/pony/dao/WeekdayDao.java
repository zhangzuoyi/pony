package com.zzy.pony.dao;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.Weekday;


public interface WeekdayDao extends JpaRepository<Weekday,Integer>{
	List<Weekday> findByhaveClass(Integer haveClass);
	List<Weekday> findByName(String name);

}
