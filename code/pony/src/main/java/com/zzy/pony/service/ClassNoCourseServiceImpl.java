package com.zzy.pony.service;



import java.util.ArrayList;
import java.util.List;









import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;









import com.zzy.pony.dao.ClassNoCourseDao;
import com.zzy.pony.model.ClassNoCourse;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.ClassNoCourseVo;

@Service
@Transactional
public class ClassNoCourseServiceImpl implements ClassNoCourseService {
	@Autowired
	private ClassNoCourseDao classNoCourseDao;
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	@Autowired
	private SchoolClassService schoolClassService;
	
	
	
	@Override
	public List<ClassNoCourse> findAll() {
		// TODO Auto-generated method stub
		return classNoCourseDao.findAll();
	}

	@Override
	public List<ClassNoCourseVo> findAllVo() {
		// TODO Auto-generated method stub
	List<ClassNoCourseVo> result= new ArrayList<ClassNoCourseVo>();	
	List<ClassNoCourse> classNoCourses  = classNoCourseDao.findAll();
	for (ClassNoCourse classNoCourse : classNoCourses) {
		ClassNoCourseVo vo = new ClassNoCourseVo();
		vo.setClassId(classNoCourse.getSchoolClass().getClassId());
		vo.setClassName(classNoCourse.getSchoolClass().getName());
		vo.setId(classNoCourse.getId());
		vo.setLessonPeriodId(classNoCourse.getLessonPeriod().getPeriodId());
		vo.setLessonPeriodSeq(classNoCourse.getLessonPeriod().getSeq());
		vo.setLessonPeriodName(classNoCourse.getLessonPeriod().getStartTime()+"--"+classNoCourse.getLessonPeriod().getEndTime());
		vo.setSchoolYearId(classNoCourse.getSchoolYear().getYearId());
		vo.setSchoolYearName(classNoCourse.getSchoolYear().getName());
		vo.setTermId(classNoCourse.getTerm().getTermId());
		vo.setTermName(classNoCourse.getTerm().getName());
		vo.setWeekdayId(classNoCourse.getWeekday().getSeq());
		vo.setWeekdayName(classNoCourse.getWeekday().getName());


		result.add(vo);
	}
	
		return result;
	}
	
	

	@Override
	public List<ClassNoCourseVo> findCurrentAllVo() {
		// TODO Auto-generated method stub
		List<ClassNoCourseVo> result= new ArrayList<ClassNoCourseVo>();	
		SchoolYear schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		List<ClassNoCourse> classNoCourses  = classNoCourseDao.findBySchoolYearAndTerm(schoolYear, term);
		for (ClassNoCourse classNoCourse : classNoCourses) {
			ClassNoCourseVo vo = new ClassNoCourseVo();
			vo.setClassId(classNoCourse.getSchoolClass().getClassId());
			vo.setClassName(classNoCourse.getSchoolClass().getName());
			vo.setId(classNoCourse.getId());
			vo.setLessonPeriodId(classNoCourse.getLessonPeriod().getPeriodId());
			vo.setLessonPeriodName(classNoCourse.getLessonPeriod().getStartTime()+"--"+classNoCourse.getLessonPeriod().getEndTime());
			vo.setLessonPeriodSeq(classNoCourse.getLessonPeriod().getSeq());
			vo.setSchoolYearId(classNoCourse.getSchoolYear().getYearId());
			vo.setSchoolYearName(classNoCourse.getSchoolYear().getName());
			vo.setTermId(classNoCourse.getTerm().getTermId());
			vo.setTermName(classNoCourse.getTerm().getName());
			vo.setWeekdayId(classNoCourse.getWeekday().getSeq());
			vo.setWeekdayName(classNoCourse.getWeekday().getName());

			result.add(vo);
		}
		
			return result;	
			
	}
	
	

	@Override
	public List<ClassNoCourseVo> findVoByClass(int classId) {
		// TODO Auto-generated method stub
		List<ClassNoCourseVo> result = new ArrayList<ClassNoCourseVo>();
		SchoolYear schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		SchoolClass schoolClass = schoolClassService.get(classId);
		List<ClassNoCourse> list = classNoCourseDao.findBySchoolClassAndSchoolYearAndTerm(schoolClass, schoolYear, term);
		for (ClassNoCourse classNoCourse : list) {
			ClassNoCourseVo vo = new ClassNoCourseVo();
			vo.setClassId(classNoCourse.getSchoolClass().getClassId());
			vo.setClassName(classNoCourse.getSchoolClass().getName());
			vo.setId(classNoCourse.getId());
			vo.setLessonPeriodId(classNoCourse.getLessonPeriod().getPeriodId());
			vo.setLessonPeriodName(classNoCourse.getLessonPeriod().getStartTime()+"--"+classNoCourse.getLessonPeriod().getEndTime());
			vo.setLessonPeriodSeq(classNoCourse.getLessonPeriod().getSeq());
			vo.setSchoolYearId(classNoCourse.getSchoolYear().getYearId());
			vo.setSchoolYearName(classNoCourse.getSchoolYear().getName());
			vo.setTermId(classNoCourse.getTerm().getTermId());
			vo.setTermName(classNoCourse.getTerm().getName());
			vo.setWeekdayId(classNoCourse.getWeekday().getSeq());
			vo.setWeekdayName(classNoCourse.getWeekday().getName());
			
			result.add(vo);
		}
		return result;
	}

	@Override
	public void deleteByClassAndYearAndTerm(SchoolClass schoolClass,
			SchoolYear schoolYear, Term term) {
		// TODO Auto-generated method stub
		List<ClassNoCourse> classNoCourses = classNoCourseDao.findBySchoolClassAndSchoolYearAndTerm(schoolClass, schoolYear, term);
		classNoCourseDao.delete(classNoCourses);
	}

	@Override
	public void save(ClassNoCourse classNoCourse) {
		// TODO Auto-generated method stub
		classNoCourseDao.save(classNoCourse);
	}
	
	

	
	
	
	
	
		
		
		

	

}
