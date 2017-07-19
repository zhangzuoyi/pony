package com.zzy.pony.exam.service;




import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzy.pony.exam.dao.ExamineeDao;
import com.zzy.pony.exam.mapper.ExamineeMapper;
import com.zzy.pony.exam.model.Examinee;
import com.zzy.pony.exam.vo.ExamineeVo;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.Student;
import com.zzy.pony.service.ExamService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.StudentService;




@Service
@Transactional
public class ExamineeServiceImpl implements ExamineeService {

	@Autowired
	private SchoolClassService schoolClassService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private ExamineeDao examineeDao;
	@Autowired
	private ExamService examService;
	@Autowired
	private ExamineeMapper examineeMapper;
	
	
	
	@Override
	public void generateNo(int examId, int gradeId, String prefixNo, int bitNo) {
		// TODO Auto-generated method stub
	 List<Examinee> examinees = new ArrayList<Examinee>();
	 Exam exam = examService.get(examId);
	 List<SchoolClass> schoolClasses = schoolClassService.findByGrade(gradeId);
	 for (SchoolClass schoolClass : schoolClasses) {
		List<Student> students = studentService.findBySchoolClass(schoolClass.getClassId());
		for (Student student : students) {
			Examinee examinee = new Examinee();
			examinee.setExam(exam);
			examinee.setStudent(student);
			//@todo 考生号==prefixNo+上次考试名次(如何确定?)
			examinee.setRegNo("test");
			examinees.add(examinee);
		}
	 }
	 examineeDao.save(examinees);
	}



	@Override
	public Page<ExamineeVo> findPageByClass(int currentPage, int pageSize,
			int examId, int classId) {
		// TODO Auto-generated method stub
		List<ExamineeVo> content = examineeMapper.findPageByClass(currentPage*pageSize, pageSize, examId, classId);
		Pageable pageable = new PageRequest(currentPage, pageSize);
		Integer total = examineeMapper.findCountByClass(examId, classId);
		Page<ExamineeVo> result = new PageImpl<ExamineeVo>(content, pageable, total);
		return result;
	}

	

	@Override
	public Page<ExamineeVo> findPageByClassAndArrange(int currentPage,
			int pageSize, int examId, int classId, int arrangeId) {
		// TODO Auto-generated method stub
		List<ExamineeVo> content = examineeMapper.findPageByClassAndArrange(currentPage*pageSize, pageSize, examId, classId, arrangeId);
		Pageable pageable = new PageRequest(currentPage, pageSize);
		Integer total = examineeMapper.findCountByClassAndArrange(examId, classId, arrangeId);
		Page<ExamineeVo> result = new PageImpl<ExamineeVo>(content, pageable, total);
		return result;
	}



	@Override
	public List<Examinee> findByExamIdAndClassId(int examId, int classId) {
		// TODO Auto-generated method stub			
		return examineeMapper.findByExamIdAndClassId(examId, classId);
	}



	@Override
	public List<Examinee> findByArrangeId(int arrangeId) {
		// TODO Auto-generated method stub
		return examineeMapper.findByArrangeId(arrangeId);
	}
	
	
	
	
	
	
	

	
	
}
