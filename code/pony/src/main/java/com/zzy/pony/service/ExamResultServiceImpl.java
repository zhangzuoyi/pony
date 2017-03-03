package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.ExamDao;
import com.zzy.pony.dao.ExamResultDao;
import com.zzy.pony.dao.StudentDao;
import com.zzy.pony.mapper.ExamResultMapper;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.ExamResult;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.Student;
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

	@Override
	public List<ExamResultVo> findByClass(Integer examId, Integer classId) {
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
			vo.setSubjectName(exam.getSubject().getName());
			vo.setScore(0f);
			vos.add(vo);
		}
		List<ExamResultVo> results=mapper.find(examId, classId);
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
	public void batchSave(Map<Student, Float> scores, Integer examId, String loginName) {
		Exam exam=new Exam();
		exam.setExamId(examId);
		Date now=new Date();
		for(Map.Entry<Student, Float> entry: scores.entrySet()){
			ExamResult result=dao.findByExamAndStudent(exam, entry.getKey());
			if(result == null){
				result=new ExamResult();
				result.setCreateTime(now);
				result.setCreateUser(loginName);
				result.setExam(exam);
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

}
