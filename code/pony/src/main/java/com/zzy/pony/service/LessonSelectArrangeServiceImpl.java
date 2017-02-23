package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.GradeDao;
import com.zzy.pony.dao.LessonSelectArrangeDao;
import com.zzy.pony.mapper.LessonSelectArrangeMapper;
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.LessonSelectArrange;
import com.zzy.pony.model.LessonSelectTime;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.LessonSelectArrangeVo;
import com.zzy.pony.vo.LessonSelectTimeVo;
@Service
@Transactional
public class LessonSelectArrangeServiceImpl implements LessonSelectArrangeService {
	@Autowired
	private LessonSelectArrangeDao dao;
	@Autowired
	private LessonSelectArrangeMapper mapper;
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private TermService termService;
	@Autowired
	private GradeDao gradeDao;

	@Override
	public void add(LessonSelectArrange sy) {
		dao.save(sy);

	}

	@Override
	public List<LessonSelectArrange> findAll() {
		return dao.findAll();
	}

	@Override
	public LessonSelectArrange get(int id) {
		return dao.findOne(id);
	}

	@Override
	public void update(LessonSelectArrange sy) {
		LessonSelectArrange old=dao.findOne(sy.getArrangeId());
		old.setClassroom(sy.getClassroom());
		old.setCredit(sy.getCredit());
		old.setGrades(sy.getGrades());
		old.setLessonSelectTimes(sy.getLessonSelectTimes());
		old.setLowerLimit(sy.getLowerLimit());
		old.setPeriod(sy.getPeriod());
		old.setSubject(sy.getSubject());
		old.setTeacher(sy.getTeacher());
		old.setUpperLimit(sy.getUpperLimit());
		//学年与学期不可修改
		
		dao.save(old);
	}

	@Override
	public void delete(int id) {
		dao.delete(id);
		
	}

	@Override
	public void add(LessonSelectArrangeVo vo) {
		LessonSelectArrange arr=new LessonSelectArrange();
		arr.setClassroom(vo.getClassroom());
		arr.setCredit(vo.getCredit());
		List<Grade> grades=new ArrayList<Grade>();
		for(Integer gradeId: vo.getGradeIds()){
			Grade grade=new Grade();
			grade.setGradeId(gradeId);
			grades.add(grade);
		}
		arr.setGrades(grades);
		List<LessonSelectTime> times=new ArrayList<LessonSelectTime>();
		for(LessonSelectTimeVo st: vo.getLessonSelectTimes()){
			LessonSelectTime time=new LessonSelectTime();
			LessonPeriod lessonPeriod=new LessonPeriod();
			lessonPeriod.setPeriodId(st.getPeriodId());
			time.setLessonPeriod(lessonPeriod);
			time.setWeekDay(st.getWeekday());
			time.setLessonSelectArrange(arr);
			times.add(time);
		}
		arr.setLessonSelectTimes(times);
		arr.setLowerLimit(vo.getLowerLimit());
		arr.setPeriod(vo.getPeriod());
		SchoolYear schoolYear=new SchoolYear();
		schoolYear.setYearId(vo.getYearId());
		arr.setSchoolYear(schoolYear);
		Subject subject=new Subject();
		subject.setSubjectId(vo.getSubjectId());
		arr.setSubject(subject);
		Teacher teacher=new Teacher();
		teacher.setTeacherId(vo.getTeacherId());
		arr.setTeacher(teacher);
		Term term=new Term();
		term.setTermId(vo.getTermId());
		arr.setTerm(term);
		arr.setUpperLimit(vo.getUpperLimit());
		
		dao.save(arr);
	}

	@Override
	public List<LessonSelectArrangeVo> findCurrent() {
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		List<LessonSelectArrangeVo> list=mapper.findBySchoolYearAndTerm(year.getYearId(), term.getTermId(), null);
		List<LessonSelectTimeVo> timeList=mapper.findTimeBySchoolYearAndTerm(year.getYearId(), term.getTermId(), null);
		List<Grade> grades=gradeDao.findAll();
		addGradesAndTimes(list,grades,timeList);
		return list;
	}
	/**
	 * 给VO列表增加年级和时间设置值
	 * @param list
	 * @param grades
	 * @param timeList
	 */
	private void addGradesAndTimes(List<LessonSelectArrangeVo> list,List<Grade> grades,List<LessonSelectTimeVo> timeList){
		for(LessonSelectArrangeVo vo: list){
			vo.setGradeIdsFromStr();
			List<Grade> voGrades=new ArrayList<Grade>();
			for(Grade g: grades){
				if(vo.getGradeIds().contains(g.getGradeId())){
					voGrades.add(g);
				}
			}
			vo.setGrades(voGrades);
			
			List<LessonSelectTimeVo> voTimes=new ArrayList<LessonSelectTimeVo>();
			for(LessonSelectTimeVo tvo: timeList){
				if(tvo.getArrangeId() == vo.getArrangeId()){
					voTimes.add(tvo);
				}
			}
			vo.setLessonSelectTimes(voTimes);
		}
	}

	@Override
	public List<LessonSelectArrangeVo> findCurrentByGrade(Integer gradeId) {
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		List<LessonSelectArrangeVo> list=mapper.findBySchoolYearAndTerm(year.getYearId(), term.getTermId(), gradeId);
		List<LessonSelectTimeVo> timeList=mapper.findTimeBySchoolYearAndTerm(year.getYearId(), term.getTermId(), gradeId);
		List<Grade> grades=gradeDao.findAll();
		addGradesAndTimes(list,grades,timeList);
		return list;
	}

	@Override
	public LessonSelectArrangeVo findById(Integer id) {
		LessonSelectArrangeVo vo=mapper.findById(id);
		vo.setGradeIdsFromStr();
		List<LessonSelectTimeVo> timeList=mapper.findByArrangeId(id);
		vo.setLessonSelectTimes(timeList);
		return vo;
	}

}
