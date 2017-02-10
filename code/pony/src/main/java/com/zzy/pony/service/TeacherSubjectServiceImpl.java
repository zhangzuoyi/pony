package com.zzy.pony.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.TeacherSubjectDao;
import com.zzy.pony.model.TeacherSubject;
@Service
@Transactional
public class TeacherSubjectServiceImpl implements TeacherSubjectService {
	@Autowired
	private TeacherSubjectDao dao;

	@Override
	public void add(TeacherSubject sy) {
		dao.save(sy);

	}

	@Override
	public List<TeacherSubject> findAll() {
		return dao.findAll();
	}

	@Override
	public TeacherSubject get(int id) {
		return dao.findOne(id);
	}

	@Override
	public void update(TeacherSubject sy) {
		TeacherSubject old=dao.findOne(sy.getTsId());
		old.setSchoolClass(sy.getSchoolClass());
		old.setSubject(sy.getSubject());
		old.setTeacher(sy.getTeacher());
		
		dao.save(old);
	}

	@Override
	public void delete(int id) {
		dao.delete(id);
		
	}

}
