package com.zzy.pony.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.AutoClassArrange.DNA;
import com.zzy.pony.AutoClassArrange.GeneticAlgorithm;
import com.zzy.pony.config.Constants;
import com.zzy.pony.dao.LessonArrangeDao;
import com.zzy.pony.model.ArrangeCombine;
import com.zzy.pony.model.ArrangeRotation;
import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.model.Term;
import com.zzy.pony.model.Weekday;
import com.zzy.pony.util.GAUtil;
import com.zzy.pony.vo.ArrangeVo;
import com.zzy.pony.vo.ClassNoCourseVo;
import com.zzy.pony.vo.CombineAndRotationVo;
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
	@Autowired
	private ArrangeRotationService	 arrangeRotationService;
	@Autowired
	private ArrangeCombineService arrangeCombineService;
	@Autowired
	private LessonArrangeService lessonArrangeService;
	
	
	
	
	@Override
	public void autoLessonArrange(int gradeId) {
		// TODO Auto-generated method stub
		SchoolYear schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		List<Weekday> weekdayList = weekdayService.findByhaveClass(Constants.HAVECLASS_FLAG_TRUE);
		List<Integer> weekdays = new ArrayList<Integer>();
		for (Weekday weekday : weekdayList) {
			weekdays.add(weekday.getSeq());
		}
		List<Subject> subjects = subjectService.findAll();
		List<Integer> subjectIntegers = new ArrayList<Integer>();
		Map<String, Integer> subjectImportanceMap = new HashMap<String, Integer>();
		for (Subject subject : subjects) {
			subjectIntegers.add(subject.getSubjectId());
			subjectImportanceMap.put(String.format("%02d", subject.getSubjectId()), subject.getImportance());
		}
		
		List<Integer> seqs = new ArrayList<Integer>();
		List<LessonPeriod> lessonPeriods = lessonPeriodService.findBySchoolYearAndTerm(schoolYear, term);
		
		Map<String, List<String>> seqSubjectMap = new HashMap<String, List<String>>();		
		List<Integer> significantSeq = new ArrayList<Integer>();
		List<Integer> importantSeq = new ArrayList<Integer>();
		List<Integer> commonSeq = new ArrayList<Integer>();
		
		for (LessonPeriod lessonPeriod : lessonPeriods) {
			seqs.add(lessonPeriod.getSeq());
			List<String>  subjectString = new ArrayList<String>();
			for (Subject subject : subjects) {
				if (lessonPeriod.getImportance() == subject.getImportance()) {
					subjectString.add(String.format("%02d", subject.getSubjectId()));
				}
			}
			seqSubjectMap.put(lessonPeriod.getSeq().toString(), subjectString);
			if (lessonPeriod.getImportance() != null) {
				if (lessonPeriod.getImportance() == Constants.SUBJECT_SIGNIFICANT) {
					significantSeq.add(lessonPeriod.getSeq());
				}
				if (lessonPeriod.getImportance() == Constants.SUBJECT_IMPORTANT) {
					importantSeq.add(lessonPeriod.getSeq());
				}
				if (lessonPeriod.getImportance() == Constants.SUBJECT_COMMON) {
					commonSeq.add(lessonPeriod.getSeq());
				}
			}else{
				commonSeq.add(lessonPeriod.getSeq());
			}
				
									
		}
		
		
		
		
		
		
		
		
		String[] classIdCandidate =   GAUtil.getCandidateStrings(teacherSubjectService.findCurrentAllClassId(gradeId), 3,false);    
		//String[] subjectIdCandidate =GAUtil.getCandidateStrings(teacherSubjectService.findCurrentAllSubjectId(), 2,true);
		String[] subjectIdCandidate =GAUtil.getCandidateStrings(subjectIntegers, 2,true);
		String[] teacherIdCandidate =GAUtil.getCandidateStrings(teacherSubjectService.findCurrentAllTeacherId(), 4,true); 
		String[] weekdayIdCandidate =GAUtil.getCandidateStrings(weekdays, 1, false);
		String[] seqIdCandidate=GAUtil.getCandidateStrings(seqs, 1, false);;
		List<TeacherSubjectVo> vos = teacherSubjectService.findCurrentAll(gradeId);
		List<TeacherSubjectVo> voGroups = teacherSubjectService.findCurrentByGroup(gradeId); 
		List<ClassNoCourseVo> classNoCourseVos = classNoCourseService.findCurrentAllVo();
		List<TeacherNoCourseVo> teacherNoCourseVos = teacherNoCourseService.findCurrentAllVo();
		List<SubjectNoCourseVo> subjectNoCourseVos = subjectNoCourseService.findCurrentAllVo();
		List<GradeNoCourseVo> gradeNoCourseVos = gradeNoCourseService.findCurrentAllVo();
		List<CombineAndRotationVo> combineAndRotationVos = arrangeRotationService.findAllVo();
		List<CombineAndRotationVo> combineAndRotationVos2 = arrangeCombineService.findAllVo();
		Map<String, Set<Integer>> combineMap = new HashMap<String, Set<Integer>>();
		Map<String, List<Integer>> alreadyTeacherSeqMap = new HashMap<String, List<Integer>>();
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
		DNA.getInstance().setTeacherSubjectClassMap(GAUtil.getTeacherSubjectClass(vos));
		DNA.getInstance().setTeacherSubjectIrregularClassMap(GAUtil.getTeacherSubjectIrregularClass(vos));
		DNA.getInstance().setClassInMorning(GAUtil.classInMorning(subjects));
		DNA.getInstance().setClassInAfternoon(GAUtil.classInAfternoon(subjects));
		DNA.getInstance().setTeacherSubjectRegularClassMap(GAUtil.getTeacherSubjectRegularClass(voGroups));
		DNA.getInstance().setSeqSubjectMap(seqSubjectMap);
		DNA.getInstance().setSignificantSeq(significantSeq);
		DNA.getInstance().setImportantSeq(importantSeq);
		DNA.getInstance().setCommonSeq(commonSeq);
		DNA.getInstance().setSubjectImportanceMap(subjectImportanceMap);
		DNA.getInstance().setArrangeRotationMap(GAUtil.getArrangeRotation(combineAndRotationVos));
		DNA.getInstance().setArrangeCombineMap(GAUtil.getArrangeCombine(combineAndRotationVos2));
		DNA.getInstance().setCombineMap(combineMap);
		DNA.getInstance().setAlreadyTeacherSeqMap(alreadyTeacherSeqMap);
		DNA.getInstance().setTeacherClassMap(GAUtil.getTeacherClassMap(vos));
		GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
		String bestChromosome =  geneticAlgorithm.caculte();	
		List<ArrangeVo> list =   GAUtil.getLessonArranges(bestChromosome);
		GAUtil.print(bestChromosome);
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
			SchoolClass schoolClass =  schoolClassService.get(arrangeVo.getClassId());
			if (arrangeVo.getRotationId()!=null) {
				ArrangeRotation ar = arrangeRotationService.get(arrangeVo.getRotationId());
				for (TeacherSubject	 teacherSubject : ar.getTeacherSubjects()) {
					Teacher teacher =  teacherSubject.getTeacher();
					Subject subject =  teacherSubject.getSubject();
					Boolean flag = teacherSubjectService.isExists(teacher, schoolYear, term, schoolClass, subject);
					if (flag) {
					LessonArrange lessonArrange = new LessonArrange();
					lessonArrange.setClassId(arrangeVo.getClassId());
					lessonArrange.setCreateTime(new Date());
					//lessonArrange.setCreateUser(ShiroUtil.getLoginUser().getLoginName());
					lessonArrange.setCreateUser("test");
					lessonArrange.setLessonPeriod(lessonPeriod);
					lessonArrange.setSchoolYear(schoolYear);
					lessonArrange.setSourceType("1");//自动排课
					lessonArrange.setSubject(subject);
					lessonArrange.setTerm(term);
					lessonArrange.setWeekDay(arrangeVo.getWeekdayId()+"");
					lessonArrangeDao.save(lessonArrange);
					}										
				}								
			}else if (arrangeVo.getCombineId()!=null) {
				ArrangeCombine ac  = arrangeCombineService.get(arrangeVo.getCombineId());
				for (TeacherSubject teacherSubject : ac.getTeacherSubjects()) {
					Teacher teacher =  teacherSubject.getTeacher();
					Subject subject =  teacherSubject.getSubject();
					Boolean flag = teacherSubjectService.isExists(teacher, schoolYear, term, schoolClass, subject);
					LessonArrange la =  lessonArrangeService.findByClassIdAndSubjectAndSchoolYearAndTermAndWeekDayAndLessonPeriod(schoolClass.getClassId(), subject, schoolYear, term, arrangeVo.getWeekdayId()+"", lessonPeriod);
					if (flag && la == null) {
						LessonArrange lessonArrange = new LessonArrange();
						lessonArrange.setClassId(arrangeVo.getClassId());
						lessonArrange.setCreateTime(new Date());
						//lessonArrange.setCreateUser(ShiroUtil.getLoginUser().getLoginName());
						lessonArrange.setCreateUser("test");
						lessonArrange.setLessonPeriod(lessonPeriod);
						lessonArrange.setSchoolYear(schoolYear);
						lessonArrange.setSourceType("1");//自动排课
						lessonArrange.setSubject(subject);
						lessonArrange.setTerm(term);
						lessonArrange.setWeekDay(arrangeVo.getWeekdayId()+"");
						lessonArrangeDao.save(lessonArrange);
					}
				}
				
			}else{
				Teacher teacher =  teacherService.get(arrangeVo.getTeacherId());
				Subject subject =  subjectService.get(arrangeVo.getSubjectId());
				Boolean flag = teacherSubjectService.isExists(teacher, schoolYear, term, schoolClass, subject);
				if (flag) {
				LessonArrange lessonArrange = new LessonArrange();
				lessonArrange.setClassId(arrangeVo.getClassId());
				lessonArrange.setCreateTime(new Date());
				//lessonArrange.setCreateUser(ShiroUtil.getLoginUser().getLoginName());
				lessonArrange.setCreateUser("test");
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
	
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
}
