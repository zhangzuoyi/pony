package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzy.pony.AutoClassArrange.DNA;
import com.zzy.pony.AutoClassArrange.GeneticAlgorithm;
import com.zzy.pony.config.Constants;
import com.zzy.pony.dao.LessonArrangeDao;
import com.zzy.pony.mapper.LessonArrangeMapper;
import com.zzy.pony.mapper.TeacherSubjectMapper;
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
import com.zzy.pony.util.GAUtilTwo;
import com.zzy.pony.util.WeekSeqUtil;
import com.zzy.pony.vo.ArrangeVo;
import com.zzy.pony.vo.ClassNoCourseVo;
import com.zzy.pony.vo.CombineAndRotationVo;
import com.zzy.pony.vo.ConditionVo;
import com.zzy.pony.vo.GradeNoCourseVo;
import com.zzy.pony.vo.SubjectNoCourseVo;
import com.zzy.pony.vo.TeacherNoCourseVo;
import com.zzy.pony.vo.TeacherSubjectVo;

import net.sf.jasperreports.components.barbecue.BarcodeProviders.NW7Provider;

@Transactional
@Service
public class AutoLessonArrangeServiceImpl implements AutoLessonArrangeService {

	private static Logger log = LoggerFactory.getLogger(AutoLessonArrangeServiceImpl.class);

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
	private ArrangeRotationService arrangeRotationService;
	@Autowired
	private ArrangeCombineService arrangeCombineService;
	@Autowired
	private LessonArrangeService lessonArrangeService;
	@Autowired
	private PreLessonArrangeService preLessonArrangeService;
	@Autowired
	private TeacherSubjectMapper teacherSubjectMapper;
	@Autowired
	private LessonArrangeMapper lessonArrangeMapper;

