package com.zzy.pony.mapper;

import java.util.List;

import com.zzy.pony.model.Student;

public interface StudentMapper {
	List<Student> findByGradeOrderByStudentId(int  gradeId,int yearId);
	void insertStudentClassRelation(int studentId, int classId);
	/**
	 * 修改整个班学生的状态，可以用于毕业
	 * @param status
	 * @param classId
	 */
	void updateStatusByClass(String tostatus, String fromstatus, int classId);
	void updateClassByClass(int targetClassId, int srcClassId);
	void setExamSubjectsByStudentNo(String examSubjects, String studentNo);
}
