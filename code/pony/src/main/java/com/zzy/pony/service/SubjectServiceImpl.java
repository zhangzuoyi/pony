package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.ExamDao;
import com.zzy.pony.dao.ExamSubjectDao;
import com.zzy.pony.dao.SubjectDao;
import com.zzy.pony.mapper.TeacherSubjectMapper;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.ExamSubject;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.TeacherSubjectVo;
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
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	@Autowired
	private TeacherSubjectMapper teacherSubjectMapper;
	
	

	@Override
	public void add(Subject sy) {
		dao.save(sy);

	}

	@Override
	public List<Subject> findAll() {
		
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.ASC, "importance"));
		orders.add(new Order(Direction.ASC, "subjectId"));
		Sort sort = new Sort(orders);
		return dao.findAll(sort);
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
		old.setImportance(sy.getImportance());
		
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
		List<Subject> list = dao.findByName(name);
		if(list.size()>0) {
			return list.get(0);
		}
		return null;
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
				result.add(examSubject.getSubject());				
		}
		}
		//重写hashCode和equals方法去重subject
		Set<Subject> subjects = new HashSet<Subject>();
		subjects.addAll(result);
		
		List<Subject> subjectList = new ArrayList<Subject>();
		subjectList.addAll(subjects);
		
		return subjectList;
	}

	@Override
	public List<Subject> findSubjects(int[] subjectIds) {
		// TODO Auto-generated method stub
		List<Subject> result = new ArrayList<Subject>();
		for (int subjectId : subjectIds) {
			Subject subject = dao.findOne(subjectId);
			result.add(subject);
		}
		
		
		return result;
	}

	@Override
	public Subject findByTeacherAndGrade(int teacherId,int gradeId) {
		// TODO Auto-generated method stub
		SchoolYear year = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		List<TeacherSubjectVo> teacherSubjectVos = teacherSubjectMapper.findByGradeAndTeacher(year.getYearId(), term.getTermId(), gradeId, teacherId);
		if (teacherSubjectVos!= null && teacherSubjectVos.size()>0) {
			return dao.findOne(teacherSubjectVos.get(0).getSubjectId()); 
		}	
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
