package com.zzy.pony.exam.service;




import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzy.pony.exam.dao.ExamineeDao;
import com.zzy.pony.exam.model.Examinee;
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
			//@todo 考生号==prefixNo+上次考试名次
			
			
		}
	 }
	 
	}

	
	
}
