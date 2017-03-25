package com.zzy.pony.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.SchoolClassDao;
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Teacher;
@Service
@Transactional
public class SchoolClassServiceImpl implements SchoolClassService {
	@Autowired
	private SchoolClassDao dao;
	@Autowired
	private SchoolYearService yearService;

	@Override
	public void add(SchoolClass sy) {
		dao.save(sy);

	}

	@Override
	public List<SchoolClass> findAll() {
		return dao.findAll();
	}

	@Override
	public SchoolClass get(int id) {
		return dao.findOne(id);
	}

	@Override
	public void update(SchoolClass sy) {
		SchoolClass old=dao.findOne(sy.getClassId());
		old.setGrade(sy.getGrade());
		old.setSeq(sy.getSeq());
		old.setTeacher(sy.getTeacher());
		old.setUpdateUser(sy.getUpdateUser());
		old.setUpdateTime(sy.getUpdateTime());
		
		dao.save(old);
	}

	@Override
	public void delete(int id) {
		dao.delete(id);
		
	}

	@Override
	public List<SchoolClass> findByGrade(int gradeId) {
		Grade grade=new Grade();
		grade.setGradeId(gradeId);
		return dao.findByGrade(grade);
	}

	@Override
	public List<SchoolClass> findByYearIdAndTeacher(Integer yearId, Teacher teacher) {
		return dao.findByYearIdAndTeacher(yearId, teacher);
	}

	@Override
	public List<SchoolClass> findCurrentByTeacher(Teacher teacher) {
		SchoolYear year=yearService.getCurrent();
		return dao.findByYearIdAndTeacher(year.getYearId(), teacher);
	}

}
