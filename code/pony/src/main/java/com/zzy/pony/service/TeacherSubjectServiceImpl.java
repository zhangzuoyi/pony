package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.Result;
import com.zzy.pony.dao.LessonArrangeDao;
import com.zzy.pony.dao.TeacherSubjectDao;
import com.zzy.pony.mapper.TeacherSubjectMapper;
import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.TeacherSubjectVo;
import com.zzy.pony.vo.ConditionVo;
@Service
@Transactional
public class TeacherSubjectServiceImpl implements TeacherSubjectService {
	@Autowired
	private TeacherSubjectDao dao;
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private TermService termService;
	@Autowired
	private LessonArrangeDao arrangeDao;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private TeacherSubjectMapper teacherSubjectMapper;
	@Autowired
	private SchoolClassService schoolClassService;
	
	@Override
	public void add(TeacherSubject sy) {
		dao.save(sy);

	}

	@Override
	public List<TeacherSubject> findAll() {
		return dao.findAll();
	}

	@Override
	public TeacherSubject get(int id) {
		return dao.findOne(id);
	}

	@Override
	public void update(TeacherSubject sy) {
		TeacherSubject old=dao.findOne(sy.getTsId());
		old.setSchoolClass(sy.getSchoolClass());
		old.setSubject(sy.getSubject());
		old.setTeacher(sy.getTeacher());
		
		dao.save(old);
	}

	@Override
	public void delete(int id) {
		dao.delete(id);
		
	}

	@Override
	public List<TeacherSubject> findCurrentByTeacher(Teacher teacher) {
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		
		return dao.findByTeacherAndYearAndTerm(teacher, year, term);
	}

	@Override
	public List<TeacherSubjectVo> findCurrentVoByTeacher(Teacher teacher) {
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		List<TeacherSubject> list=dao.findByTeacherAndYearAndTerm(teacher, year, term);
		List<TeacherSubjectVo> result=new ArrayList<TeacherSubjectVo>();
		for(TeacherSubject ts:list){
			List<LessonArrange> arranges=arrangeDao.findByClassIdAndSchoolYearAndTermAndSubject(ts.getSchoolClass().getClassId(), year, term, ts.getSubject());
			TeacherSubjectVo vo=TeacherSubjectVo.fromModel(ts);
			vo.setArranges(arranges);
			result.add(vo);
		}
		return result;
	}

	@Override
	public List<TeacherSubjectVo> findCurrentVoBySchoolClass(
			SchoolClass schoolClass) {
		// TODO Auto-generated method stub
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		List<TeacherSubject> list = dao.findBySchoolClassAndYearAndTerm(schoolClass, year, term);
		List<TeacherSubjectVo> result = new ArrayList<TeacherSubjectVo>();
		for(TeacherSubject ts:list){
			List<LessonArrange> arranges=arrangeDao.findByClassIdAndSchoolYearAndTermAndSubject(ts.getSchoolClass().getClassId(), year, term, ts.getSubject());
			TeacherSubjectVo vo=TeacherSubjectVo.fromModel(ts);
			vo.setArranges(arranges);
			result.add(vo);
		}
		return result;
	}
	
	
	

	@Override
	public List<TeacherSubjectVo> findCurrentAll(int gradeId) {
		// TODO Auto-generated method stub
		List<TeacherSubjectVo> result = new ArrayList<TeacherSubjectVo>();
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		List<SchoolClass> schoolClasses = schoolClassService.findByGrade(gradeId);
		List<TeacherSubject> list = new ArrayList<TeacherSubject>();
		for (SchoolClass schoolClass : schoolClasses) {
			List<TeacherSubject> teacherSubjects = dao.findBySchoolClassAndYearAndTerm(schoolClass, year, term);
			list.addAll(teacherSubjects);
		}
		
		for (TeacherSubject teacherSubject : list) {
			
			if (teacherSubject.getWeekArrange() != null) {
				TeacherSubjectVo vo  = TeacherSubjectVo.fromModel(teacherSubject);
				result.add(vo);
			}
			
			
		}
		
		return result;
	}
	
	

