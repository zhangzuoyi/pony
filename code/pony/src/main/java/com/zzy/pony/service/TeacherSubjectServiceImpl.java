package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.Result;
import com.zzy.pony.dao.LessonArrangeDao;
import com.zzy.pony.dao.TeacherSubjectDao;
import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.TeacherSubjectVo;
@Service
@Transactional
public class TeacherSubjectServiceImpl implements TeacherSubjectService {
	@Autowired
	private TeacherSubjectDao dao;
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private TermService termService;
	@Autowired
	private LessonArrangeDao arrangeDao;

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

	@Override
	public List<TeacherSubject> findCurrentByTeacher(Teacher teacher) {
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		
		return dao.findByTeacherAndYearAndTerm(teacher, year, term);
	}

	@Override
	public List<TeacherSubjectVo> findCurrentVoByTeacher(Teacher teacher) {
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		List<TeacherSubject> list=dao.findByTeacherAndYearAndTerm(teacher, year, term);
		List<TeacherSubjectVo> result=new ArrayList<TeacherSubjectVo>();
		for(TeacherSubject ts:list){
			List<LessonArrange> arranges=arrangeDao.findByClassIdAndSchoolYearAndTermAndSubject(ts.getSchoolClass().getClassId(), year, term, ts.getSubject());
			TeacherSubjectVo vo=TeacherSubjectVo.fromModel(ts);
			vo.setArranges(arranges);
			result.add(vo);
		}
		return result;
	}

	@Override
	public List<TeacherSubjectVo> findCurrentVoBySchoolClass(
			SchoolClass schoolClass) {
		// TODO Auto-generated method stub
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		List<TeacherSubject> list = dao.findBySchoolClassAndYearAndTerm(schoolClass, year, term);
		List<TeacherSubjectVo> result = new ArrayList<TeacherSubjectVo>();
		for(TeacherSubject ts:list){
			List<LessonArrange> arranges=arrangeDao.findByClassIdAndSchoolYearAndTermAndSubject(ts.getSchoolClass().getClassId(), year, term, ts.getSubject());
			TeacherSubjectVo vo=TeacherSubjectVo.fromModel(ts);
			vo.setArranges(arranges);
			result.add(vo);
		}
		return result;
	}
	
	
	

	@Override
	public List<TeacherSubjectVo> findCurrentAll() {
		// TODO Auto-generated method stub
		List<TeacherSubjectVo> result = new ArrayList<TeacherSubjectVo>();
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		List<TeacherSubject> list = dao.findByYearAndTerm(year, term);
		for (TeacherSubject teacherSubject : list) {
			TeacherSubjectVo vo  = TeacherSubjectVo.fromModel(teacherSubject);
			result.add(vo);
		}
		
		return result;
	}

	@Override
	public List<Integer> findCurrentAllTeacherId() {
		// TODO Auto-generated method stub
		List<Integer> result =  new ArrayList<Integer>();
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		List<TeacherSubject> list = dao.findByYearAndTerm(year, term);		
		for (TeacherSubject teacherSubject : list) {
			Integer teacherId = teacherSubject.getTeacher().getTeacherId();
			if (!result.contains(teacherId)) {
				result.add(teacherId);
			}
		}
		
		
		return result;
	}

	@Override
	public List<Integer> findCurrentAllClassId() {
		// TODO Auto-generated method stub
		List<Integer> result =  new ArrayList<Integer>();
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		List<TeacherSubject> list = dao.findByYearAndTerm(year, term);
		for (TeacherSubject teacherSubject : list) {
			Integer classId = teacherSubject.getSchoolClass().getClassId();
			if (!result.contains(classId)) {
				result.add(classId);
			}
		}
		
		
		return result;
	}

	@Override
	public List<Integer> findCurrentAllSubjectId() {
		// TODO Auto-generated method stub
		List<Integer> result =  new ArrayList<Integer>();
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		List<TeacherSubject> list = dao.findByYearAndTerm(year, term);
		for (TeacherSubject teacherSubject : list) {
			Integer subjectId = teacherSubject.getSubject().getSubjectId();
			if (!result.contains(subjectId)) {
				result.add(subjectId);
			}
		}	
		return result;
	}
	
	
	
	

	
	
	

}
