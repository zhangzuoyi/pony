package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zzy.pony.model.Teacher;


public interface TeacherDao extends JpaRepository<Teacher,Integer>{
	List<Teacher> findByTeacherNo(String teacherNo);
	List<Teacher> findAllByOrderByTeacherNo();
	@Query("select teacherId from Teacher where teacherNo=?1")
	Integer findIdByTeacherNo(String teacherNo);
}
