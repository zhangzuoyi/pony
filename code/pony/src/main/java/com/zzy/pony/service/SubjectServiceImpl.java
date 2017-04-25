package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.ExamDao;
import com.zzy.pony.dao.ExamSubjectDao;
import com.zzy.pony.dao.SubjectDao;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.ExamSubject;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.Term;
@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {
	@Autowired
	private SubjectDao dao;
	@Autowired 
	private ExamDao examDao;
	@Autowired
	private ExamSubjectDao examSubjectDao;
	@Autowired
	private SchoolClassService schoolClassService;
	
	

	@Override
	public void add(Subject sy) {
		dao.save(sy);

	}

	@Override
	public List<Subject> findAll() {
		return dao.findAll();
	}

	@Override
	public Subject get(int id) {
		return dao.findOne(id);
	}

	@Override
	public void update(Subject sy) {
		Subject old=dao.findOne(sy.getSubjectId());
		old.setName(sy.getName());
		old.setType(sy.getType());
		
		dao.save(old);
	}

	@Override
	public void delete(int id) {
		dao.delete(id);
		
	}

	@Override
	public List<Subject> findClassSubject() {
		List<Integer> types=new ArrayList<Integer>();
		types.add(0);
		types.add(1);
		
		return dao.findByTypeIn(types);
	}

	@Override
	public List<Subject> findSelectiveSubject() {
		return dao.findByType(2);
	}

	@Override
	public List<Subject> findMajorSubject() {
		// TODO Auto-generated method stub
		List<Integer> types=new ArrayList<Integer>();
		types.add(0);
		
		
		return dao.findByTypeIn(types);
	}

	@Override
	public List<Subject> findByExam(int examId) {
		// TODO Auto-generated method stub
		List<Subject> subjects = new ArrayList<Subject>();
		Exam exam = examDao.findOne(examId);
		List<ExamSubject> examSubjects =  examSubjectDao.findByExam(exam);
		for (ExamSubject examSubject : examSubjects) {
			subjects.add(examSubject.getSubject());
		}
		
		return subjects;
	}

	@Override
	public List<Integer> findAllSubjectId() {
		// TODO Auto-generated method stub
		List<Integer> result = new ArrayList<Integer>();
		List<Subject> list = dao.findAll();
		for (Subject subject : list) {
			
			result.add(subject.getSubjectId());	
		}
		
		return result;
	}

	@Override
	public Subject findByName(String name) {
		// TODO Auto-generated method stub
		List<Subject> list = dao.findByName(name);
		return list.get(0);
	}

	@Override
	public List<Subject> findByClass(int classId) {
		// TODO Auto-generated method stub
		SchoolClass schoolClass =  schoolClassService.get(classId);
		List<SchoolClass> schoolClasses = new ArrayList<SchoolClass>();
		schoolClasses.add(schoolClass);
		List<Exam> exams = examDao.findBySchoolClasses(schoolClasses);
		List<Subject> result = new ArrayList<Subject>();
		for (Exam exam : exams) {
			List<ExamSubject> examSubjects =  examSubjectDao.findByExam(exam);
			for (ExamSubject examSubject : examSubjects) {
				if (!result.contains(examSubject)) {
					result.add(examSubject.getSubject());
				}				
			}
		}
		
		return result;
	}
	
	
	
	
	
	
	
	
	
	

}
