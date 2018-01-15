package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.Student;
import com.zzy.pony.model.StudentStatusChange;
import com.zzy.pony.vo.StudentStatusChangeVo;

public interface StudentService {
	String STUDENT_TYPE_TZ="0";//统招
	String STUDENT_TYPE_ZR="1";//转入
	String STUDENT_TYPE_JR="2";//借入
	String STUDENT_STATUS_ZD="0";//在读
	String STUDENT_STATUS_BY="1";//毕业
	String STUDENT_STATUS_KC="2";//开除
	String STUDENT_STATUS_TX="3";//退学
	String STUDENT_STATUS_CX="4";//辍学
	String STUDENT_STATUS_YY="5";//肄业
	String STUDENT_STATUS_ZC="6";//转出
	String STUDENT_STATUS_JC="7";//借出
	String STUDENT_STATUS_XX="8";//休学
	String STUDENT_STATUS_SW="9";//死亡
	String STUDENT_STATUS_BCP="10";//不参评，即不参加考试
	void add(Student sy);
	List<Student> findAll();
	Student get(int id);
	void update(Student sy);
	void delete(int id);
	List<Student> findBySchoolClass(Integer classId);
	List<Student> findBySchoolClassAndStatus(Integer classId,String status);
	void upload(List<Student> students, String loginUser);
	void changeStatus(StudentStatusChange sc);
	/**
	 * 同时改变多个学生的状态
	 * @param sc
	 */
	void changeStatus(StudentStatusChangeVo sc);
	List<Student> findByGradeOrderByStudentId(int gradeId,int yearId);
	/**
	 * 设置学生的考试科目
	 * @param subjects 科目名称
	 * @param studentIds
	 */
	void setStudentSubjects(String[] subjects,Integer[] studentIds);
	List<Student> findByGrade(Integer gradeId);
}
