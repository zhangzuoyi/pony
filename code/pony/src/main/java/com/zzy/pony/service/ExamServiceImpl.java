package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
	public void update(Exam sy, List<Integer> classIds, Integer[] subjectIds) {
		Exam old=dao.findOne(sy.getExamId());
		old.setName(sy.getName());
		old.setExamDate(sy.getExamDate());
		old.setType(sy.getType());
		old.getSchoolClasses().clear();
		old.getSchoolClasses().addAll(classDao.findByClassIdIn(classIds));
//		old.setSubject(sy.getSubject());
		old.setUpdateTime(sy.getUpdateTime());
		old.setUpdateUser(sy.getUpdateUser());
		
		List<ExamSubject> list=esDao.findByExam(old);
		for(ExamSubject es: list){//删除移除的对象
			boolean isFind=false;
			for(Integer sid: subjectIds){
				if(es.getSubject().getSubjectId() == sid){
					isFind=true;
					break;
				}
			}
			if(! isFind){
				esDao.delete(es);
			}
		}
		for(Integer sid: subjectIds){//保存新增的对象
			boolean isFind=false;
			for(ExamSubject es: list){
				if(sid == es.getSubject().getSubjectId()){
					isFind=true;
					break;
				}
			}
			if( ! isFind){
				ExamSubject newEs=new ExamSubject();
				newEs.setExam(old);
				Subject subject=new Subject();
				subject.setSubjectId(sid);
				newEs.setSubject(subject);
				newEs.setWeight(ExamSubject.DEFAULT_WEIGHT);
				esDao.save(newEs);
			}
		}
		
		dao.save(old);
	}

	@Override
	public void delete(int id) {
		Exam exam=dao.findOne(id);
		esDao.deleteByExam(exam);
		dao.delete(exam);
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

	@Override
	public List<ExamVo> findByYearAndTermOrderByExamDate(SchoolYear year,
			Term term) {
		// TODO Auto-generated method stub
		Sort sort = new Sort(Direction.DESC, "examDate");
		List<Exam> exams = dao.findBySchoolYearAndTermOrderByExamDate(year, term,sort);
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

	@Override
	public ExamVo getVo(int id) {
		Exam exam=dao.findOne(id);
		exam.getSchoolClasses().size();
		List<ExamSubject> list=esDao.findByExam(exam);
		List<ExamSubjectVo> result = new ArrayList<ExamSubjectVo>();
		for (ExamSubject es : list) {
			result.add(es.toVo());
		}
		ExamVo vo=exam.toVo();
		vo.setSubjects(result);
		return vo;
	}
}
