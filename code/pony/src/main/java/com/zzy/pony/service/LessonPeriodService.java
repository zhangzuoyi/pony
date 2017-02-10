package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.LessonPeriod;

public interface LessonPeriodService {
	void add(LessonPeriod sy);
	List<LessonPeriod> findAll();
	LessonPeriod get(int id);
	void update(LessonPeriod sy);
	void delete(int id);
}
