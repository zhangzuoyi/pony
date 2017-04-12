package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;










import com.zzy.pony.dao.GradeNoCourseDao;
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.GradeNoCourse;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.GradeNoCourseVo;

@Service
@Transactional
public class GradeNoCourseServiceImpl implements GradeNoCourseService {
	@Autowired
	private GradeNoCourseDao gradeNoCourseDao; 
	@Autowired
	private SchoolClassService schoolClassService;
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	
	
	@Override
	public List<GradeNoCourse> findAll() {
		// TODO Auto-generated method stub
		return gradeNoCourseDao.findAll();
	}


	@Override
	public List<GradeNoCourseVo> findAllVo() {
		// TODO Auto-generated method stub
		List<GradeNoCourseVo> result = new ArrayList<GradeNoCourseVo>();
		List<GradeNoCourse> list = gradeNoCourseDao.findAll();
		for (GradeNoCourse gradeNoCourse : list) {
			GradeNoCourseVo gncv = toGradeNoCourseVo(gradeNoCourse);
			List<SchoolClass> schoolClasses = schoolClassService.findByGrade(gradeNoCourse.getGrade().getGradeId());
			List<Integer> gradeClassIds=new ArrayList<Integer>();
			for (SchoolClass schoolClass : schoolClasses) {
				gradeClassIds.add(schoolClass.getClassId());
			}
			gncv.setGradeClassIds(gradeClassIds);

			result.add(gncv);
		}
		return result;
	}
	
	
	
	@Override
	public List<GradeNoCourseVo> findCurrentAllVo() {
		// TODO Auto-generated method stub
		List<GradeNoCourseVo> result = new ArrayList<GradeNoCourseVo>();
		SchoolYear schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();	
		List<GradeNoCourse> list = gradeNoCourseDao.findBySchoolYearAndTerm(schoolYear, term);
		for (GradeNoCourse gradeNoCourse : list) {
			GradeNoCourseVo gncv = toGradeNoCourseVo(gradeNoCourse);
			List<SchoolClass> schoolClasses = schoolClassService.findByGrade(gradeNoCourse.getGrade().getGradeId());
			List<Integer> gradeClassIds=new ArrayList<Integer>();
			for (SchoolClass schoolClass : schoolClasses) {
				gradeClassIds.add(schoolClass.getClassId());
			}
			gncv.setGradeClassIds(gradeClassIds);

			result.add(gncv);
		}
		return result;
	}


	private GradeNoCourseVo toGradeNoCourseVo(GradeNoCourse tnc){
		GradeNoCourseVo tncv = new GradeNoCourseVo();
		tncv.setId(tnc.getId());
		tncv.setLessonPeriodId(tnc.getLessonPeriod().getPeriodId());
		tncv.setLessonPeriodName(tnc.getLessonPeriod().getStartTime()+"——"+tnc.getLessonPeriod().getEndTime());
		tncv.setGradeId(tnc.getGrade().getGradeId());
		tncv.setGradeName(tnc.getGrade().getName());
		tncv.setTermId(tnc.getTerm().getTermId());
		tncv.setTermName(tnc.getTerm().getName());
		tncv.setWeekdayId(tnc.getWeekday().getSeq());
		tncv.setWeekdayName(tnc.getWeekday().getName());
		tncv.setYearId(tnc.getSchoolYear().getYearId());
		tncv.setYearName(tnc.getSchoolYear().getName());		
		return tncv;	
	}


	@Override
	public void deleteByGradeAndYearAndTerm(Grade grade,
			SchoolYear schoolYear, Term term) {
		// TODO Auto-generated method stub
		
		List<GradeNoCourse> gradeNoCourses = gradeNoCourseDao.findByGradeAndSchoolYearAndTerm(grade, schoolYear, term);
		gradeNoCourseDao.delete(gradeNoCourses);
		
	}


	@Override
	public void save(GradeNoCourse gradeNoCourse) {
		// TODO Auto-generated method stub
		gradeNoCourseDao.save(gradeNoCourse);
	}
	
	
	
	
	
	
	
	
	
	
}
