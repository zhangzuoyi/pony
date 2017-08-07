package com.zzy.pony.exam.controller;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.iterators.ArrayListIterator;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.exam.service.ExamineeRoomArrangeService;
import com.zzy.pony.vo.ExamineeRoomArrangeVo;



@Controller
@RequestMapping(value="/examAdmin/examineeRoomArrange")
public class ExamineeRoomArrangeController {
	
	@Autowired
	private ExamineeRoomArrangeService examineeRoomArrangeService;

	
	@RequestMapping(value="main",method=RequestMethod.GET)
	public String main(Model model){
		return "examAdmin/examineeRoomArrange/main";
	}
	
	@RequestMapping(value="autoExamineeRoomArrange",method=RequestMethod.GET)
	@ResponseBody
	public void autoExamineeRoomArrange(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId){		
		examineeRoomArrangeService.autoExamineeRoomArrange(examId, gradeId);
	}
	
	@RequestMapping(value="findExamineeRoomArrange",method=RequestMethod.GET)
	@ResponseBody
	public List<ExamineeRoomArrangeVo> findExamineeRoomArrange(@RequestParam(value="examId") int examId,
			@RequestParam(value="type") int type,
			@RequestParam(value="classId",required=false,defaultValue="0") int classId,
			@RequestParam(value="roomId",required=false,defaultValue="0") int roomId){		
		List<ExamineeRoomArrangeVo> result = new ArrayList<ExamineeRoomArrangeVo>();
		if (type == 1) {
			result = examineeRoomArrangeService.findExamineeRoomArrangeByClassId(classId, examId);
		}
		if (type == 2) {
			result = examineeRoomArrangeService.findExamineeRoomArrangeByRoomId(roomId, examId);
		}
		return result;
	}
	
	
	
}
