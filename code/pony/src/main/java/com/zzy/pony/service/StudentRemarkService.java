package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.StudentRemark;

public interface StudentRemarkService {
	List<StudentRemark> findByStudent(Integer studentId);

	void add(StudentRemark remark);

	void delete(int id);
	void update(StudentRemark remark);
}