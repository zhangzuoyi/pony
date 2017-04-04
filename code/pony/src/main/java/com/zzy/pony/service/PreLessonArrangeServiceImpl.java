package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
























import com.zzy.pony.config.Constants;
import com.zzy.pony.dao.LessonArrangeDao;
import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Term;
import com.zzy.pony.model.Weekday;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.security.ShiroDbRealm.ShiroUser;
import com.zzy.pony.vo.ArrangeVo;

@Service
@Transactional
public class PreLessonArrangeServiceImpl implements PreLessonArrangeService {
	@Autowired
	private LessonArrangeDao lessonArrangeDao;
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private LessonPeriodService lessonPeriodService;
	@Autowired
	private WeekdayService weekdayService;

	@Override
	public List<ArrangeVo> findVoByClassIdAndSubject(Integer classId,
			Integer subjectId) {
		// TODO Auto-generated method stub
		List<ArrangeVo> result = new ArrayList<ArrangeVo>();
		SchoolYear schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		Subject subject = subjectService.get(subjectId);
		List<LessonArrange> lessonArranges=  lessonArrangeDao.findByClassIdAndSchoolYearAndTermAndSubjectAndSourceType(classId, schoolYear, term, subject, Constants.SOURCE_TYPE_PRE);
		if (lessonArranges!=null && lessonArranges.size()>0) {
			for (LessonArrange lessonArrange : lessonArranges) {
				ArrangeVo arrangeVo = toArrangeVo(lessonArrange);
				result.add(arrangeVo);
			}
		}
		
		return result;
	}
	
	
	@Override
	public void deleteByClassIdAndSubject(Integer classId, Integer subjectId) {
		// TODO Auto-generated method stub
		SchoolYear schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		Subject subject = subjectService.get(subjectId);
		List<LessonArrange> lessonArranges=  lessonArrangeDao.findByClassIdAndSchoolYearAndTermAndSubjectAndSourceType(classId, schoolYear, term, subject, Constants.SOURCE_TYPE_PRE);
		lessonArrangeDao.delete(lessonArranges);		
	}
	
	 


	@Override
	public void save(ArrangeVo arrangeVo) {
		// TODO Auto-generated method stub
		
		LessonArrange lessonArrange = new LessonArrange();
	
		SchoolYear schoolYear = schoolYearService.getCurrent();
		Term term= termService.getCurrent();
		Subject subject = subjectService.get(arrangeVo.getSubjectId());
		//weekday-->seq
		Weekday weekday = weekdayService.findByName(arrangeVo.getWeekDay());
		//peroid-->periodId
		String[] periods = arrangeVo.getPeriod().split("--");
		LessonPeriod lessonPeriod = lessonPeriodService.findByStartTimeAndEndTime(periods[0], periods[1]);
	
		lessonArrange.setClassId(arrangeVo.getClassId());
		lessonArrange.setCreateTime(new Date());
		lessonArrange.setCreateUser(ShiroUtil.getLoginUser().getLoginName());
		lessonArrange.setLessonPeriod(lessonPeriod);
		lessonArrange.setOtherLesson(arrangeVo.getOtherLesson());
		lessonArrange.setSchoolYear(schoolYear);
		lessonArrange.setSourceType(Constants.SOURCE_TYPE_PRE);
		lessonArrange.setSubject(subject);
		lessonArrange.setTerm(term);
		lessonArrange.setWeekDay(weekday.getSeq()+"");
		
		lessonArrangeDao.save(lessonArrange);
		
	}


	private ArrangeVo toArrangeVo(LessonArrange lessonArrange){
		ArrangeVo vo = new ArrangeVo();
		vo.setArrangeId(lessonArrange.getArrangeId());
		vo.setClassId(lessonArrange.getClassId());
		vo.setEndTime(lessonArrange.getLessonPeriod().getEndTime());
		vo.setOtherLesson(lessonArrange.getOtherLesson());
		vo.setPeriodId(lessonArrange.getLessonPeriod().getPeriodId());
		vo.setSourceType(lessonArrange.getSourceType());
		vo.setStartTime(lessonArrange.getLessonPeriod().getStartTime());
		vo.setSubjectId(lessonArrange.getSubject().getSubjectId());
		vo.setSubjectName(lessonArrange.getSubject().getName());
		vo.setTermId(lessonArrange.getTerm().getTermId());
		vo.setWeekDay(lessonArrange.getWeekDay());
		vo.setYearId(lessonArrange.getSchoolYear().getYearId());
		return vo;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
