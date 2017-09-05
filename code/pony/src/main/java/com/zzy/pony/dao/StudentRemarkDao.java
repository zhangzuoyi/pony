package com.zzy.pony.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.Student;
import com.zzy.pony.model.StudentRemark;

public interface StudentRemarkDao extends JpaRepository<StudentRemark, Integer> {
	List<StudentRemark> findByStudent(Student student);
}