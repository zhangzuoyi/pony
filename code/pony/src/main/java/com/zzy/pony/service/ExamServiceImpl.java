package com.zzy.pony.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.ExamDao;
import com.zzy.pony.dao.SchoolClassDao;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Term;
@Service
@Transactional
public class ExamServiceImpl implements ExamService {
	@Autowired
	private ExamDao dao;
	@Autowired
	private SchoolClassDao classDao;
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private TermService termService;

	@Override
	public void add(Exam sy) {
		dao.save(sy);

	}

	@Override
	public List<Exam> findAll() {
		List<Exam> list=dao.findAll();
		for(Exam exam: list){
			exam.getSchoolClasses().size();
		}
		return list;
	}

	@Override
	public Exam get(int id) {
		Exam exam=dao.findOne(id);
		exam.getSchoolClasses().size();
		return exam;
	}

	@Override
	public void update(Exam sy, List<Integer> classIds) {
		Exam old=dao.findOne(sy.getExamId());
		old.setName(sy.getName());
		old.getSchoolClasses().clear();
		old.getSchoolClasses().addAll(classDao.findByClassIdIn(classIds));
		old.setSubject(sy.getSubject());
		old.setUpdateTime(sy.getUpdateTime());
		old.setUpdateUser(sy.getUpdateUser());
		
		dao.save(old);
	}

	@Override
	public void delete(int id) {
		dao.delete(id);
		
	}

	@Override
	public List<Exam> findBySubject(Subject subject) {
		return dao.findBySubject(subject);
	}

	@Override
	public List<Exam> findCurrentBySubjectAndClass(Subject subject, SchoolClass schoolClass) {
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		
		return dao.findBySchoolYearAndTermAndSubjectAndSchoolClasses(year, term, subject, schoolClass);
	}

}
