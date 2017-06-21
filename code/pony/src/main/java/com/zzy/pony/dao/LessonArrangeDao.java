package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Term;


public interface LessonArrangeDao extends JpaRepository<LessonArrange,Integer>{
	List<LessonArrange> findByClassIdAndSchoolYearAndTerm(Integer classId, SchoolYear year, Term term);
	List<LessonArrange> findByClassIdAndSchoolYearAndTermAndSubject(Integer classId, SchoolYear schoolYear, Term term, Subject subject);
	List<LessonArrange> findByClassIdAndSchoolYearAndTermAndSubjectAndSourceType(Integer classId, SchoolYear schoolYear, Term term, Subject subject,String sourceType);
	List<LessonArrange> findByClassIdAndSchoolYearAndTermAndWeekDayAndLessonPeriod(Integer classId, SchoolYear year, Term term, String weekDay,
			LessonPeriod lessonPeriod);
	List<LessonArrange> findBySchoolYearAndTermAndSourceType(SchoolYear year,Term term,String sourceType);
	List<LessonArrange> findByClassIdAndSubjectAndSchoolYearAndTermAndWeekDayAndLessonPeriod(int classId,Subject subject,SchoolYear schoolYear,Term term,String weekDay,LessonPeriod lessonPeriod);
	List<LessonArrange> findBySchoolYearAndTermAndClassIdAndSourceType(SchoolYear year,Term term,int classId,String sourceType);

}
