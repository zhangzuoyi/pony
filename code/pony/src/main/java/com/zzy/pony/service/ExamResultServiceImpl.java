package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import antlr.LexerSharedInputState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.ExamDao;
import com.zzy.pony.dao.ExamResultDao;
import com.zzy.pony.dao.StudentDao;
import com.zzy.pony.dao.SubjectDao;
import com.zzy.pony.mapper.ExamResultMapper;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.ExamResult;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.Student;
import com.zzy.pony.model.Subject;
import com.zzy.pony.vo.ExamResultVo;
@Service
@Transactional
public class ExamResultServiceImpl implements ExamResultService {
	@Autowired
	private ExamResultMapper mapper;
	@Autowired
	private StudentDao studentDao;
	@Autowired
	private ExamDao examDao;
	@Autowired
	private ExamResultDao dao;
	@Autowired
	private SubjectDao subjectDao;
	@Autowired
	private SubjectService subjectService;

	@Override
	public List<ExamResultVo> findByClass(Integer examId, Integer classId, Integer subjectId) {
		Subject subject=subjectDao.findOne(subjectId);
		Exam exam=examDao.findOne(examId);
		List<ExamResultVo> vos=new ArrayList<ExamResultVo>();
		SchoolClass sc=new SchoolClass();
		sc.setClassId(classId);
		List<Student> students=studentDao.findBySchoolClass(sc);
		for(Student stu: students){
			ExamResultVo vo=new ExamResultVo();
			vo.setStudentId(stu.getStudentId());
			vo.setStudentName(stu.getName());
			vo.setStudentNo(stu.getStudentNo());
			vo.setExamId(examId);
			vo.setExamName(exam.getName());
			vo.setSubjectName(subject.getName());
			vo.setSubjectId(subjectId);
			vo.setScore(0f);
			vos.add(vo);
		}
		List<ExamResultVo> results=mapper.find(examId, classId, subjectId);
		for(ExamResultVo vo: vos){
			for(ExamResultVo result: results){
				if(result.getStudentId().equals(vo.getStudentId())){
					vo.setScore(result.getScore());
					vo.setResultId(result.getResultId());
					break;
				}
			}
		}
		return vos;
	}

	@Override
	public void save(List<ExamResultVo> vos) {
		for(ExamResultVo vo: vos){
			if(vo.getResultId() == null || vo.getResultId() == 0){//新增
				ExamResult er=new ExamResult();
				er.setCreateTime(new Date());
				er.setCreateUser("test");
				Exam exam=new Exam();
				exam.setExamId(vo.getExamId());
				er.setExam(exam);
				er.setScore(vo.getScore());
				Student student=new Student();
				student.setStudentId(vo.getStudentId());
				er.setStudent(student);
				Subject subject=new Subject();
				subject.setSubjectId(vo.getSubjectId());
				er.setSubject(subject);
				er.setUpdateTime(new Date());
				er.setUpdateUser("test");
				dao.save(er);
			}else{//修改
				ExamResult er=dao.findOne(vo.getResultId());
				er.setScore(vo.getScore());
				er.setUpdateTime(new Date());
				er.setUpdateUser("test");
				dao.save(er);
			}
			
		}
		
	}

	@Override
	public void batchSave(Map<Student, Float> scores, Integer examId,Integer subjectId, String loginName) {
		Exam exam=new Exam();
		exam.setExamId(examId);
		Subject subject=new Subject();
		subject.setSubjectId(subjectId);
		Date now=new Date();
		for(Map.Entry<Student, Float> entry: scores.entrySet()){
			ExamResult result=dao.findByExamAndSubjectAndStudent(exam, subject, entry.getKey());
			if(result == null){
				result=new ExamResult();
				result.setCreateTime(now);
				result.setCreateUser(loginName);
				result.setExam(exam);
				result.setSubject(subject);
				result.setScore(entry.getValue());
				result.setStudent(entry.getKey());
				result.setUpdateTime(now);
				result.setUpdateUser(loginName);
			}else{
				result.setScore(entry.getValue());
				result.setUpdateTime(now);
				result.setUpdateUser(loginName);
			}
			dao.save(result);
		}
		
	}

	@Override
	public List<ExamResultVo> findByStudent(Integer studentId) {
		return mapper.findByStudent(studentId);
	}

	@Override
	public List<Subject> findSubjectByExam(int examId) {
		List<Subject> result = new ArrayList<Subject>();
		List<Integer> subjectIds = mapper.findSubjectByExam(examId);
		for (int subjectId:
			 subjectIds) {
			Subject subject = subjectService.get(subjectId);
			result.add(subject);
		}
		return result;
	}

	@Override
	public void uploadAll(Integer examId, List<ExamResultVo> resultList, String loginName) {
		//先删除此考试所有结果
		mapper.deleteByExam(examId);
		//插入结果
		for(ExamResultVo vo: resultList) {
			mapper.insert(examId, vo.getStudentId(), vo.getScore(), vo.getSubjectId(), loginName);
		}
		//生成排名 TODO
		
	}
}


