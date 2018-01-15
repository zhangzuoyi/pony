package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.Student;


public interface StudentDao extends JpaRepository<Student,Integer>{
	List<Student> findBySchoolClass(SchoolClass sc);
	List<Student> findBySchoolClassAndStatus(SchoolClass sc,String status);
	Student findByStudentNo(String studentNo);
	@Query("select t from Student t where t.schoolClass in (:classes) order by t.schoolClass")
	List<Student> findBySchoolClasses(@Param(value = "classes")List<SchoolClass> classes);
}
