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
import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.model.Term;
import com.zzy.pony.model.Weekday;
import com.zzy.pony.service.AutoLessonArrangeService;
import com.zzy.pony.service.LessonArrangeService;
import com.zzy.pony.service.LessonPeriodService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.TeacherService;
import com.zzy.pony.service.TeacherSubjectService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.service.WeekdayService;
import com.zzy.pony.vo.ConditionVo;



@Controller
@RequestMapping(value = "/teacherCourse")
public class TeacherCourseController {
	
	
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	@Autowired
	private WeekdayService weekdayService;
	@Autowired
	private LessonPeriodService lessonPeriodService;
	@Autowired
	private TeacherSubjectService teacherSubjectService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private LessonArrangeService lessonArrangeService ;
	

	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){	
		return "teacherCourse/main";
	}
	
	
	
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public String listTableData(@RequestParam(value="teacherId") int teacherId) {
			//新增默认全选功能
			SchoolYear year = schoolYearService.getCurrent();
			Term term = termService.getCurrent();
			List<Weekday> weekdays = weekdayService.findByhaveClass(Constants.HAVECLASS_FLAG_TRUE);
			List<LessonPeriod> lessonPeriods= lessonPeriodService.findBySchoolYearAndTerm(year, term);
			Teacher teacher = teacherService.get(teacherId);
			List<TeacherSubject> teacherSubjects =  teacherSubjectService.findCurrentByTeacher(teacher);//该老师在本学期的任课列表
			
			List<Map<String, Object>> dataList =  new ArrayList<Map<String,Object>>();
			for (LessonPeriod lessonPeriod : lessonPeriods) {
				Map<String, Object> map = new HashMap<String, Object>();
				//map.put("period",lessonPeriod.getStartTime()+"--"+lessonPeriod.getEndTime());
				map.put("period", lessonPeriod.getSeq()+"");
				for (Weekday weekday : weekdays) {
					for (TeacherSubject teacherSubject : teacherSubjects) {
						
						LessonArrange lessonArrange =  lessonArrangeService.findByClassIdAndSubjectAndSchoolYearAndTermAndWeekDayAndLessonPeriod(teacherSubject.getSchoolClass().getClassId(), teacherSubject.getSubject(), year, term, weekday.getSeq()+"", lessonPeriod);
						if (lessonArrange != null ) {
							map.put(Constants.WEEKDAYMAP.get(weekday.getSeq()+""), teacherSubject.getSchoolClass().getName()+"("+teacherSubject.getSubject().getName()+")");
						}
						
					}
		      }
				dataList.add(map);
			}
			StringBuilder result = new StringBuilder();	
			GsonBuilder gb = new GsonBuilder();
			Gson gson = gb.create();
			String data = gson.toJson(dataList);
		
			result.append("{\"total\"");
			result.append(":");
			result.append(dataList.size());
			result.append(",\"rows\":");
			result.append(data);						
			result.append("}");
		
            return result.toString();
			

	}
	
	
	
	
	
	
	
	
	
}
