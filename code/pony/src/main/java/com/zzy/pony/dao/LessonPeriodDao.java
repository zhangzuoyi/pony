package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;


public interface LessonPeriodDao extends JpaRepository<LessonPeriod,Integer>{
	List<LessonPeriod> findBySchoolYearAndTermOrderBySeq(SchoolYear year, Term term);
	List<LessonPeriod> findByStartTimeAndEndTime(String startTime,String endTime);
}
