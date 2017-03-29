package com.zzy.pony.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.model.Student;
import com.zzy.pony.vo.StudentCard;
@Service
public class StudentCardServiceImpl implements StudentCardService {
	@Autowired
	private StudentService studentService;
	@Autowired
	private PrizePunishService ppService;
	@Autowired
	private StudentRemarkService remarkService;
	@Autowired
	private ExamResultService resultService;

	@Override
	public StudentCard get(Integer studentId) {
		StudentCard card=new StudentCard();
		Student student=studentService.get(studentId);
		card.setStudent(student);
		card.setPps(ppService.findByStudent(studentId));
		card.setRemarks(remarkService.findByStudent(studentId));
		card.setResults(resultService.findByStudent(studentId));
		
		return card;
	}

}
