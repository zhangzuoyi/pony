package com.zzy.pony.exam.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zzy.pony.exam.model.ExamArrange;
import com.zzy.pony.exam.model.ExamRoomAllocate;
import com.zzy.pony.exam.model.Examinee;
import com.zzy.pony.exam.service.ExamArrangeService;
import com.zzy.pony.exam.vo.ExamArrangeVo;


@Controller
@RequestMapping(value="/examAdmin/examArrange")
public class ExamArrangeController {
	
	@Autowired
	private ExamArrangeService examArrangeService;
	
	@RequestMapping(value="main",method=RequestMethod.GET)
	public String main(Model model){
		return "examAdmin/examArrange/main";
	}
	@RequestMapping(value="listPage",method=RequestMethod.GET)
	public Page<ExamArrangeVo> listPage(@RequestParam(value="currentPage",defaultValue="0") int currentPage,
			@RequestParam(value="pageSize",defaultValue="20") int pageSize,
			@RequestParam(value="examId") int examId,
			@RequestParam(value="gradeId") int gradeId
			){
		Pageable pageable=new PageRequest(currentPage, pageSize,Direction.ASC,"arrangeId");
		Page<ExamArrangeVo> result = null;
		Page<ExamArrange> examArrangePage = examArrangeService.findByExamAndGrade(pageable,examId,gradeId);
		List<ExamArrangeVo> content = new ArrayList<ExamArrangeVo>();
		for (ExamArrange ea : examArrangePage) {
			ExamArrangeVo vo = new ExamArrangeVo();
			vo.setArrangeId(ea.getArrangeId());
			vo.setEndTime(ea.getEndTime());
			vo.setExamDate(ea.getExamDate());
			vo.setStartTime(ea.getStartTime());
			vo.setExamId(ea.getExam().getExamId());
			vo.setExamName(ea.getExam().getName());
			if (ea.getExaminees()!= null && !ea.getExaminees().isEmpty()) {
				List<Integer> examineeIds = new ArrayList<Integer>();
				List<String> examineeNames = new ArrayList<String>();
				for (Examinee examinee : ea.getExaminees()) {
					examineeIds.add(examinee.getExamineeId());
					examineeNames.add(examinee.getStudent().getName());
				}
				vo.setExamineeIds(examineeIds);
				vo.setExamineeNames(examineeNames);
			}
			if (ea.getExamRoomAllocates()!= null && !ea.getExamRoomAllocates().isEmpty()) {
				List<Integer> examRoomIds = new ArrayList<Integer>();
				List<String> examRoomNames = new ArrayList<String>();
				for (ExamRoomAllocate examRoomAllocate : ea.getExamRoomAllocates()) {
					examRoomIds.add(examRoomAllocate.getRoomId());
					examRoomNames.add(examRoomAllocate.getRoomName());
				}
				vo.setExamRoomIds(examRoomIds);
				vo.setExamRoomNames(examRoomNames);
			}
			vo.setGradeId(ea.getGrade().getGradeId());
			vo.setGradeName(ea.getGrade().getName());
			vo.setGroupName(ea.getGroup().getName());
			vo.setSubjectId(ea.getSubject().getSubjectId());
			vo.setSubjectName(ea.getSubject().getName());
			content.add(vo);
			
		}
		result = new PageImpl<ExamArrangeVo>(content, pageable, examArrangePage.getTotalElements());							
		return result;
		
	}
	
	
}
