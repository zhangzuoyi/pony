package com.zzy.pony.service;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.AutoClassArrange.DNA;
import com.zzy.pony.AutoClassArrange.GeneticAlgorithm;
import com.zzy.pony.util.GAUtil;
import com.zzy.pony.vo.TeacherSubjectVo;


@Service
@Transactional
public class AutoLessonArrangeServiceImpl implements AutoLessonArrangeService {

	@Autowired
	private TeacherSubjectService teacherSubjectService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private SubjectService subjectService;
	
	@Override
	public void autoLessonArrange() {
		// TODO Auto-generated method stub
	
		String[] classIdCandidate =   GAUtil.getCandidateStrings(teacherSubjectService.findCurrentAllClassId(), 3);    
		String[] subjectIdCandidate =GAUtil.getCandidateStrings(teacherService.findAllTeacherId(), 2);    
		String[] teacherIdCandidate =GAUtil.getCandidateStrings(subjectService.findAllSubjectId(), 4); 
		String[] weekdayIdCandidate ={"1","2"};
		String[] seqIdCandidate={"1","2"};
		List<TeacherSubjectVo> vos = teacherSubjectService.findCurrentAll();		
		DNA.getInstance().setClassIdCandidate(classIdCandidate);
		DNA.getInstance().setSeqIdCandidate(seqIdCandidate);
		DNA.getInstance().setSubjectIdCandidate(subjectIdCandidate);
		DNA.getInstance().setTeacherIdCandidate(teacherIdCandidate);
		DNA.getInstance().setWeekdayIdCandidate(weekdayIdCandidate);
		DNA.getInstance().setTeacherSubjectweekArrange(GAUtil.getTeacherSubjectweekArrange(vos));
		GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
		geneticAlgorithm.caculte();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
