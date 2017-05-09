package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.config.Constants;
import com.zzy.pony.dao.LessonArrangeDao;
import com.zzy.pony.dao.LessonPeriodDao;
import com.zzy.pony.dao.SchoolClassDao;
import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.LessonArrangeVo;
import com.zzy.pony.vo.LessonArrangeVo.PeriodVo;
import com.zzy.pony.vo.LessonArrangeVo.WeekDayVo;
@Service
@Transactional
public class LessonArrangeServiceImpl implements LessonArrangeService {
	@Autowired
	private LessonArrangeDao dao;
	@Autowired
	private SchoolClassDao scDao;
	@Autowired
	private LessonPeriodDao periodDao;
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private TermService termService;
	@Autowired
	private SubjectService subjectService;

	@Override
	public void add(LessonArrange sy) {
		dao.save(sy);

	}

	@Override
	public List<LessonArrange> findAll() {
		return dao.findAll();
	}

	@Override
	public LessonArrange get(int id) {
		return dao.findOne(id);
	}

	@Override
	public void update(LessonArrange sy) {
		LessonArrange old=dao.findOne(sy.getArrangeId());
		old.setLessonPeriod(sy.getLessonPeriod());
		old.setOtherLesson(sy.getOtherLesson());
		old.setWeekDay(sy.getWeekDay());
		old.setSubject(sy.getSubject());
		
		dao.save(old);
	}

	@Override
	public void delete(int id) {
		dao.delete(id);
		
	}

	@Override
	public LessonArrangeVo findArrangeVo(Integer classId) {
		//找到当前的学年和学期
		SchoolYear schoolYear=yearService.getCurrent();
		Term term=termService.getCurrent();
		SchoolClass schoolClass=scDao.getOne(classId);
		//根据条件找到所有的LessonArrange
		List<LessonArrange> list=dao.findByClassIdAndSchoolYearAndTerm(classId, schoolYear, term);
		//根据星期与时段，组织好结果
		Map<String, String> weekDaysMap=Constants.WEEKDAYS;
		List<LessonPeriod> periodList=periodDao.findBySchoolYearAndTermOrderBySeq(schoolYear, term);
		LessonArrangeVo vo=new LessonArrangeVo();
		vo.setClassId(classId);
		vo.setClassName(schoolClass.getName());
		vo.setSchoolYear(schoolYear);
		vo.setTerm(term);
		List<WeekDayVo> weekDays=new ArrayList<WeekDayVo>();
		for(Map.Entry<String, String> map: weekDaysMap.entrySet()){
			WeekDayVo weekDayVo=new WeekDayVo();
			weekDayVo.setWeekDay(map.getKey());
			weekDayVo.setWeekDayName(map.getValue());
			List<PeriodVo> periods=new ArrayList<PeriodVo>();
			for(LessonPeriod lp: periodList){
				PeriodVo period=new PeriodVo();
				period.setLessonPeriod(lp);
				setPeriodValue(period, map.getKey(), list);
				
				periods.add(period);
			}
			weekDayVo.setPeriods(periods);
			weekDays.add(weekDayVo);
		}
		vo.setWeekDays(weekDays);
		return vo;
	}
	private void setPeriodValue(PeriodVo period, String weekDay, List<LessonArrange> list){
		for(LessonArrange la: list){
			if(la.getLessonPeriod().getPeriodId() == period.getLessonPeriod().getPeriodId() && la.getWeekDay().equals(weekDay)){
				period.setArrangeId(la.getArrangeId());
				period.setOtherLesson(la.getOtherLesson());
				period.setSubject(la.getSubject());
				break;
			}
		}
	}

	@Override
	public List<LessonArrange> findByClassIdAndSchoolYearAndTerm(Integer classId, SchoolYear year, Term term) {
		// TODO Auto-generated method stub
		
		List<LessonArrange> list =  dao.findByClassIdAndSchoolYearAndTerm(classId, year, term);
		return list;
	}

	@Override
	public LessonArrange findByClassIdAndSchoolYearAndTermAndWeekDayAndLessonPeriod(
			Integer classId, SchoolYear year, Term term, String weekDay,
			LessonPeriod lessonPeriod) {
		// TODO Auto-generated method stub
		List<LessonArrange> list = dao.findByClassIdAndSchoolYearAndTermAndWeekDayAndLessonPeriod(classId, year, term, weekDay, lessonPeriod);
		if (list != null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<LessonArrange> findBySchooleYearAndTermAndSourceType(
			SchoolYear year, Term term, String sourceType) {
		// TODO Auto-generated method stub
		List<LessonArrange> result = dao.findBySchoolYearAndTermAndSourceType(year, term, sourceType);
		
		return result;
	}

	@Override
	public void deleteList(List<LessonArrange> list) {
		// TODO Auto-generated method stub
		dao.delete(list);
	}

	@Override
	public LessonArrange findByClassIdAndSubjectAndSchoolYearAndTermAndWeekDayAndLessonPeriod(
			int classId, Subject subject, SchoolYear year, Term term,
			String weekDay, LessonPeriod lessonPeriod) {
		// TODO Auto-generated method stub
		List<LessonArrange> list = dao.findByClassIdAndSubjectAndSchoolYearAndTermAndWeekDayAndLessonPeriod(classId, subject, year, term, weekDay, lessonPeriod);
		if (list != null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	
	
	
	
	
	
	
	
	

	
	
	
}