	@Override
	public List<TeacherSubjectVo> findCurrentAll() {
		// TODO Auto-generated method stub
		List<TeacherSubjectVo> result = new ArrayList<TeacherSubjectVo>();
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		List<TeacherSubject> list = dao.findByYearAndTerm(year, term);		
		for (TeacherSubject teacherSubject : list) {
			
			if (teacherSubject.getWeekArrange() != null) {
				TeacherSubjectVo vo  = TeacherSubjectVo.fromModel(teacherSubject);
				result.add(vo);
			}
			
			
		}
		
		return result;
	}

	@Override
	public List<Integer> findCurrentAllTeacherId() {
		// TODO Auto-generated method stub
		List<Integer> result =  new ArrayList<Integer>();
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		List<TeacherSubject> list = dao.findByYearAndTerm(year, term);		
		for (TeacherSubject teacherSubject : list) {
			Integer teacherId = teacherSubject.getTeacher().getTeacherId();
			if (!result.contains(teacherId)) {
				result.add(teacherId);
			}
		}
		
		
		return result;
	}

	@Override
	public List<Integer> findCurrentAllClassId(int gradeId) {
		// TODO Auto-generated method stub
		List<Integer> result =  new ArrayList<Integer>();
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		List<SchoolClass> schoolClasses = schoolClassService.findByGrade(gradeId);
		List<TeacherSubject> list = new ArrayList<TeacherSubject>();
		for (SchoolClass schoolClass : schoolClasses) {
			List<TeacherSubject> teacherSubjects = dao.findBySchoolClassAndYearAndTerm(schoolClass, year, term);
			list.addAll(teacherSubjects);
		}
		for (TeacherSubject teacherSubject : list) {
			if (teacherSubject.getWeekArrange()!= null) {
				Integer classId = teacherSubject.getSchoolClass().getClassId();
				if (!result.contains(classId)) {
					result.add(classId);
				}
			}
			
			
		}
		
		
		return result;
	}

	@Override
	public List<Integer> findCurrentAllSubjectId() {
		// TODO Auto-generated method stub
		List<Integer> result =  new ArrayList<Integer>();
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		List<TeacherSubject> list = dao.findByYearAndTerm(year, term);
		for (TeacherSubject teacherSubject : list) {
			Integer subjectId = teacherSubject.getSubject().getSubjectId();
			if (!result.contains(subjectId)) {
				result.add(subjectId);
			}
		}	
		return result;
	}

	@Override
	public Boolean isExists(Teacher teacher, SchoolYear schoolYear, Term term,
			SchoolClass schoolClass, Subject subject) {
		// TODO Auto-generated method stub
		List<TeacherSubject> list = dao.findByTeacherAndYearAndTermAndSchoolClassAndSubject(teacher, schoolYear, term, schoolClass, subject);
		if (list != null && list.size()>0) {
			return true;
		}
		
		return false;
	}

	@Override
	public List<TeacherSubjectVo> findCurrentVoByTeacherAndSubject(
			int teacherId, int subjectId) {
		// TODO Auto-generated method stub
		SchoolYear schoolYear = yearService.getCurrent();
		Term term = termService.getCurrent();
		Teacher teacher = teacherService.get(teacherId);
		Subject subject = subjectService.get(subjectId);
		List<TeacherSubject> list = dao.findByTeacherAndSubjectAndYearAndTerm(teacher, subject, schoolYear, term);
		List<TeacherSubjectVo> result = new ArrayList<TeacherSubjectVo>();
		for(TeacherSubject ts:list){
			List<LessonArrange> arranges=arrangeDao.findByClassIdAndSchoolYearAndTermAndSubject(ts.getSchoolClass().getClassId(), schoolYear, term, ts.getSubject());
			TeacherSubjectVo vo=TeacherSubjectVo.fromModel(ts);
			vo.setArranges(arranges);
			result.add(vo);
		}
		
		return result;
	}

