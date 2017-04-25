package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.TeacherSubjectVo;
import com.zzy.pony.vo.conditionVo;

public interface TeacherSubjectService {
	void add(TeacherSubject sy);
	List<TeacherSubject> findAll();
	TeacherSubject get(int id);
	void update(TeacherSubject sy);
	void delete(int id);
	/**
	 * 当前学期老师的任教列表
	 * @param teacher
	 * @return
	 */
	List<TeacherSubject> findCurrentByTeacher(Teacher teacher);
	List<TeacherSubjectVo> findCurrentVoByTeacher(Teacher teacher);
	
	List<TeacherSubjectVo> findCurrentVoBySchoolClass(SchoolClass schoolClass);
	
	List<TeacherSubjectVo> findCurrentVoByTeacherAndSubject(int teacherId,int subjectId);
	
	List<TeacherSubjectVo> findCurrentVoByCondition(conditionVo cv);


	
	List<TeacherSubjectVo> findCurrentAll();
	
	List<Integer> findCurrentAllTeacherId();
	List<Integer> findCurrentAllClassId();
	List<Integer> findCurrentAllSubjectId();
	Boolean isExists(Teacher teacher,SchoolYear schoolYear,Term term,SchoolClass schoolClass,Subject subject);

	
}
