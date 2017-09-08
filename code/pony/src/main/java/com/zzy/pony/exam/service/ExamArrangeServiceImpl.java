package com.zzy.pony.exam.service;


import java.util.*;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.zzy.pony.exam.mapper.ExamArrangeMapper;
import com.zzy.pony.model.Subject;
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
	@Autowired
	private ExamArrangeMapper examArrangeMapper;
	
	
	
	@Override
	public Page<ExamArrange> findByExamAndGrade(Pageable pageable,int examId,int gradeId) {
		Exam exam = examService.get(examId);
		Grade grade = gradeService.get(gradeId);
		return examArrangeDao.findByExamAndGrade(pageable,exam,grade);
	}
	public List<ExamArrange> findByExamAndGrade(int examId,int gradeId){
		Exam exam = examService.get(examId);
		Grade grade = gradeService.get(gradeId);
		return examArrangeDao.findByExamAndGrade(exam,grade);
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
	public void add(int[] subjects,int examId,int gradeId) {
		Exam exam = examService.get(examId);
		Grade grade = gradeService.get(gradeId);
		if (subjects.length>0){
			for (int subjectId:
					subjects) {
				ExamArrange examArrange = new ExamArrange();
				examArrange.setSubject(subjectService.get(subjectId));
				examArrange.setExam(exam);
				examArrange.setGrade(grade);
				examArrangeDao.save(examArrange);
			}

		}
	}

	@Override
	public void addExamDate(int[] examArranges, Date examDate) {
		// TODO Auto-generated method stub
		List<Integer> examArrangeIds = new ArrayList<Integer>();
		Map<String,Object> map = new HashMap<String, Object>();
		if (examArranges.length>0) {
			for (int arrangeId : examArranges) {
				examArrangeIds.add(arrangeId);

				/*ExamArrange examArrange = examArrangeDao.findOne(arrangeId);
				examArrange.setExamDate(examDate);
				examArrangeDao.save(examArrange);*/
			}
			map.put("examDate",examDate);
			map.put("examArrangeIds",examArrangeIds);
			examArrangeMapper.updateExamDate(map);

		}
	}

	@Override
	public void addExamTime(int[] examArranges, Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String, Object>();
		List<Integer> examArrangeIds = new ArrayList<Integer>();
		if (examArranges.length>0) {
			for (int arrangeId : examArranges) {

				examArrangeIds.add(arrangeId);
				/*ExamArrange examArrange = examArrangeDao.findOne(arrangeId);
				examArrange.setStartTime(startTime);
				examArrange.setEndTime(endTime);
				examArrangeDao.save(examArrange);*/
			}
			map.put("startTime",startTime);
			map.put("endTime",endTime);
			map.put("examArrangeIds",examArrangeIds);
			examArrangeMapper.updateExamTime(map);
		}
	}

	@Override
	public void addGroup(int[] examArranges, String groupName, String gradeId,
			String examId) {
		// TODO Auto-generated method stub
        ExamArrangeGroup examArrangeGroup = new ExamArrangeGroup();

        if (examId != null && !"".equalsIgnoreCase(examId)) {
            examArrangeGroup.setExam(examService.get(Integer.valueOf(examId)));
		}
		if (gradeId != null && !"".equalsIgnoreCase(gradeId)) {
            examArrangeGroup.setGrade(gradeService.get(Integer.valueOf(gradeId)));
		}
		List<ExamArrange> list = new ArrayList<ExamArrange>();
		for (int arrangeId : examArranges) {
			ExamArrange examArrange = examArrangeDao.findOne(arrangeId);
			examArrange.setGroup(examArrangeGroup);
			list.add(examArrange);
		}
		examArrangeGroup.setName(groupName);
		examArrangeGroup.setExamArranges(list);
		examArrangeGroupDao.save(examArrangeGroup);



    }

	@Override
	public void delete(int arrangeId) {
		// TODO Auto-generated method stub	
		examArrangeDao.delete(arrangeId);
	}

	@Override
	public ExamArrange findByExamAndGradeAndSubject(int examId, int gradeId, int subjectId) {
		Exam exam = examService.get(examId);
		Grade grade = gradeService.get(gradeId);
		Subject subject = subjectService.get(subjectId);
		List<ExamArrange> list = examArrangeDao.findByExamAndGradeAndSubject(exam,grade,subject);
		if (list != null && list.size()>0)
		{
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<ExamArrange> findByExam(int examId) {
		// TODO Auto-generated method stub
		Exam exam = examService.get(examId);		
		return examArrangeDao.findByExam(exam);
	}

	@Override
	public List<ExamArrangeGroup> findByExamAndGroup(int examId, int gradeId) {
		// TODO Auto-generated method stub
		Exam exam = examService.get(examId);
		Grade grade = gradeService.get(gradeId);
		return examArrangeGroupDao.findByExamAndGrade(exam, grade);
	}

	@Override
	public ExamArrange get(int arrangeId) {
		// TODO Auto-generated method stub
		return examArrangeDao.findOne(arrangeId);
	}

	@Override
	public List<ExamArrange> findByExamAndGroupIsNull(int examId) {
		// TODO Auto-generated method stub
		Exam exam = examService.get(examId);
		return examArrangeDao.findByExamAndGroupIsNull(exam);
	}
	
	
	
	
	
	
	
	
}
