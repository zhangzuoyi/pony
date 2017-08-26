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
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.TeacherNoCourse;
import com.zzy.pony.model.Term;
import com.zzy.pony.model.Weekday;
import com.zzy.pony.service.LessonPeriodService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.TeacherNoCourseService;
import com.zzy.pony.service.TeacherService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.service.WeekdayService;
import com.zzy.pony.vo.ClassNoCourseVo;
import com.zzy.pony.vo.NoCourseVo;
import com.zzy.pony.vo.TeacherNoCourseVo;


@Controller
@RequestMapping(value = "/teacherNoCourse")
public class TeacherNoCourseController {
	
	@Autowired
	private TeacherNoCourseService teacherNoCourseService;
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	@Autowired
	private WeekdayService weekdayService;
	@Autowired
	private LessonPeriodService lessonPeriodService;
	@Autowired
	private TeacherService teacherService;
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){

	
		return "teacherNoCourse/main";
	}

	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<TeacherNoCourse> list( ){
		List<TeacherNoCourse> resultList =teacherNoCourseService.findAll();					
		return resultList;
	}
	@RequestMapping(value="listAllVo",method = RequestMethod.GET)
	@ResponseBody
	public List<TeacherNoCourseVo> listAllVo( ){
		List<TeacherNoCourseVo> resultList =teacherNoCourseService.findAllVo();					
		return resultList;
	}
	
	@RequestMapping(value="listTableData",method = RequestMethod.GET)
	@ResponseBody
	public String listTableData(@RequestParam(value="teacherId") int teacherId,  Model model){
		StringBuilder result= new StringBuilder();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		SchoolYear schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		List<Weekday> weekdays =   weekdayService.findByhaveClass(Constants.HAVECLASS_FLAG_TRUE);//上课星期数
		List<TeacherNoCourseVo> teacherNoCourseVos = teacherNoCourseService.findAllVo();
		List<LessonPeriod> lessonPeriods= lessonPeriodService.findBySchoolYearAndTerm(schoolYear, term);//上课时段
		for (LessonPeriod lessonPeriod : lessonPeriods) {
			Map<String, Object> map = new HashMap<String, Object>();
			//map.put("period", lessonPeriod.getStartTime()+"--"+lessonPeriod.getEndTime());
			map.put("period", lessonPeriod.getSeq()+"");
			for (Weekday weekday : weekdays) {
				Boolean  flag = false;
				for (TeacherNoCourseVo vo : teacherNoCourseVos) {
					if (teacherId == vo.getTeacherId() 
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
	
	@RequestMapping(value="findAll",method = RequestMethod.GET)
	@ResponseBody
	public String findAll(){
		StringBuilder result= new StringBuilder();
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();		
		List<TeacherNoCourseVo> teacherNoCourseVos = teacherNoCourseService.findAllVo();		
		for (TeacherNoCourseVo vo : teacherNoCourseVos) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("yearName",vo.getYearName());
			map.put("termName", vo.getTermName());
			map.put("period",vo.getLessonPeriodSeq());
			map.put("week", vo.getWeekdayName());
			map.put("teacherName", vo.getTeacherName());
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
	
	@RequestMapping(value="findVoByTeacher",method = RequestMethod.GET)
	@ResponseBody
	public List<TeacherNoCourseVo> findVoByTeacher(@RequestParam(value="teacherId") int teacherId){
		List<TeacherNoCourseVo> result = teacherNoCourseService.findVoByTeacher(teacherId);
		return result;		
	}
	
	@RequestMapping(value="delete",method = RequestMethod.GET)
	@ResponseBody
	public void delete(@RequestParam(value="teacherId") int teacherId){
		SchoolYear  schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		Teacher teacher = teacherService.get(teacherId);
		teacherNoCourseService.deleteByTeacherAndYearAndTerm(teacher, schoolYear, term);
	}
	
	@RequestMapping(value="save",method = RequestMethod.POST)
	@ResponseBody
	public void save(@RequestBody List<NoCourseVo> teacherNoCourseVos){
		//保存逻辑,现删除原有老师的不排课，再插入
		SchoolYear  schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();		
		Teacher teacher = teacherService.get(Integer.valueOf(teacherNoCourseVos.get(0).getTeacherId()));
		teacherNoCourseService.deleteByTeacherAndYearAndTerm(teacher, schoolYear, term);		
		for (NoCourseVo noCourseVo : teacherNoCourseVos) {		
				TeacherNoCourse tnc = new TeacherNoCourse();
				tnc.setSchoolYear(schoolYear);
				tnc.setTerm(term);
				tnc.setTeacher(teacher);
				//weekday-->seq
				Weekday weekday = weekdayService.findByName(noCourseVo.getWeekday());
				tnc.setWeekday(weekday);
				//peroid-->periodId
				//String[] periods = noCourseVo.getPeriod().split("--");
				//LessonPeriod lessonPeriod = lessonPeriodService.findByStartTimeAndEndTime(periods[0], periods[1]);
				LessonPeriod lessonPeriod = lessonPeriodService.findBySchoolYearAndTermAndSeq(schoolYear, term,Integer.valueOf(noCourseVo.getPeriod()) );
				tnc.setLessonPeriod(lessonPeriod);
				teacherNoCourseService.save(tnc);									
		}
		
	}
	
	
	
	
	
	
	
}
