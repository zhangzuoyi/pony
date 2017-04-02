package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;









import com.zzy.pony.dao.SubjectNoCourseDao;
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.SubjectNoCourse;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.SubjectNoCourseVo;

@Service
@Transactional
public class SubjectNoCourseServiceImpl implements SubjectNoCourseService {
	@Autowired
	private SubjectNoCourseDao subjectNoCourseDao;
	
	
	@Override
	public List<SubjectNoCourse> findAll() {
		// TODO Auto-generated method stub
		return subjectNoCourseDao.findAll();
	}


	@Override
	public List<SubjectNoCourseVo> findAllVo() {
		// TODO Auto-generated method stub
		List<SubjectNoCourseVo> result = new ArrayList<SubjectNoCourseVo>();
		List<SubjectNoCourse> list = subjectNoCourseDao.findAll();
		for (SubjectNoCourse subjectNoCourse : list) {
			SubjectNoCourseVo tncv = toSubjectNoCourseVo(subjectNoCourse);
			result.add(tncv);
		}
		return result;
	}
	
	private SubjectNoCourseVo toSubjectNoCourseVo(SubjectNoCourse snc){
		SubjectNoCourseVo sncv = new SubjectNoCourseVo();
		sncv.setId(snc.getId());
		sncv.setLessonPeriodId(snc.getLessonPeriod().getPeriodId());
		sncv.setLessonPeriodName(snc.getLessonPeriod().getStartTime()+"——"+snc.getLessonPeriod().getEndTime());
		sncv.setGradeId(snc.getGrade().getGradeId());
		sncv.setGradeName(snc.getGrade().getName());
		sncv.setSubjectId(snc.getSubject().getSubjectId());
		sncv.setSubjectName(snc.getSubject().getName());		
		sncv.setTermId(snc.getTerm().getTermId());
		sncv.setTermName(snc.getTerm().getName());
		sncv.setWeekdayId(snc.getWeekday().getSeq());
		sncv.setWeekdayName(snc.getWeekday().getName());
		sncv.setYearId(snc.getSchoolYear().getYearId());
		sncv.setYearName(snc.getSchoolYear().getName());
		return sncv;	
	}
	
	@Override
	public void deleteByGradeAndSubjectAndYearAndTerm(Grade grade,Subject subject,
			SchoolYear schoolYear, Term term) {
		// TODO Auto-generated method stub
		
		List<SubjectNoCourse> subjectNoCourses = subjectNoCourseDao.findByGradeAndSubjectAndSchoolYearAndTerm(grade, subject, schoolYear, term);
		subjectNoCourseDao.delete(subjectNoCourses);
		
	}


	@Override
	public void save(SubjectNoCourse subjectNoCourse) {
		// TODO Auto-generated method stub
		subjectNoCourseDao.save(subjectNoCourse);
	}
	
	
	
	
	
}
