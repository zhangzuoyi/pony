package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.Weekday;

public interface WeekdayService {
	List<Weekday> findByhaveClass(Integer haveClass);
	Weekday findByName(String name);
	
	
}
