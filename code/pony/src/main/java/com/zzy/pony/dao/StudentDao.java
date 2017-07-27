package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.Student;


public interface StudentDao extends JpaRepository<Student,Integer>{
	List<Student> findBySchoolClass(SchoolClass sc);
	Student findByStudentNo(String studentNo);
	
}
