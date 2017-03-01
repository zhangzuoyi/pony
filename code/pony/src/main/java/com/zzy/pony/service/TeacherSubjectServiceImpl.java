package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.LessonArrangeDao;
import com.zzy.pony.dao.TeacherSubjectDao;
import com.zzy.pony.model.LessonArrange;
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

}