	@Override
	public List<TeacherSubjectVo> findCurrentVoByCondition(ConditionVo cv) {
		// TODO Auto-generated method stub
		List<TeacherSubjectVo> result = new ArrayList<TeacherSubjectVo>();
		SchoolYear year = yearService.getCurrent();
		Term term = termService.getCurrent();
		cv.setYearId(year.getYearId());
		cv.setTermId(term.getTermId());
		//选择了年级未选择班级
		if (cv.getGradeId()!=0 && cv.getClassId() ==0) {
//					List<SchoolClass> classes = schoolClassService.findByYearAndGrade(year.getYearId(),cv.getGradeId());
//					for (SchoolClass schoolClass : classes) {
//						cv.setClassId(schoolClass.getClassId());
//						List<TeacherSubjectVo> list = teacherSubjectMapper.findByCondition(cv);
//						result.addAll(list);
//					}
			result=teacherSubjectMapper.findByCondition(cv);
		}else {
			result = teacherSubjectMapper.findByCondition(cv);
		}
//				for (TeacherSubjectVo teacherSubjectVo : result) {
//					SchoolClass schoolClass = schoolClassService.get(teacherSubjectVo.getClassId());
//					Teacher teacher = teacherService.get(teacherSubjectVo.getTeacherId());
//					teacherSubjectVo.setClassName(schoolClass.getName());
//					teacherSubjectVo.setTeacherNo(teacher.getTeacherNo());
//				}
		
		
		return result;
	}

	@Override
	public TeacherSubject findCurrentByClassAndSubject(SchoolClass schoolClass,
			Subject subject) {
		// TODO Auto-generated method stub
		SchoolYear schoolYear = yearService.getCurrent();
		Term term= termService.getCurrent();
		List<TeacherSubject> teacherSubjects = dao.findBySchoolClassAndSubjectAndYearAndTerm(schoolClass, subject, schoolYear, term);		
		if (teacherSubjects != null && teacherSubjects.size()>0) {
			return  teacherSubjects.get(0);
		}
		return null;
	}

	@Override
	public List<TeacherSubjectVo> findCurrentByGroup(int gradeId) {
		// TODO Auto-generated method stub
		ConditionVo cv = new ConditionVo();
		List<SchoolClass> schoolClasses = schoolClassService.findByGrade(gradeId);
		String[] classes = new String[schoolClasses.size()];
		for (int i = 0; i < schoolClasses.size(); i++) {
			classes[i] = schoolClasses.get(i).getClassId().toString();
		}
		SchoolYear schoolYear = yearService.getCurrent();
		Term term= termService.getCurrent();
		cv.setYearId(schoolYear.getYearId());
		cv.setTermId(term.getTermId());
		cv.setSchoolClasses(classes);
		return teacherSubjectMapper.findCurrentByGroup(cv);
	}

	@Override
	public List<TeacherSubject> findCurrent() {
		SchoolYear schoolYear = yearService.getCurrent();
		Term term= termService.getCurrent();
		
		return dao.findByYearAndTerm(schoolYear, term);
	}

	@Override
	public List<TeacherSubject> findCurrentByClass(SchoolClass schoolClass) {
		SchoolYear schoolYear = yearService.getCurrent();
		Term term= termService.getCurrent();
		return dao.findBySchoolClassAndYearAndTerm(schoolClass, schoolYear, term);
	}

	@Override
	public List<TeacherSubjectVo> findByGrade(int yearId, int termId, int gradeId) {
		return teacherSubjectMapper.findByGrade(yearId,termId,gradeId);
	}

	@Override
	public List<TeacherSubjectVo> findByTeacherAndGrade(int yearId, int termId, int gradeId, int teacherId) {
		// TODO Auto-generated method stub
		return teacherSubjectMapper.findByGradeAndTeacher(yearId, termId, gradeId, teacherId);
	}
	
	
}

