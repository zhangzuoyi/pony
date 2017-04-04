package com.zzy.pony.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.StudentStatusChange;

public interface StudentStatusChangeDao extends JpaRepository<StudentStatusChange, Long> {
	List<StudentStatusChange> findByStudentId(Integer studentId);
}