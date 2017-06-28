package com.zzy.pony.exam.service;


import com.zzy.pony.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzy.pony.exam.dao.ExamArrangeDao;
import com.zzy.pony.exam.model.ExamArrange;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.Grade;
import com.zzy.pony.service.ExamService;
import com.zzy.pony.service.GradeService;


@Service
@Transactional
public class ExamArrangeServiceImpl implements ExamArrangeService {

	@Autowired
	private ExamArrangeDao examArrangeDao;
	@Autowired
	private ExamService examService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private SubjectService subjectService;
	
	
	
	@Override
	public Page<ExamArrange> findByExamAndGrade(Pageable pageable,int examId,int gradeId) {
		// TODO Auto-generated method stub
		Exam exam = examService.get(examId);
		Grade grade = gradeService.get(gradeId);
		return examArrangeDao.findByExamAndGrade(pageable,exam,grade);
	}

	@Override
	public Page<ExamArrange> findByExam(Pageable pageable, int examId) {
		Exam exam = examService.get(examId);
		return examArrangeDao.findByExam(pageable,exam);
	}

	@Override
	public Page<ExamArrange> findByGrade(Pageable pageable, int gradeId) {
		Grade grade = gradeService.get(gradeId);
		return examArrangeDao.findByGrade(pageable,grade);
	}

	@Override
	public Page<ExamArrange> findAll(Pageable pageable) {
		return examArrangeDao.findAll(pageable);
	}

	@Override
	public void add(int[] subjects) {
		if (subjects.length>0){
			for (int subjectId:
					subjects) {
				ExamArrange examArrange = new ExamArrange();
				examArrange.setSubject(subjectService.get(subjectId));
				examArrangeDao.save(examArrange);
			}

		}
	}
}
