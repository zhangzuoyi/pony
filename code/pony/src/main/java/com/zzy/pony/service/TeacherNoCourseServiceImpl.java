package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;





import com.zzy.pony.dao.TeacherNoCourseDao;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.TeacherNoCourse;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.TeacherNoCourseVo;

@Service
@Transactional
public class TeacherNoCourseServiceImpl implements TeacherNoCourseService {
	@Autowired
	private TeacherNoCourseDao teacherNoCourseDao; 
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	
	
	@Override
	public List<TeacherNoCourse> findAll() {
		// TODO Auto-generated method stub
		return teacherNoCourseDao.findAll();
	}


	@Override
	public List<TeacherNoCourseVo> findAllVo() {
		// TODO Auto-generated method stub
		List<TeacherNoCourseVo> result = new ArrayList<TeacherNoCourseVo>();
		List<TeacherNoCourse> list = teacherNoCourseDao.findAll();
		for (TeacherNoCourse teacherNoCourse : list) {
			TeacherNoCourseVo tncv = toTeacherNoCourseVo(teacherNoCourse);
			result.add(tncv);
		}
		return result;
	}
	
	
	
	@Override
	public List<TeacherNoCourseVo> findCurrentAllVo() {
		// TODO Auto-generated method stub
		List<TeacherNoCourseVo> result = new ArrayList<TeacherNoCourseVo>();
		SchoolYear schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		List<TeacherNoCourse> list = teacherNoCourseDao.findBySchoolYearAndTerm(schoolYear, term);
		for (TeacherNoCourse teacherNoCourse : list) {
			TeacherNoCourseVo tncv = toTeacherNoCourseVo(teacherNoCourse);
			result.add(tncv);
		}
		return result;	
		
	}


	private TeacherNoCourseVo toTeacherNoCourseVo(TeacherNoCourse tnc){
		TeacherNoCourseVo tncv = new TeacherNoCourseVo();
		tncv.setId(tnc.getId());
		tncv.setLessonPeriodId(tnc.getLessonPeriod().getPeriodId());
		tncv.setLessonPeriodName(tnc.getLessonPeriod().getStartTime()+"--"+tnc.getLessonPeriod().getEndTime());
		tncv.setLessonPeriodSeq(tnc.getLessonPeriod().getSeq());
		tncv.setTeacherId(tnc.getTeacher().getTeacherId());
		tncv.setTeacherName(tnc.getTeacher().getName());
		tncv.setTermId(tnc.getTerm().getTermId());
		tncv.setTermName(tnc.getTerm().getName());
		tncv.setWeekdayId(tnc.getWeekday().getSeq());
		tncv.setWeekdayName(tnc.getWeekday().getName());
		tncv.setYearId(tnc.getSchoolYear().getYearId());
		tncv.setYearName(tnc.getSchoolYear().getName());		
		return tncv;	
	}


	@Override
	public void deleteByTeacherAndYearAndTerm(Teacher teacher,
			SchoolYear schoolYear, Term term) {
		// TODO Auto-generated method stub
		
		List<TeacherNoCourse> teacherNoCourses = teacherNoCourseDao.findByTeacherAndSchoolYearAndTerm(teacher, schoolYear, term);
		teacherNoCourseDao.delete(teacherNoCourses);
		
	}


	@Override
	public void save(TeacherNoCourse teacherNoCourse) {
		// TODO Auto-generated method stub
		teacherNoCourseDao.save(teacherNoCourse);
	}
	
	
	
	
	
	
	
	
	
	
}