	@Override
	public boolean autoLessonArrange(int gradeId) {
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
			List<String> subjectString = new ArrayList<String>();
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
			} else {
				commonSeq.add(lessonPeriod.getSeq());
			}

		}

		String[] classIdCandidate = GAUtil.getCandidateStrings(teacherSubjectService.findCurrentAllClassId(gradeId), 3,
				false);
		// String[] subjectIdCandidate
		// =GAUtil.getCandidateStrings(teacherSubjectService.findCurrentAllSubjectId(),
		// 2,true);
		String[] subjectIdCandidate = GAUtil.getCandidateStrings(subjectIntegers, 2, true);
		String[] teacherIdCandidate = GAUtil.getCandidateStrings(teacherSubjectService.findCurrentAllTeacherId(), 4,
				true);
		String[] weekdayIdCandidate = GAUtil.getCandidateStrings(weekdays, 1, false);
		String[] seqIdCandidate = GAUtil.getCandidateStrings(seqs, 1, false);
		;
		List<TeacherSubjectVo> vos = teacherSubjectService.findCurrentAll(gradeId);
		List<TeacherSubjectVo> voGroups = teacherSubjectService.findCurrentByGroup(gradeId);
		ConditionVo cv = new ConditionVo();
		cv.setYearId(schoolYear.getYearId());
		cv.setTermId(term.getTermId());
		cv.setGradeId(gradeId);
		cv.setSourceType(Constants.SOURCE_TYPE_PRE);
		List<TeacherSubjectVo> voSeq = teacherSubjectMapper.findArrangeSeq(cv);
		List<ClassNoCourseVo> classNoCourseVos = classNoCourseService.findCurrentAllVo();
		List<TeacherNoCourseVo> teacherNoCourseVos = teacherNoCourseService.findCurrentAllVo();
		List<SubjectNoCourseVo> subjectNoCourseVos = subjectNoCourseService.findCurrentAllVo();
		List<GradeNoCourseVo> gradeNoCourseVos = gradeNoCourseService.findCurrentAllVo();
		List<CombineAndRotationVo> combineAndRotationVos = arrangeRotationService.findCurrentAllVo();
		List<CombineAndRotationVo> combineAndRotationVos2 = arrangeCombineService.findCurrentAllVo();
		List<ArrangeVo> arrangeVos = preLessonArrangeService.findCurrentVo();
		List<ArrangeVo> arrangeVos2 = preLessonArrangeService.findCurrentWeekArrange();
		List<ArrangeVo> arrangeVos3 = lessonArrangeMapper.findPreTeacherAlready(cv);
		Map<String, Set<Integer>> combineMap = new HashMap<String, Set<Integer>>();
		Map<String, List<Integer>> alreadyTeacherSeqMap = new HashMap<String, List<Integer>>();
		DNA.getInstance().setClassIdCandidate(classIdCandidate);
		DNA.getInstance().setSeqIdCandidate(seqIdCandidate);
		DNA.getInstance().setSubjectIdCandidate(subjectIdCandidate);
		DNA.getInstance().setTeacherIdCandidate(teacherIdCandidate);
		DNA.getInstance().setWeekdayIdCandidate(weekdayIdCandidate);
		DNA.getInstance().setTeacherSubjectweekArrange(GAUtil.getTeacherSubjectweekArrange(vos));
		DNA.getInstance().setPreNoCourse(GAUtil.getPreNoCourse(arrangeVos));
		DNA.getInstance().setPreWeekArrange(GAUtil.getPreClassTeacherSubjectweekArrange(arrangeVos2));
		DNA.getInstance().setPreTeacherAlready(GAUtil.getPreTeacherAlready(arrangeVos3));
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
		// DNA.getInstance().setArrangeCombineMap(GAUtil.getArrangeCombine(combineAndRotationVos2));
		DNA.getInstance().setArrangeCombineMap(arrangeCombineService.getCombineMap());
		DNA.getInstance().setSpecialMap(arrangeCombineService.getSpecialMap());
		DNA.getInstance().setCombineMap(combineMap);
		DNA.getInstance().setAlreadyTeacherSeqMap(alreadyTeacherSeqMap);
		DNA.getInstance().setTeacherClassMap(GAUtil.getTeacherClassMap(vos));
		DNA.getInstance().setArrangeSeq(GAUtil.getArrangeSeq(voSeq));
		GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
		String bestChromosome = geneticAlgorithm.caculte();
		List<ArrangeVo> list = GAUtil.getLessonArranges(bestChromosome);
		// GAUtil.print(bestChromosome);
		this.save(list);

		return true;
	}

	@Override
	public void save(List<ArrangeVo> list) {
		// TODO Auto-generated method stub
		SchoolYear schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();

		for (ArrangeVo arrangeVo : list) {
			LessonPeriod lessonPeriod = lessonPeriodService.findBySchoolYearAndTermAndSeq(schoolYear, term,
					arrangeVo.getSeqId());
			// Weekday weekDay = weekdayService.get(arrangeVo.getWeekdayId());
			SchoolClass schoolClass = schoolClassService.get(arrangeVo.getClassId());
			if (arrangeVo.getRotationId() != null) {
				ArrangeRotation ar = arrangeRotationService.get(arrangeVo.getRotationId());
				for (TeacherSubject teacherSubject : ar.getTeacherSubjects()) {
					Teacher teacher = teacherSubject.getTeacher();
					Subject subject = teacherSubject.getSubject();
					Boolean flag = teacherSubjectService.isExists(teacher, schoolYear, term, schoolClass, subject);
					if (flag) {
						LessonArrange lessonArrange = new LessonArrange();
						lessonArrange.setClassId(arrangeVo.getClassId());
						lessonArrange.setCreateTime(new Date());
						// lessonArrange.setCreateUser(ShiroUtil.getLoginUser().getLoginName());
						lessonArrange.setCreateUser("test");
						lessonArrange.setLessonPeriod(lessonPeriod);
						lessonArrange.setSchoolYear(schoolYear);
						lessonArrange.setSourceType("1");// 自动排课
						lessonArrange.setSubject(subject);
						lessonArrange.setTerm(term);
						lessonArrange.setWeekDay(arrangeVo.getWeekdayId() + "");
						lessonArrangeDao.save(lessonArrange);
					}
				}
			} else if (arrangeVo.getCombineId() != null) {
				ArrangeCombine ac = arrangeCombineService.get(arrangeVo.getCombineId());
				for (TeacherSubject teacherSubject : ac.getTeacherSubjects()) {
					Teacher teacher = teacherSubject.getTeacher();
					Subject subject = teacherSubject.getSubject();
					Boolean flag = teacherSubjectService.isExists(teacher, schoolYear, term, schoolClass, subject);
					LessonArrange la = lessonArrangeService
							.findByClassIdAndSubjectAndSchoolYearAndTermAndWeekDayAndLessonPeriod(
									schoolClass.getClassId(), subject, schoolYear, term, arrangeVo.getWeekdayId() + "",
									lessonPeriod);
					if (flag && la == null) {
						LessonArrange lessonArrange = new LessonArrange();
						lessonArrange.setClassId(arrangeVo.getClassId());
						lessonArrange.setCreateTime(new Date());
						// lessonArrange.setCreateUser(ShiroUtil.getLoginUser().getLoginName());
						lessonArrange.setCreateUser("test");
						lessonArrange.setLessonPeriod(lessonPeriod);
						lessonArrange.setSchoolYear(schoolYear);
						lessonArrange.setSourceType("1");// 自动排课
						lessonArrange.setSubject(subject);
						lessonArrange.setTerm(term);
						lessonArrange.setWeekDay(arrangeVo.getWeekdayId() + "");
						lessonArrangeDao.save(lessonArrange);
					}
				}

			} else {
				Teacher teacher = teacherService.get(arrangeVo.getTeacherId());
				Subject subject = subjectService.get(arrangeVo.getSubjectId());
				Boolean flag = teacherSubjectService.isExists(teacher, schoolYear, term, schoolClass, subject);
				if (flag) {
					LessonArrange lessonArrange = new LessonArrange();
					lessonArrange.setClassId(arrangeVo.getClassId());
					lessonArrange.setCreateTime(new Date());
					// lessonArrange.setCreateUser(ShiroUtil.getLoginUser().getLoginName());
					lessonArrange.setCreateUser("test");
					lessonArrange.setLessonPeriod(lessonPeriod);
					lessonArrange.setSchoolYear(schoolYear);
					lessonArrange.setSourceType("1");// 自动排课
					lessonArrange.setSubject(subject);
					lessonArrange.setTerm(term);
					lessonArrange.setWeekDay(arrangeVo.getWeekdayId() + "");
					lessonArrangeDao.save(lessonArrange);
				}
			}

		}

	}

	@Override
	public boolean autoLessonArrangeTwo(int gradeId) {
		SchoolYear year = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		// key classId value(key weekSeq value subjectId)
		Map<Integer, Map<Integer, Integer>> autoArrangeMap = new HashMap<Integer, Map<Integer, Integer>>();
		// key classId value(key teacherId value weekArrange)
		Map<Integer, Map<Integer, Integer>> classTSMap = new HashMap<Integer, Map<Integer, Integer>>();
		// key teacherId value(key classId value weekArrange)
		Map<Integer, Map<Integer, Integer>> teacherTSMap = new HashMap<Integer, Map<Integer, Integer>>();
		Map<Integer, Set<Integer>> classAlreadyMap = new HashMap<Integer, Set<Integer>>();
		// key classId value(key:teacherId value: weekSeq)
		Map<Integer, Map<Integer, List<Integer>>> classMap = new HashMap<Integer, Map<Integer, List<Integer>>>();
		// key teacherId value(key:classId value: weekSeq) 预排的
		Map<Integer, Map<Integer, List<Integer>>> preTeacherMap = new LinkedHashMap<Integer, Map<Integer, List<Integer>>>();
		// key teacherId value(key:classId value: weekSeq) 整体的 (不用考虑班级因素)
		Map<Integer, Map<Integer, List<Integer>>> teacherClassMap = new LinkedHashMap<Integer, Map<Integer, List<Integer>>>();
		Map<Integer, List<Integer>> teacherMap = new LinkedHashMap<Integer, List<Integer>>();// 本班的
		// key teacherId value subjectId
		Map<Integer, Integer> teacherSubjectMap = new HashMap<Integer, Integer>();
		// key classId value(key:subjectId value: teacherId)
		Map<Integer, Map<Integer, Integer>> subjectTeacherMap = new HashMap<Integer, Map<Integer, Integer>>();

		// sigList 语数英 impList 物化政史地生 comList 其他
		List<Integer> sigList = new ArrayList<Integer>();
		List<Integer> impList = new ArrayList<Integer>();
		List<Integer> comList = new ArrayList<Integer>();
		// 获取科目重要程度
		List<Subject> subjects = subjectService.findAll();
		GAUtilTwo.getSubjectList(subjects, sigList, impList, comList);

		// 不排课设置(年级)

		List<GradeNoCourseVo> gradeNoCourseVos = gradeNoCourseService.findCurrentAllVo();
		Map<Integer, List<Integer>> gradeNoCourseMap = GAUtilTwo.getGradeNoCourse(gradeId, gradeNoCourseVos);

		// 走班设置 (走班仅发生在一个班)
		List<CombineAndRotationVo> combineAndRotationVos = arrangeRotationService.findCurrentAllVo();
		Map<Integer, List<Integer>> rotationSubjectMap = new HashMap<Integer, List<Integer>>();
		Map<String, Integer> rotationMap = GAUtilTwo.getArrangeRotation(combineAndRotationVos, rotationSubjectMap);

		// 无法安排的 classId teacherId size
		Map<Integer, Map<Integer, Integer>> unableMap = new LinkedHashMap<Integer, Map<Integer, Integer>>();

		// 1获取总的上课安排
		List<TeacherSubjectVo> teacherSubjectVos = teacherSubjectService.findByGrade(year.getYearId(), term.getTermId(),
				gradeId);
		// 走班处理
		GAUtilTwo.rotationHandle(teacherSubjectVos, rotationMap);

		GAUtilTwo.getTeacherSubject(teacherSubjectVos, classTSMap, teacherTSMap, teacherSubjectMap, subjectTeacherMap);
		// 2获取预排
		List<ArrangeVo> preArrangeVos = preLessonArrangeService.findCurrentVoByGrade(gradeId);
		GAUtilTwo.getPre(preArrangeVos, classMap, teacherMap, teacherClassMap, preTeacherMap, classAlreadyMap);
		// 3按照班级顺序排课
		List<SchoolClass> schoolClasses = schoolClassService.findByYearAndGradeOrderBySeq(year.getYearId(), gradeId);
		ConditionVo cv = new ConditionVo();
		cv.setYearId(year.getYearId());
		cv.setTermId(term.getTermId());
		cv.setGradeId(gradeId);
		List<TeacherSubjectVo> voSeq = teacherSubjectMapper.findArrangeSeq(cv);
		
		try {
			
		
		for (SchoolClass sc : schoolClasses) {

			Map<Integer, Integer> classTSInnerMap = classTSMap.get(sc.getClassId());
			Map<Integer, Integer> classSubjectTeacherMap = subjectTeacherMap.get(sc.getClassId());
			Map<Integer, Integer> sortClassTSInnerMap = GAUtilTwo.sortBySubject(classTSInnerMap, classSubjectTeacherMap,
					subjects, voSeq);
			Map<Integer, List<Integer>> preClassMap = classMap.get(sc.getClassId());
			Set<Integer> classAlreadySet = classAlreadyMap.get(sc.getClassId());
			Map<Integer, Integer> innerAutoArrangeMap = new HashMap<Integer, Integer>();
			List<Integer> noCourseList = new ArrayList<Integer>();
			List<Integer> gradeNoCourseList = gradeNoCourseMap.get(sc.getClassId());// 年级不排课
			if (gradeNoCourseList != null && gradeNoCourseList.size() > 0) {
				noCourseList.addAll(gradeNoCourseList);
			}
			// 按照老师(即科目)的顺序来排 (语文在周三周五第一节，英语在二四第一节)
			for (Integer teacherId : sortClassTSInnerMap.keySet()) {
				int preArrangeCount = 0;
				if (preClassMap != null && preClassMap.get(teacherId) != null) {
					preArrangeCount = preClassMap.get(teacherId).size();
				}
				int autoArrangeCount = classTSInnerMap.get(teacherId) - preArrangeCount;
				List<Integer> preAlreadyTeacherList = new ArrayList<Integer>();// 本班的预排
				int preSize = 0;// 预排课程数,用来判断是否第一次排
				List<Integer> preAlreadyTeacherLAllist = new ArrayList<Integer>();// 整体的预排
				List<Integer> alreadyTeacherList = new ArrayList<Integer>();// 本班的已安排
				List<Integer> alreadyTeacherAllList = new ArrayList<Integer>();// 所有的已安排

				if (preTeacherMap.get(teacherId) != null) {
					for (Integer key : preTeacherMap.get(teacherId).keySet()) {
						preSize += preTeacherMap.get(teacherId).get(key).size();
						preAlreadyTeacherLAllist.addAll(preTeacherMap.get(teacherId).get(key));
					}
				}
				if (preTeacherMap.get(teacherId) != null && preTeacherMap.get(teacherId).get(sc.getClassId()) != null) {
					preAlreadyTeacherList = preTeacherMap.get(teacherId).get(sc.getClassId());// 已经预排的
					alreadyTeacherList = teacherClassMap.get(teacherId).get(sc.getClassId());
				}
				if (teacherMap.get(teacherId) != null) {
					alreadyTeacherAllList = teacherMap.get(teacherId);
				}

				// 已经预排的和未排的超过5天
				if (autoArrangeCount + WeekSeqUtil.getWeek(preAlreadyTeacherList).size() > 5) {
					log.error("----------------" + sc.getName() + ":老师(" + teacherId + ")"
							+ "预排与自动排的超过5天----------------");
				}

				// System.out.println("-----前:"+preAlreadyTeacherList.size());
				Set<Integer> availWeek = WeekSeqUtil.getAvailWeek(preAlreadyTeacherList);
				// System.out.println("-----后:"+availWeek.size());

				Set<Integer> teacherSet = new HashSet<Integer>();// 保存当次的教师上课安排
				Set<Integer> alreadyWeekSet = new HashSet<Integer>();

				for (int i = 0; i < autoArrangeCount; i++) {

					// 从预排过后的剩下的星期中选择 (每天安排的课程不能超过3节)
					int week = WeekSeqUtil.getRandomWeek(availWeek, preAlreadyTeacherList, alreadyTeacherList,
							alreadyTeacherAllList, classAlreadySet, alreadyWeekSet);
					if (week == 0) {
						// 无法安排
						if (unableMap.containsKey(sc.getClassId())) {
							Map<Integer, Integer> innerMap = unableMap.get(sc.getClassId());
							if (innerMap.containsKey(teacherId)) {
								innerMap.put(teacherId, innerMap.get(teacherId).intValue() + 1);
							} else {
								innerMap.put(teacherId, 1);
							}
						} else {
							Map<Integer, Integer> innerMap = new HashMap<Integer, Integer>();
							innerMap.put(teacherId, 1);
							unableMap.put(sc.getClassId(), innerMap);
						}
						continue;
					}
					availWeek.remove(week);
					alreadyWeekSet.add(week);
					/*
					 * weekseq的获取 1 不能在已安排的课程中 classAlreadySet 2 满足年级不排课(放在classAlreadySet) 3
					 * 满足班级不排课(放在classAlreadySet) 4 满足老师不排课 5 满足科目不排课 6 若该老师在其他班级有安排，则需要靠拢 7
					 * 重要程度的设定，语数外尽量在上午
					 * 
					 */
					int type = 0;
					int subjectId = 0;
					if (teacherId > 990) {
						type = Constants.SUBJECT_IMPORTANT;
					} else {
						subjectId = teacherSubjectMap.get(teacherId);
						if (sigList.contains(subjectId)) {
							type = Constants.SUBJECT_SIGNIFICANT;
							if (i == autoArrangeCount - 1) {
								type = Constants.SUBJECT_COMMON;// 语数英选择一节
							}
						}
						if (impList.contains(subjectId)) {
							type = Constants.SUBJECT_IMPORTANT;
						}
						if (comList.contains(subjectId)) {
							type = Constants.SUBJECT_COMMON;
						}
					}

					int weekSeq = WeekSeqUtil.getWeekSeq(week, preAlreadyTeacherList, preAlreadyTeacherLAllist,
							alreadyTeacherList, alreadyTeacherAllList, classAlreadySet, type, preSize, noCourseList);

					classAlreadySet.add(weekSeq);
					teacherSet.add(weekSeq);
					availWeek.remove(WeekSeqUtil.getWeek(weekSeq));
					if (teacherId > 990) {
						// 走班
						innerAutoArrangeMap.put(weekSeq, teacherId);
					} else {
						innerAutoArrangeMap.put(weekSeq, teacherSubjectMap.get(teacherId));
					}
				}
				// 所有的
				if (alreadyTeacherAllList == null || alreadyTeacherAllList.size() == 0) {
					List<Integer> innerList = new ArrayList<Integer>();
					innerList.addAll(teacherSet);
					teacherMap.put(teacherId, innerList);
				} else {
					alreadyTeacherAllList.addAll(teacherSet);
				}
				// 本班的
				if (alreadyTeacherList == null || alreadyTeacherList.size() == 0) {
					if (teacherClassMap.get(teacherId) != null) {
						List<Integer> innerList = new ArrayList<Integer>();
						innerList.addAll(teacherSet);
						teacherClassMap.get(teacherId).put(sc.getClassId(), innerList);
					} else {
						Map<Integer, List<Integer>> innerMap = new HashMap<Integer, List<Integer>>();
						List<Integer> innerList = new ArrayList<Integer>();
						innerList.addAll(teacherSet);
						innerMap.put(sc.getClassId(), innerList);
						teacherClassMap.put(teacherId, innerMap);
					}
				} else {
					alreadyTeacherList.addAll(teacherSet);
				}
			}
			autoArrangeMap.put(sc.getClassId(), innerAutoArrangeMap);
			WeekSeqUtil.printCourseTable(sc.getClassId(), teacherSubjectMap, preClassMap, innerAutoArrangeMap);
		}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		saveTwo(autoArrangeMap, year, term, rotationSubjectMap);
		System.out.println(unableMap.size());
		saveUnable(unableMap, year, term, gradeId);
		return true;
	}

	@Override
	public void saveUnable(Map<Integer, Map<Integer, Integer>> unableMap, SchoolYear year, Term term, int gradeId) {
		// TODO Auto-generated method stub
		List<LessonPeriod> lessonPeriods = lessonPeriodService.findBySchoolYearAndTerm(year, term);
		Random random = new Random();
		List<LessonArrange> las = new ArrayList<LessonArrange>();
		for (Integer classId : unableMap.keySet()) {
			Map<Integer, Integer> innerMap = unableMap.get(classId);
			// 根据classId获取该班空闲的
			List<LessonArrange> lessonArranges = lessonArrangeService.findByClassIdAndSchoolYearAndTerm(classId, year,
					term);
			List<String> weekSeqs = GAUtilTwo.getAvailableWeekseq(lessonPeriods, lessonArranges);
			for (Integer teacherId : innerMap.keySet()) {
				int count = 1;
				while (count <= innerMap.get(teacherId)) {
					// LessonArrange la =
					// lessonArrangeService.findByClassIdAndSubjectAndSchoolYearAndTermAndWeekDayAndLessonPeriod(classId,
					// subject, year, term, weekDay, lessonPeriod)
					String weekSeq = weekSeqs.get(random.nextInt(weekSeqs.size()));
					if (lessonArrangeService.isTeacherConflict(Integer.valueOf(weekSeq.split(";")[0]),
							Integer.valueOf(weekSeq.split(";")[1]), teacherId)) {
						List<LessonArrange> regionLessonArranges = getRegionArranges(year, term, classId, weekSeq);
						regionAdjust(year.getYearId(), term.getTermId(), gradeId, classId, teacherId, weekSeq,
								regionLessonArranges);
						weekSeqs.remove(weekSeq);

					} else {
						LessonArrange la = new LessonArrange();
						la.setClassId(classId);
						Subject subject = subjectService.findByTeacherAndGrade(teacherId, gradeId);
						la.setSubject(subject);
						la.setWeekDay(weekSeq.split(";")[0]);
						LessonPeriod lp = lessonPeriodService.get(Integer.valueOf(weekSeq.split(";")[1]));
						la.setLessonPeriod(lp);
						la.setCreateTime(new Date());
						la.setCreateUser("testThree");
						la.setSchoolYear(year);
						la.setTerm(term);
						la.setSourceType(Constants.SOURCE_TYPE_AUTO);
						las.add(la);
						weekSeqs.remove(weekSeq);
					}
					count++;
				}
			}
		}
		lessonArrangeDao.save(las);

	}

	@Override
	public void saveTwo(Map<Integer, Map<Integer, Integer>> autoArrangeMap, SchoolYear year, Term term,
			Map<Integer, List<Integer>> rotationSubjectMap) {
		List<LessonPeriod> lessonPeriods = lessonPeriodService.findBySchoolYearAndTerm(year, term);
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (LessonPeriod lp : lessonPeriods) {
			map.put(lp.getSeq(), lp.getPeriodId());
		}
		for (int classId : autoArrangeMap.keySet()) {
			List<LessonArrange> lessonArranges = new ArrayList<LessonArrange>();
			Map<Integer, Integer> innerAutoArrangeMap = autoArrangeMap.get(classId);
			for (int weekSeq : innerAutoArrangeMap.keySet()) {
				if (innerAutoArrangeMap.get(weekSeq) > 990) {
					// 走班
					for (Integer subjectId : rotationSubjectMap.get(innerAutoArrangeMap.get(weekSeq))) {
						LessonArrange la = new LessonArrange();
						la.setClassId(classId);
						la.setWeekDay(WeekSeqUtil.getWeek(weekSeq) + "");
						LessonPeriod lp = lessonPeriodService.get(map.get(WeekSeqUtil.getSeq(weekSeq)));
						la.setLessonPeriod(lp);
						la.setCreateTime(new Date());
						la.setCreateUser("testTwo");
						la.setSchoolYear(year);
						la.setTerm(term);
						la.setSourceType(Constants.SOURCE_TYPE_AUTO);
						Subject subject = subjectService.get(subjectId);
						la.setSubject(subject);
						lessonArranges.add(la);
					}
				} else {
					LessonArrange la = new LessonArrange();
					la.setClassId(classId);
					la.setWeekDay(WeekSeqUtil.getWeek(weekSeq) + "");
					LessonPeriod lp = lessonPeriodService.get(map.get(WeekSeqUtil.getSeq(weekSeq)));
					la.setLessonPeriod(lp);
					la.setCreateTime(new Date());
					la.setCreateUser("testTwo");
					la.setSchoolYear(year);
					la.setTerm(term);
					la.setSourceType(Constants.SOURCE_TYPE_AUTO);
					Subject subject = subjectService.get(innerAutoArrangeMap.get(weekSeq));
					la.setSubject(subject);
					lessonArranges.add(la);
				}

			}
			lessonArrangeDao.save(lessonArranges);
		}
	}

	/**
	 * @param yearId
	 * @param termId
	 * @param lessonArranges
	 *            区域调整,某一班级不跨天
	 */
	private void regionAdjust(int yearId, int termId, int gradeId, int classId, int teacherId, String weekSeq,
			List<LessonArrange> lessonArranges) {
		List<Integer> teacherIds = new ArrayList<Integer>();
		List<String> weekSeqs = new ArrayList<String>();
		teacherIds.add(teacherId);
		weekSeqs.add(weekSeq);
		SchoolYear year = schoolYearService.get(yearId);
		Term term = termService.get(termId);
		for (LessonArrange la : lessonArranges) {
			// teacher
			SchoolClass schoolClass = schoolClassService.get(la.getClassId());
			TeacherSubject ts = teacherSubjectService.findCurrentByClassAndSubject(schoolClass, la.getSubject());
			teacherIds.add(ts.getTeacher().getTeacherId());
			// weekSeq
			weekSeqs.add(la.getWeekDay() + ";" + la.getLessonPeriod().getPeriodId());
		}
		// 删除原有
		lessonArrangeDao.delete(lessonArranges);
		boolean flag = true;
		List<LessonArrange> las = new ArrayList<LessonArrange>();
		List<String> conflictList = new ArrayList<String>();
		Random random = new Random();
		while (flag) {
			List<List<String>> arranges = GAUtilTwo.availableArrange(teacherIds, weekSeqs, conflictList);
			List<String> arrange = arranges.get(random.nextInt(arranges.size()));
			int count = 0;
			for (String weekseq : arrange) {
				if (!lessonArrangeService.isTeacherConflict(Integer.valueOf(weekseq.split(";")[0]),
						Integer.valueOf(weekseq.split(";")[1]), Integer.valueOf(weekseq.split(";")[2]))) {
					count++;
					LessonArrange la = new LessonArrange();
					la.setClassId(classId);
					Subject subject = subjectService.findByTeacherAndGrade(Integer.valueOf(weekseq.split(";")[2]),
							gradeId);
					la.setSubject(subject);
					la.setWeekDay(weekseq.split(";")[0]);
					LessonPeriod lp = lessonPeriodService.get(Integer.valueOf(weekseq.split(";")[1]));
					la.setLessonPeriod(lp);
					la.setCreateTime(new Date());
					la.setCreateUser("testTwo");
					la.setSchoolYear(year);
					la.setTerm(term);
					la.setSourceType(Constants.SOURCE_TYPE_AUTO);
					las.add(la);
				} else {
					conflictList.add(weekseq);
					las.clear();
					break;
				}
			}
			if (count == arrange.size()) {
				flag = false;
			}
		}
		lessonArrangeDao.save(las);
	}

	/**
	 * @param classId
	 * @param weekSeq
	 * @return 当天范围的weekseq相邻的已经安排的
	 */
	private List<LessonArrange> getRegionArranges(SchoolYear year, Term term, int classId, String weekSeq) {
		List<LessonArrange> result = new ArrayList<LessonArrange>();
		LessonPeriod lessonPeriod = lessonPeriodService.get(Integer.valueOf(weekSeq.split(";")[1]));
		int initSeq = lessonPeriod.getSeq();
		int distance = 1;
		while (result.size() < 5) {
			int previous = initSeq - distance;
			if (previous > 0) {
				LessonPeriod previouslp = lessonPeriodService.findBySchoolYearAndTermAndSeq(year, term, previous);
				LessonArrange previousla = lessonArrangeService
						.findByClassIdAndSchoolYearAndTermAndWeekDayAndLessonPeriod(classId, year, term,
								weekSeq.split(";")[0], previouslp);
				result.add(previousla);
			}
			int next = initSeq + distance;
			if (result.size() < 5 && next <= 8) {
				LessonPeriod nextlp = lessonPeriodService.findBySchoolYearAndTermAndSeq(year, term, next);
				LessonArrange nextla = lessonArrangeService.findByClassIdAndSchoolYearAndTermAndWeekDayAndLessonPeriod(
						classId, year, term, weekSeq.split(";")[0], nextlp);
				result.add(nextla);
			}
			distance++;
		}
		return result;
	}

}
