package com.zzy.pony.controller;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzy.pony.config.Constants;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.model.Weekday;
import com.zzy.pony.service.LessonPeriodService;
import com.zzy.pony.service.PreLessonArrangeService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.service.WeekdayService;
import com.zzy.pony.vo.ArrangeVo;



@Controller
@RequestMapping(value = "/preLessonArrange")
public class PreLessonArrangeController {
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	@Autowired
	private WeekdayService weekdayService;
	@Autowired
	private LessonPeriodService lessonPeriodService;
	@Autowired
	private PreLessonArrangeService preLessonArrangeService;

	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){

	
		return "preLessonArrange/main";
	}
	@RequestMapping(value="listTableData",method = RequestMethod.GET)
	@ResponseBody
	public String listTableData(@RequestParam(value="classId") int classId,@RequestParam(value="subjectId") int subjectId,  Model model){
		StringBuilder result= new StringBuilder();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		SchoolYear schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		List<ArrangeVo> arrangeVos = preLessonArrangeService.findVoByClassIdAndSubject(classId, subjectId);
		List<Weekday> weekdays =   weekdayService.findByhaveClass(Constants.HAVECLASS_FLAG_TRUE);//上课星期数
		List<LessonPeriod> lessonPeriods= lessonPeriodService.findBySchoolYearAndTerm(schoolYear, term);//上课时段
		for (LessonPeriod lessonPeriod : lessonPeriods) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("period", lessonPeriod.getStartTime()+"--"+lessonPeriod.getEndTime());
			for (Weekday weekday : weekdays) {
				Boolean  flag = false;
				String subjectName="";
				for (ArrangeVo vo : arrangeVos) {
					if (lessonPeriod.getPeriodId().equals(vo.getPeriodId())
						&& (weekday.getSeq()+"").equalsIgnoreCase(vo.getWeekDay())) {
						flag = true;
						subjectName=vo.getSubjectName();
						break;
					}
				}				
				if (flag) {
				map.put(Constants.WEEKDAYMAP.get(weekday.getSeq()+""), subjectName);	//排课
				}else {
				map.put(Constants.WEEKDAYMAP.get(weekday.getSeq()+""), "");	//不排课
				}
			}
			list.add(map);		
		}
		GsonBuilder gb = new GsonBuilder();
		Gson gson = gb.create();
		String data = gson.toJson(list);
		result.append("{\"tableData\"");
		result.append(":");
		result.append(data);		
		result.append("}");
		
		return result.toString();
	}
	@RequestMapping(value="save",method = RequestMethod.POST)
	@ResponseBody
	public void save(@RequestBody List<ArrangeVo> arrangeVos){
		//保存逻辑,现删除原有排课，再插入
		preLessonArrangeService.deleteByClassIdAndSubject(arrangeVos.get(0).getClassId(), arrangeVos.get(0).getSubjectId());
		for (ArrangeVo arrangeVo : arrangeVos) {					
			preLessonArrangeService.save(arrangeVo);												
		}
		
	}
	
	

	
	
	
	
	
	
	
	
}
