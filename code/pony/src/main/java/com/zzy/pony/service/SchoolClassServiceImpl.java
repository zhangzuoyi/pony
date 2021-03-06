package com.zzy.pony.service;

import java.util.List;

import javax.transaction.Transactional;

import com.zzy.pony.model.*;
import org.jfree.data.time.Year;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.ExamDao;
import com.zzy.pony.dao.SchoolClassDao;
import com.zzy.pony.vo.ExamVo;
@Service
@Transactional
public class SchoolClassServiceImpl implements SchoolClassService {
	@Autowired
	private SchoolClassDao dao;
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private ExamDao examDao;
	@Autowired
	private ExamService examService;
	@Autowired
	private GradeService gradeService;


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
		SchoolYear year = yearService.getCurrent();
		grade.setGradeId(gradeId);
		return dao.findByYearIdAndGrade(year.getYearId(),grade);
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

	@Override
	public List<SchoolClass> findByExam(int examId) {
		// TODO Auto-generated method stub
		 ExamVo examVo =  examService.getVo(examId);
		 examVo.getSchoolClasses().size();
		 List<SchoolClass> schoolClasses = examVo.getSchoolClasses();
		return schoolClasses;
	}

	@Override
	public List<SchoolClass> findByYearAndGrade(int yearId, int gradeId) {
		Grade grade=new Grade();
		grade.setGradeId(gradeId);
		return dao.findByYearIdAndGrade(yearId, grade);
	}
	

	@Override
	public List<SchoolClass> findByYearAndGradeOrderBySeq(int yearId,
			int gradeId) {
		// TODO Auto-generated method stub
		Grade grade=new Grade();
		grade.setGradeId(gradeId);
		return dao.findByYearIdAndGradeOrderBySeq(yearId, grade);
	}

	@Override
	public List<SchoolClass> findCurrent() {
		SchoolYear year=yearService.getCurrent();
		return dao.findByYearId(year.getYearId());
	}

	@Override
	public SchoolClass findByYearAndGradeAndSeq(int yearId, int gradeId, int seq) {
		Grade grade = gradeService.get(gradeId);
		List<SchoolClass> schoolClasses = dao.findByYearIdAndGradeAndSeq(yearId,grade,seq);
		if (schoolClasses!=null&&!schoolClasses.isEmpty()){
			return schoolClasses.get(0);
		}
		return null;
	}
}
