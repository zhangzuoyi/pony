package com.zzy.pony.exam.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.zzy.pony.model.Subject;
import com.zzy.pony.service.ExamService;
import com.zzy.pony.service.GradeService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.util.DateTimeUtil;

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
	@SuppressWarnings("deprecation")
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
			if (ea.getEndTime()!= null) {
				vo.setEndTime(ea.getEndTime().getHours() + ":" + String.format("%02d", ea.getEndTime().getMinutes()) + ":" + String.format("%02d", ea.getEndTime().getSeconds()));
			}
			vo.setExamDate(ea.getExamDate());
			if (ea.getStartTime()!=null) {
				vo.setStartTime(ea.getStartTime().getHours() + ":" + String.format("%02d", ea.getStartTime().getMinutes()) + ":" + String.format("%02d", ea.getStartTime().getSeconds()));
			}
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
            if (ea.getExamRoomAllocates()!= null) {
				int count = 0;
            	for (ExamRoomAllocate era : ea.getExamRoomAllocates()) {
            		count += era.getCapacity();
				}
            vo.setExamineeTotal(count);
			}
			content.add(vo);
			
		}
		result = new PageImpl<ExamArrangeVo>(content, pageable, examArrangePage.getTotalElements());							
		return result;
		
	}

	@RequestMapping(value="add",method=RequestMethod.GET)
	@ResponseBody
	public void add(@RequestParam(value="subjects[]") int[] subjects,@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId){
			examArrangeService.add(subjects,examId,gradeId);
	}
	@RequestMapping(value="addExamDate",method=RequestMethod.GET)
	@ResponseBody
	public void addExamDate(@RequestParam(value="examArranges[]") int[] examArranges,@RequestParam(value="examDate") String examDate ){
		Date date = DateTimeUtil.strToDate(examDate, DateTimeUtil.FORMAL_FORMAT);
   
		examArrangeService.addExamDate(examArranges, date);
	}

	@RequestMapping(value="addExamTime",method=RequestMethod.GET)
	@ResponseBody
	public void addExamTime(@RequestParam(value="examArranges[]") int[] examArranges,
			@RequestParam(value="startTime") String startTime,
			@RequestParam(value="endTime") String endTime){
			Date startTimeDate = DateTimeUtil.strToDate(startTime, DateTimeUtil.TIME_FORMAT);
			Date endTimeDate = DateTimeUtil.strToDate(endTime, DateTimeUtil.TIME_FORMAT);
		    examArrangeService.addExamTime(examArranges, startTimeDate,endTimeDate);
	}
	@RequestMapping(value="addGroup",method=RequestMethod.GET)
	@ResponseBody
	public void addGroup(@RequestParam(value="examArranges[]") int[] examArranges,
			@RequestParam(value="groupName") String groupName,
			@RequestParam(value="examId") String examId,
			@RequestParam(value="gradeId") String gradeId){
			  
		examArrangeService.addGroup(examArranges, groupName, gradeId, examId);				
	}
	@RequestMapping(value="delete",method=RequestMethod.GET)
	@ResponseBody
	public void delete(@RequestParam(value="examArranges[]") int[] examArranges
			){
		for (int arrangeId : examArranges) {
			examArrangeService.delete(arrangeId);
		}				
	}
	
	@RequestMapping(value="findByExam",method = RequestMethod.GET)
	@ResponseBody
	public List<Subject> findByExam(@RequestParam(value="examId") int examId){
		List<Subject> result = new ArrayList<Subject>();				
		List<ExamArrange> examArranges = examArrangeService.findByExam(examId);
		for (ExamArrange examArrange : examArranges) {
			result.add(examArrange.getSubject());
		}
		return result;
	}
	
	
}
