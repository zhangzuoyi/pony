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
import com.zzy.pony.model.ClassNoCourse;
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.model.Weekday;
import com.zzy.pony.service.ClassNoCourseService;
import com.zzy.pony.service.LessonPeriodService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.service.WeekdayService;
import com.zzy.pony.vo.ClassNoCourseVo;
import com.zzy.pony.vo.GradeNoCourseVo;
import com.zzy.pony.vo.NoCourseVo;



@Controller
@RequestMapping(value = "/classNoCourse")
public class ClassNoCourseController {
	@Autowired
	private ClassNoCourseService classNoCourseService;
	@Autowired
	private WeekdayService weekdayService;
	@Autowired
	private LessonPeriodService lessonPeriodService;
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	@Autowired
	private SchoolClassService schoolClassService;
	
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){

	
		return "classNoCourse/main";
	}
	
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<ClassNoCourse> list(Model model){
		List<ClassNoCourse> list = classNoCourseService.findAll();
		return list;
	}
	@RequestMapping(value="listVo",method = RequestMethod.GET)
	@ResponseBody
	public List<ClassNoCourseVo> listVo(Model model){
		List<ClassNoCourseVo> list = classNoCourseService.findAllVo();
		return list;
	}
	@RequestMapping(value="listTableData",method = RequestMethod.GET)
	@ResponseBody
	public String listTableData(@RequestParam(value="classId") int classId,  Model model){
		StringBuilder result= new StringBuilder();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		SchoolYear schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		List<Weekday> weekdays =   weekdayService.findByhaveClass(Constants.HAVECLASS_FLAG_TRUE);//上课星期数
		List<ClassNoCourseVo> classNoCourseVos = classNoCourseService.findAllVo();
		List<LessonPeriod> lessonPeriods= lessonPeriodService.findBySchoolYearAndTerm(schoolYear, term);//上课时段
		for (LessonPeriod lessonPeriod : lessonPeriods) {
			Map<String, Object> map = new HashMap<String, Object>();
			//map.put("period", lessonPeriod.getStartTime()+"--"+lessonPeriod.getEndTime());
			map.put("period", lessonPeriod.getSeq()+"");
			for (Weekday weekday : weekdays) {
				Boolean  flag = false;
				for (ClassNoCourseVo vo : classNoCourseVos) {
					if (classId == vo.getClassId() 
							&& lessonPeriod.getPeriodId().equals(vo.getLessonPeriodId())
							&& weekday.getSeq() == vo.getWeekdayId()
							&& schoolYear.getYearId().equals(vo.getSchoolYearId())
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
	
	@RequestMapping(value="findVoByClass",method = RequestMethod.GET)
	@ResponseBody
	public List<ClassNoCourseVo> findVoByClass(@RequestParam(value="classId") int classId){
		List<ClassNoCourseVo> result = classNoCourseService.findVoByClass(classId);
		return result;		
	}
	
	@RequestMapping(value="delete",method = RequestMethod.GET)
	@ResponseBody
	public void delete(@RequestParam(value="classId") int classId){
		SchoolYear  schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		SchoolClass schoolClass = schoolClassService.get(classId);
		classNoCourseService.deleteByClassAndYearAndTerm(schoolClass, schoolYear, term);
	}
	
	@RequestMapping(value="save",method = RequestMethod.POST)
	@ResponseBody
	public void save(@RequestBody List<NoCourseVo> classNoCourseVos){
		//保存逻辑,现删除原有班级的不排课，再插入
		SchoolYear  schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();		
		String[] classIds = classNoCourseVos.get(0).getClassIds();
		for (String classId : classIds) {
			SchoolClass schoolClass	=   schoolClassService.get(Integer.valueOf(classId));
			classNoCourseService.deleteByClassAndYearAndTerm(schoolClass, schoolYear, term);	
		}
		for (NoCourseVo noCourseVo : classNoCourseVos) {
			for (String classId : classIds) {
				SchoolClass schoolClass	=   schoolClassService.get(Integer.valueOf(classId));
				/*	classNoCourseService.deleteByClassAndYearAndTerm(schoolClass, schoolYear, term);*/	
				ClassNoCourse cnc = new ClassNoCourse();
				cnc.setSchoolYear(schoolYear);
				cnc.setTerm(term);
				cnc.setSchoolClass(schoolClass);
				//weekday-->seq
				Weekday weekday = weekdayService.findByName(noCourseVo.getWeekday());
				cnc.setWeekday(weekday);
				//peroid-->periodId
				//String[] periods = noCourseVo.getPeriod().split("--");
				//LessonPeriod lessonPeriod = lessonPeriodService.findByStartTimeAndEndTime(periods[0], periods[1]);				
				LessonPeriod lessonPeriod = lessonPeriodService.findBySchoolYearAndTermAndSeq(schoolYear, term,Integer.valueOf(noCourseVo.getPeriod()) );
				cnc.setLessonPeriod(lessonPeriod);
				classNoCourseService.save(cnc);				
			}		
		}
		
	}

	
	
	
	
	
	
}
