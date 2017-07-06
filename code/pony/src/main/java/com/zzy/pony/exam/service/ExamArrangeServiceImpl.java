package com.zzy.pony.exam.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zzy.pony.service.SubjectService;

import org.apache.commons.collections.iterators.ArrayListIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzy.pony.exam.dao.ExamArrangeDao;
import com.zzy.pony.exam.dao.ExamArrangeGroupDao;
import com.zzy.pony.exam.model.ExamArrange;
import com.zzy.pony.exam.model.ExamArrangeGroup;
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
	@Autowired
	private ExamArrangeGroupDao examArrangeGroupDao;
	
	
	
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

	@Override
	public void addExamDate(int[] examArranges, Date examDate) {
		// TODO Auto-generated method stub
		if (examArranges.length>0) {
			for (int arrangeId : examArranges) {
				ExamArrange examArrange = examArrangeDao.findOne(arrangeId);
				examArrange.setExamDate(examDate);
				examArrangeDao.save(examArrange);
			}
		}
	}

	@Override
	public void addExamTime(int[] examArranges, Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		if (examArranges.length>0) {
			for (int arrangeId : examArranges) {
				ExamArrange examArrange = examArrangeDao.findOne(arrangeId);
				examArrange.setStartTime(startTime);
				examArrange.setEndTime(endTime);
				examArrangeDao.save(examArrange);
			}
		}
	}

	@Override
	public void addGroup(int[] examArranges, String groupName, Grade grade,
			Exam exam) {
		// TODO Auto-generated method stub
		ExamArrangeGroup examArrangeGroup = new ExamArrangeGroup();
		List<ExamArrange> list = new ArrayList<ExamArrange>();
		for (int arrangeId : examArranges) {
			ExamArrange examArrange = examArrangeDao.findOne(arrangeId);
			list.add(examArrange);
		}
		examArrangeGroup.setExam(exam);
		examArrangeGroup.setExamArranges(list);
		examArrangeGroup.setGrade(grade);
		examArrangeGroup.setName(groupName);
		examArrangeGroupDao.save(examArrangeGroup);
	}

	@Override
	public void delete(int arrangeId) {
		// TODO Auto-generated method stub
		examArrangeDao.delete(arrangeId);
	}
	
	
	
	
	
	
}
