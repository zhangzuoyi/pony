package com.zzy.pony.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.StudentRemarkDao;
import com.zzy.pony.model.Student;
import com.zzy.pony.model.StudentRemark;

@Service
public class StudentRemarkServiceImpl implements StudentRemarkService {
	@Autowired
	private StudentRemarkDao dao;

	@Override
	public List<StudentRemark> findByStudent(Integer studentId) {
		Student student = new Student();
		student.setStudentId(studentId);
		return dao.findByStudent(student);
	}

	@Override
	public void add(StudentRemark remark) {
		dao.save(remark);

	}

	@Override
	public void delete(int id) {
		dao.delete(id);

	}

	@Override
	public void update(StudentRemark remark) {
		StudentRemark old=dao.findOne(remark.getId());
		old.setRemarkLevel(remark.getRemarkLevel());
		old.setRemark(remark.getRemark());
		
		dao.save(old);
	}

}