package com.zzy.pony.ss.dao;







import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.Student;
import com.zzy.pony.ss.model.StudentSubjectSelect;
import com.zzy.pony.ss.model.SubjectSelectConfig;


public interface StudentSubjectSelectDao extends JpaRepository<StudentSubjectSelect,Integer>{
	List<StudentSubjectSelect> findBySubjectSelectConfigAndStudent(SubjectSelectConfig config, Student student);
	
	
}
