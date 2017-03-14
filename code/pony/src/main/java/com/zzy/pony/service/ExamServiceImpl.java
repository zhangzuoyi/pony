package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.ExamDao;
import com.zzy.pony.dao.ExamSubjectDao;
import com.zzy.pony.dao.SchoolClassDao;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.ExamSubject;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.ExamSubjectVo;
import com.zzy.pony.vo.ExamVo;
@Service
@Transactional
public class ExamServiceImpl implements ExamService {
	@Autowired
	private ExamDao dao;
	@Autowired
	private SchoolClassDao classDao;
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private TermService termService;
	@Autowired
	private ExamSubjectDao esDao;

	@Override
	public void add(Exam sy, Integer[] subjectIds) {
		dao.save(sy);
		for(Integer subjectId: subjectIds){
			ExamSubject es=new ExamSubject();
			Subject sub=new Subject();
			sub.setSubjectId(subjectId);
			es.setExam(sy);
			es.setSubject(sub);
			es.setWeight(ExamSubject.DEFAULT_WEIGHT);//暂时先设为默认值
			esDao.save(es);
		}
	}

	@Override
	public List<Exam> findAll() {
		List<Exam> list=dao.findAll();
		for(Exam exam: list){
			exam.getSchoolClasses().size();
		}
		return list;
	}

	@Override
	public Exam get(int id) {
		Exam exam=dao.findOne(id);
		exam.getSchoolClasses().size();
		return exam;
	}

	@Override
	public void update(Exam sy, List<Integer> classIds) {
		Exam old=dao.findOne(sy.getExamId());
		old.setName(sy.getName());
		old.getSchoolClasses().clear();
		old.getSchoolClasses().addAll(classDao.findByClassIdIn(classIds));
//		old.setSubject(sy.getSubject());
		old.setUpdateTime(sy.getUpdateTime());
		old.setUpdateUser(sy.getUpdateUser());
		
		dao.save(old);
	}

	@Override
	public void delete(int id) {
		dao.delete(id);
		
	}

	@Override
	public List<Exam> findBySubject(Subject subject) {
//		return dao.findBySubject(subject);
		return null;
	}

	@Override
	public List<Exam> findCurrentBySubjectAndClass(Subject subject, SchoolClass schoolClass) {
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		
//		return dao.findBySchoolYearAndTermAndSubjectAndSchoolClasses(year, term, subject, schoolClass);
		return null;
	}

	@Override
	public List<ExamVo> findByYearAndTerm(SchoolYear year, Term term) {
		List<Exam> exams = dao.findBySchoolYearAndTerm(year, term);
		List<ExamSubject> subjects = esDao.findByExamIn(exams);
		List<ExamVo> result = new ArrayList<ExamVo>();
		for (Exam exam : exams) {
			exam.getSchoolClasses().size();
			ExamVo vo = exam.toVo();
			vo.setSubjects(getSubjectVos(vo.getExamId(), subjects));
			result.add(vo);
		}
		return result;
	}

	private List<ExamSubjectVo> getSubjectVos(Integer examId, List<ExamSubject> subjects) {
		List<ExamSubjectVo> result = new ArrayList<ExamSubjectVo>();
		for (ExamSubject es : subjects) {
			if (examId == es.getExam().getExamId()) {
				result.add(es.toVo());
			}
		}
		return result;
	}
}
