package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;

public interface LessonPeriodService {
	void add(LessonPeriod sy);
	List<LessonPeriod> findAll();
	LessonPeriod get(int id);
	void update(LessonPeriod sy);
	void delete(int id);
	List<LessonPeriod> findBySchoolYearAndTerm(SchoolYear year, Term term);
	LessonPeriod findByStartTimeAndEndTime(String startTime,String endTime);
}
