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
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.GradeNoCourse;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.model.Weekday;
import com.zzy.pony.service.GradeNoCourseService;
import com.zzy.pony.service.GradeService;
import com.zzy.pony.service.LessonPeriodService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.service.WeekdayService;
import com.zzy.pony.vo.GradeNoCourseVo;
import com.zzy.pony.vo.NoCourseVo;


@Controller
@RequestMapping(value = "/gradeNoCourse")
public class GradeNoCourseController {
	
	@Autowired
	private GradeNoCourseService gradeNoCourseService;
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	@Autowired
	private WeekdayService weekdayService;
	@Autowired
	private LessonPeriodService lessonPeriodService;
	@Autowired
	private GradeService gradeService;
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){

	
		return "gradeNoCourse/main";
	}

	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<GradeNoCourse> list( ){
		List<GradeNoCourse> resultList =gradeNoCourseService.findAll();					
		return resultList;
	}
	@RequestMapping(value="listAllVo",method = RequestMethod.GET)
	@ResponseBody
	public List<GradeNoCourseVo> listAllVo( ){
		List<GradeNoCourseVo> resultList =gradeNoCourseService.findAllVo();					
		return resultList;
	}
	
	@RequestMapping(value="listTableData",method = RequestMethod.GET)
	@ResponseBody
	public String listTableData(@RequestParam(value="gradeId") int gradeId,  Model model){
		StringBuilder result= new StringBuilder();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		SchoolYear schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		List<Weekday> weekdays =   weekdayService.findByhaveClass(Constants.HAVECLASS_FLAG_TRUE);//上课星期数
		List<GradeNoCourseVo> gradeNoCourseVos = gradeNoCourseService.findAllVo();
		List<LessonPeriod> lessonPeriods= lessonPeriodService.findBySchoolYearAndTerm(schoolYear, term);//上课时段
		for (LessonPeriod lessonPeriod : lessonPeriods) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("period", lessonPeriod.getStartTime()+"--"+lessonPeriod.getEndTime());
			for (Weekday weekday : weekdays) {
				Boolean  flag = false;
				for (GradeNoCourseVo vo : gradeNoCourseVos) {
					if (gradeId == vo.getGradeId() 
							&& lessonPeriod.getPeriodId().equals(vo.getLessonPeriodId())
							&& weekday.getSeq() == vo.getWeekdayId()
							&& schoolYear.getYearId().equals(vo.getYearId())
							&& term.getTermId().equals(vo.getTermId())
							) {
						flag = true;
						break;
					}
				}				
				if (flag) {
				map.put(Constants.WEEKDAYMAP.get(weekday.getSeq()+""), "不排课");	//不排课
				}else {
				map.put(Constants.WEEKDAYMAP.get(weekday.getSeq()+""), "");	//排课
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
	public void save(@RequestBody List<NoCourseVo> gradeNoCourseVos){
		//保存逻辑,现删除原有老师的不排课，再插入
		SchoolYear  schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();		
		Grade grade = gradeService.get(Integer.valueOf(gradeNoCourseVos.get(0).getGradeId()));
		gradeNoCourseService.deleteByGradeAndYearAndTerm(grade, schoolYear, term);		
		for (NoCourseVo noCourseVo : gradeNoCourseVos) {		
				GradeNoCourse tnc = new GradeNoCourse();
				tnc.setSchoolYear(schoolYear);
				tnc.setTerm(term);
				tnc.setGrade(grade);
				//weekday-->seq
				Weekday weekday = weekdayService.findByName(noCourseVo.getWeekday());
				tnc.setWeekday(weekday);
				//peroid-->periodId
				String[] periods = noCourseVo.getPeriod().split("--");
				LessonPeriod lessonPeriod = lessonPeriodService.findByStartTimeAndEndTime(periods[0], periods[1]);
				tnc.setLessonPeriod(lessonPeriod);
				gradeNoCourseService.save(tnc);									
		}
		
	}
	
	
	
	
	
	
	
}
