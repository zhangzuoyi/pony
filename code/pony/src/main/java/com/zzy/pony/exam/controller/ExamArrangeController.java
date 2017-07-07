package com.zzy.pony.exam.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zzy.pony.model.Exam;
import com.zzy.pony.model.Grade;
import com.zzy.pony.service.ExamService;
import com.zzy.pony.service.GradeService;
import com.zzy.pony.service.SubjectService;

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

import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value="/examAdmin/examArrange")
public class ExamArrangeController {
	
	@Autowired
	private ExamArrangeService examArrangeService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private ExamService examService;
	@Autowired
	private GradeService gradeService;

	
	@RequestMapping(value="main",method=RequestMethod.GET)
	public String main(Model model){
		return "examAdmin/examArrange/main";
	}
	@RequestMapping(value="listPage",method=RequestMethod.GET)
    @ResponseBody
	public Page<ExamArrangeVo> listPage(@RequestParam(value="currentPage",defaultValue="0") int currentPage,
			@RequestParam(value="pageSize",defaultValue="20") int pageSize,
			@RequestParam(value="examId") String examId,
			@RequestParam(value="gradeId") String gradeId
			){
		Pageable pageable=new PageRequest(currentPage, pageSize,Direction.ASC,"arrangeId");
		Page<ExamArrangeVo> result = null;
        Page<ExamArrange> examArrangePage = null;
		if (!("".equalsIgnoreCase(examId)||"".equalsIgnoreCase(gradeId))){
            examArrangePage  = examArrangeService.findByExamAndGrade(pageable,Integer.valueOf(examId),Integer.valueOf(gradeId));
        }else if (!"".equalsIgnoreCase(examId)&&"".equalsIgnoreCase(gradeId)){
            examArrangePage  = examArrangeService.findByExam(pageable,Integer.valueOf(examId));
        }else if ("".equalsIgnoreCase(examId)&&!"".equalsIgnoreCase(gradeId)){
            examArrangePage  = examArrangeService.findByGrade(pageable,Integer.valueOf(gradeId));
        }else{
            examArrangePage  = examArrangeService.findAll(pageable);
        }
		List<ExamArrangeVo> content = new ArrayList<ExamArrangeVo>();
		for (ExamArrange ea : examArrangePage) {
			ExamArrangeVo vo = new ExamArrangeVo();
			vo.setArrangeId(ea.getArrangeId());
			vo.setEndTime(ea.getEndTime());
			vo.setExamDate(ea.getExamDate());
			vo.setStartTime(ea.getStartTime());
			if (ea.getExam()!= null){
                vo.setExamId(ea.getExam().getExamId());
                vo.setExamName(ea.getExam().getName());
            }
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
			if (ea.getGrade()!= null){
                vo.setGradeId(ea.getGrade().getGradeId());
                vo.setGradeName(ea.getGrade().getName());
            }
            if (ea.getGroup()!=null){
                vo.setGroupName(ea.getGroup().getName());
            }
            if (ea.getSubject()!=null){
                vo.setSubjectId(ea.getSubject().getSubjectId());
                vo.setSubjectName(ea.getSubject().getName());
            }
			content.add(vo);
			
		}
		result = new PageImpl<ExamArrangeVo>(content, pageable, examArrangePage.getTotalElements());							
		return result;
		
	}

	@RequestMapping(value="add",method=RequestMethod.GET)
	@ResponseBody
	public void add(@RequestParam(value="subjects[]") int[] subjects){
			examArrangeService.add(subjects);
	}
	@RequestMapping(value="addExamDate",method=RequestMethod.GET)
	@ResponseBody
	public void addExamDate(@RequestParam(value="examArranges[]") int[] examArranges,@RequestParam(value="examDate") Date examDate ){
		    examArrangeService.addExamDate(examArranges, examDate);
	}

	@RequestMapping(value="addExamTime",method=RequestMethod.GET)
	@ResponseBody
	public void addExamTime(@RequestParam(value="examArranges[]") int[] examArranges,
			@RequestParam(value="startTime") Date startTime,
			@RequestParam(value="endTime") Date endTime){
		    examArrangeService.addExamTime(examArranges, startTime,endTime);
	}
	@ResponseBody
	public void addGroup(@RequestParam(value="examArranges[]") int[] examArranges,
			@RequestParam(value="groupName") String groupName,
			@RequestParam(value="examId") int examId,
			@RequestParam(value="gradeId") int gradeId){
		Exam exam = examService.get(examId);
		Grade grade = gradeService.get(gradeId);
		examArrangeService.addGroup(examArranges, groupName, grade, exam);				
	}
	@RequestMapping(value="delete",method=RequestMethod.GET)
	@ResponseBody
	public void delete(@RequestParam(value="examArranges[]") int[] examArranges
			){
		for (int arrangeId : examArranges) {
			examArrangeService.delete(arrangeId);
		}				
	}
	
	
}
