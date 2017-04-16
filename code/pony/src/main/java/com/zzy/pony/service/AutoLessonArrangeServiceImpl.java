package com.zzy.pony.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.AutoClassArrange.DNA;
import com.zzy.pony.AutoClassArrange.GeneticAlgorithm;
import com.zzy.pony.config.Constants;
import com.zzy.pony.dao.LessonArrangeDao;
import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.Term;
import com.zzy.pony.model.Weekday;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.util.GAUtil;
import com.zzy.pony.vo.ArrangeVo;
import com.zzy.pony.vo.ClassNoCourseVo;
import com.zzy.pony.vo.GradeNoCourseVo;
import com.zzy.pony.vo.SubjectNoCourseVo;
import com.zzy.pony.vo.TeacherNoCourseVo;
import com.zzy.pony.vo.TeacherSubjectVo;


@Service
@Transactional
public class AutoLessonArrangeServiceImpl implements AutoLessonArrangeService {

	@Autowired
	private TeacherSubjectService teacherSubjectService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private ClassNoCourseService classNoCourseService;
	@Autowired
	private TeacherNoCourseService teacherNoCourseService;
	@Autowired
	private SubjectNoCourseService subjectNoCourseService;
	@Autowired
	private GradeNoCourseService gradeNoCourseService;
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	@Autowired
	private SchoolClassService schoolClassService;
	@Autowired
	private LessonPeriodService lessonPeriodService;
	@Autowired
	private LessonArrangeDao lessonArrangeDao;
	@Autowired
	private WeekdayService weekdayService;
	
	
	@Override
	public void autoLessonArrange() {
		// TODO Auto-generated method stub
		SchoolYear schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		List<Weekday> weekdayList = weekdayService.findByhaveClass(Constants.HAVECLASS_FLAG_TRUE);
		List<Integer> weekdays = new ArrayList<Integer>();
		for (Weekday weekday : weekdayList) {
			weekdays.add(weekday.getSeq());
		}
		List<Integer> seqs = new ArrayList<Integer>();
		List<LessonPeriod> lessonPeriods = lessonPeriodService.findBySchoolYearAndTerm(schoolYear, term);
		for (LessonPeriod lessonPeriod : lessonPeriods) {
			seqs.add(lessonPeriod.getSeq());
		}
		
		String[] classIdCandidate =   GAUtil.getCandidateStrings(teacherSubjectService.findCurrentAllClassId(), 3,false);    
		String[] subjectIdCandidate =GAUtil.getCandidateStrings(teacherSubjectService.findCurrentAllSubjectId(), 2,true); 
		String[] teacherIdCandidate =GAUtil.getCandidateStrings(teacherSubjectService.findCurrentAllTeacherId(), 4,true); 
		String[] weekdayIdCandidate =GAUtil.getCandidateStrings(weekdays, 1, false);
		String[] seqIdCandidate=GAUtil.getCandidateStrings(seqs, 1, false);;
		List<TeacherSubjectVo> vos = teacherSubjectService.findCurrentAll();
		List<ClassNoCourseVo> classNoCourseVos = classNoCourseService.findCurrentAllVo();
		List<TeacherNoCourseVo> teacherNoCourseVos = teacherNoCourseService.findCurrentAllVo();
		List<SubjectNoCourseVo> subjectNoCourseVos = subjectNoCourseService.findCurrentAllVo();
		List<GradeNoCourseVo> gradeNoCourseVos = gradeNoCourseService.findCurrentAllVo();
		DNA.getInstance().setClassIdCandidate(classIdCandidate);
		DNA.getInstance().setSeqIdCandidate(seqIdCandidate);
		DNA.getInstance().setSubjectIdCandidate(subjectIdCandidate);
		DNA.getInstance().setTeacherIdCandidate(teacherIdCandidate);
		DNA.getInstance().setWeekdayIdCandidate(weekdayIdCandidate);
		DNA.getInstance().setTeacherSubjectweekArrange(GAUtil.getTeacherSubjectweekArrange(vos));
		DNA.getInstance().setClassNoCourse(GAUtil.getClassNoCourse(classNoCourseVos));
		DNA.getInstance().setTeacherNoCourse(GAUtil.getTeacherNoCourse(teacherNoCourseVos));
		DNA.getInstance().setSubjectNoCourse(GAUtil.getSubjectNoCourse(subjectNoCourseVos));
		DNA.getInstance().setGradeNoCourse(GAUtil.getGradeNoCourse(gradeNoCourseVos));
		GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
		String bestChromosome =  geneticAlgorithm.caculte();	
		List<ArrangeVo> list =   GAUtil.getLessonArranges(bestChromosome);
		this.save(list);
	}


	@Override
	public void save(List<ArrangeVo> list) {
		// TODO Auto-generated method stub
		SchoolYear schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		
		for (ArrangeVo arrangeVo : list) {
			LessonPeriod lessonPeriod = lessonPeriodService.findBySchoolYearAndTermAndSeq(schoolYear, term, arrangeVo.getSeqId());
			//Weekday weekDay = weekdayService.get(arrangeVo.getWeekdayId());
			Teacher teacher =  teacherService.get(arrangeVo.getTeacherId());
			SchoolClass schoolClass =  schoolClassService.get(arrangeVo.getClassId());
			Subject subject =  subjectService.get(arrangeVo.getSubjectId());
			Boolean flag = teacherSubjectService.isExists(teacher, schoolYear, term, schoolClass, subject);
			if (flag) {
			LessonArrange lessonArrange = new LessonArrange();
			lessonArrange.setClassId(arrangeVo.getClassId());
			lessonArrange.setCreateTime(new Date());
			lessonArrange.setCreateUser(ShiroUtil.getLoginUser().getLoginName());
			lessonArrange.setLessonPeriod(lessonPeriod);
			lessonArrange.setSchoolYear(schoolYear);
			lessonArrange.setSourceType("1");//自动排课
			lessonArrange.setSubject(subject);
			lessonArrange.setTerm(term);
			lessonArrange.setWeekDay(arrangeVo.getWeekdayId()+"");
			lessonArrangeDao.save(lessonArrange);
			}
		
		}
		
		
	}
	
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
}
