package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.LessonArrangeVo;

public interface LessonArrangeService {
	void add(LessonArrange sy);
	List<LessonArrange> findAll();
	LessonArrange get(int id);
	void update(LessonArrange sy);
	void delete(int id);
	LessonArrangeVo findArrangeVo(Integer classId);
	List<LessonArrange> findByClassIdAndSchoolYearAndTerm(Integer classId, SchoolYear year, Term term);
	LessonArrange findByClassIdAndSchoolYearAndTermAndWeekDayAndLessonPeriod(Integer classId, SchoolYear year, Term term,String weekDay,LessonPeriod lessonPeriod);


}
