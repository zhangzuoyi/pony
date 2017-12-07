package com.zzy.pony.ss.controller;




import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.ss.model.SubjectSelectConfig;
import com.zzy.pony.ss.service.SubjectSelectConfigService;
import com.zzy.pony.ss.service.SubjectSelectStatisticsService;
import com.zzy.pony.ss.vo.StudentSubjectStatisticsVo;

@Controller
@RequestMapping(value = "/ss/statistics")
public class SubjectSelectStatisticsController {
	
	@Autowired
	private SubjectSelectStatisticsService subjectSelectStatisticsService;
	@Autowired
	private SubjectSelectConfigService subjectSelectConfigService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "ss/statistics/main";
	}

	@RequestMapping(value="totalSelect",method = RequestMethod.GET)
	@ResponseBody
	public int totalSelect(Model model){
		SubjectSelectConfig config = subjectSelectConfigService.getCurrent();
		return subjectSelectStatisticsService.findTotalSelectByConfig(config.getConfigId());
	}
	
	@RequestMapping(value="group",method = RequestMethod.GET)
	@ResponseBody
	public List<StudentSubjectStatisticsVo> group(Model model){
		List<StudentSubjectStatisticsVo> result = new ArrayList<StudentSubjectStatisticsVo>();
		SubjectSelectConfig config = subjectSelectConfigService.getCurrent();
		return subjectSelectStatisticsService.group(config.getConfigId());					
	}

	

	
}
