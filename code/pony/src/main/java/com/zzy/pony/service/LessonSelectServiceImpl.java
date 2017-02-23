package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.LessonSelectStudentDao;
import com.zzy.pony.dao.StudentDao;
import com.zzy.pony.model.LessonSelectArrange;
import com.zzy.pony.model.LessonSelectStudent;
import com.zzy.pony.model.Student;
import com.zzy.pony.vo.LessonSelectArrangeVo;
@Service
@Transactional
public class LessonSelectServiceImpl implements LessonSelectService {
	@Autowired
	private StudentDao studentDao;
	@Autowired
	private LessonSelectArrangeService arrangeService;
	@Autowired
	private LessonSelectStudentDao dao;

	@Override
	public List<LessonSelectArrangeVo> lessonForStudentSelect(Integer studentId) {
		Student student=studentDao.findOne(studentId);
		List<LessonSelectArrangeVo> allLessons=arrangeService.findCurrentByGrade(student.getSchoolClass().getGrade().getGradeId());
		List<Integer> selectIds=dao.findArrangeIdsByStudent(student);
		List<LessonSelectArrangeVo> result=new ArrayList<LessonSelectArrangeVo>();
		for(LessonSelectArrangeVo vo: allLessons){
			if( ! selectIds.contains(vo.getArrangeId())){
				result.add(vo);
			}
		}
		return result;
	}

	@Override
	public void selectLesson(Integer studentId, Integer arrangeId) {
		Student student=new Student();
		student.setStudentId(studentId);
		LessonSelectArrange arrange=new LessonSelectArrange();
		arrange.setArrangeId(arrangeId);
		LessonSelectStudent ss=new LessonSelectStudent();
		ss.setLessonSelectArrange(arrange);
		ss.setStudent(student);
		dao.save(ss);
	}

	@Override
	public List<LessonSelectArrangeVo> lessonStudentSelected(Integer studentId) {
		Student student=studentDao.findOne(studentId);
		List<LessonSelectArrangeVo> allLessons=arrangeService.findCurrentByGrade(student.getSchoolClass().getGrade().getGradeId());
		List<Integer> selectIds=dao.findArrangeIdsByStudent(student);
		List<LessonSelectArrangeVo> result=new ArrayList<LessonSelectArrangeVo>();
		for(LessonSelectArrangeVo vo: allLessons){
			if( selectIds.contains(vo.getArrangeId())){
				result.add(vo);
			}
		}
		return result;
	}

	@Override
	public void deleteSelect(Integer studentId, Integer arrangeId) {
		Student student=new Student();
		student.setStudentId(studentId);
		LessonSelectArrange arrange=new LessonSelectArrange();
		arrange.setArrangeId(arrangeId);
		LessonSelectStudent ss=dao.findByStudentAndLessonSelectArrange(student, arrange);
		dao.delete(ss);
	}

}
