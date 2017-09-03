package com.zzy.pony.exam.controller;





import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.exam.mapper.ExamMonitorMapper;
import com.zzy.pony.exam.service.ExamMonitorService;
import com.zzy.pony.exam.service.ExamineeArrangeService;
import com.zzy.pony.exam.vo.ExamMonitorVo;




@Controller
@RequestMapping(value="/examAdmin/monitorArrange")
public class MonitorArrangeController {
	
	@Autowired
	private ExamMonitorService service;
	@Autowired
	private ExamMonitorMapper mapper;

	
	@RequestMapping(value="main",method=RequestMethod.GET)
	public String main(Model model){
		return "examAdmin/monitorArrange/main";
	}
	
	@RequestMapping(value="list",method=RequestMethod.GET)
	@ResponseBody
	public List<ExamMonitorVo> list(@RequestParam(value="examId") int examId, Model model){
		return mapper.find(examId);
	}
	
	@RequestMapping(value="submit",method=RequestMethod.POST)
	@ResponseBody
	public void submit(@RequestParam(value="examId") int examId,@RequestParam(value="teacherIds[]") int[] teacherIds){
		
		service.add(examId, teacherIds);
	}
	
	@RequestMapping(value="setCount",method=RequestMethod.POST)
	@ResponseBody
	public void setCount(@RequestParam(value="examId") int examId,@RequestParam(value="teacherIds[]") int[] teacherIds,@RequestParam(value="count") int count){
		
		service.setCount(examId, teacherIds, count);
	}
	
	@RequestMapping(value="delete",method=RequestMethod.POST)
	@ResponseBody
	public void delete(@RequestParam(value="ids[]") int[] ids){
		service.delete(ids);
	}
	
	@RequestMapping(value="monitorArrange",method=RequestMethod.POST)
	@ResponseBody
	public void monitorArrange(@RequestParam(value="examId") int examId){
		service.monitorArrange(examId);
	}
	
	
}
