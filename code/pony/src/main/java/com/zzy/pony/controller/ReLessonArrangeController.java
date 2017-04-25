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
import com.zzy.pony.dao.LessonArrangeDao;
import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Term;
import com.zzy.pony.model.Weekday;
import com.zzy.pony.service.AutoLessonArrangeService;
import com.zzy.pony.service.LessonArrangeService;
import com.zzy.pony.service.LessonPeriodService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.service.WeekdayService;
import com.zzy.pony.vo.ArrangeVo;
import com.zzy.pony.vo.conditionVo;



@Controller
@RequestMapping(value = "/reLessonArrange")
public class ReLessonArrangeController {
	
	
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	@Autowired
	private LessonArrangeService lessonArrangeService;
	@Autowired
	private LessonPeriodService lessonPeriodService;
	@Autowired
	private WeekdayService weekdayService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private LessonArrangeDao lessonArrangeDao;

	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){	
		return "reLessonArrange/main";
	}
	@RequestMapping(value="rearrange",method = RequestMethod.POST)
	@ResponseBody
	public void rearrange(@RequestBody List<ArrangeVo> arrangeVos){
		ArrangeVo beforeSelect = arrangeVos.get(0);
		ArrangeVo afterSelect = arrangeVos.get(1);
		SchoolYear year = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		
		//String[] beforePeriods = beforeSelect.getPeriod().split("--");
		//LessonPeriod beforeLessonPeriod = lessonPeriodService.findByStartTimeAndEndTime(beforePeriods[0], beforePeriods[1]);
		LessonPeriod beforeLessonPeriod = lessonPeriodService.findBySchoolYearAndTermAndSeq(year, term,Integer.valueOf(beforeSelect.getPeriod()) );
		String beforeWeekday="";
		for (String key : Constants.WEEKDAYS.keySet()) {
			if (Constants.WEEKDAYS.get(key).equalsIgnoreCase(beforeSelect.getWeekDay())) {
				beforeWeekday = key;
				break;
			}
		}
		Subject beforeSubject = null;
		if (!"".equalsIgnoreCase(beforeSelect.getSubjectName())) {
			 beforeSubject = subjectService.findByName(beforeSelect.getSubjectName());
		}
		//String[] afterPeriods = afterSelect.getPeriod().split("--");
		//LessonPeriod afterLessonPeriod = lessonPeriodService.findByStartTimeAndEndTime(afterPeriods[0], afterPeriods[1]);
		LessonPeriod afterLessonPeriod = lessonPeriodService.findBySchoolYearAndTermAndSeq(year, term,Integer.valueOf(afterSelect.getPeriod()) );

		String afterWeekday="";
		for (String key : Constants.WEEKDAYS.keySet()) {
			if (Constants.WEEKDAYS.get(key).equalsIgnoreCase(afterSelect.getWeekDay())) {
			    afterWeekday = key;
				break;
			}
		}
		Subject afterSubject = null;
		if (!"".equalsIgnoreCase(afterSelect.getSubjectName())) {
			 afterSubject = subjectService.findByName(afterSelect.getSubjectName());
		}
		//二者均非空
		if(beforeSubject!=null && afterSubject!= null ){
		LessonArrange	beforeLessonArrange =  lessonArrangeService.findByClassIdAndSchoolYearAndTermAndWeekDayAndLessonPeriod(beforeSelect.getClassId(), year, term, beforeWeekday, beforeLessonPeriod);
		LessonArrange	afterLessonArrange =  lessonArrangeService.findByClassIdAndSchoolYearAndTermAndWeekDayAndLessonPeriod(afterSelect.getClassId(), year, term, afterWeekday, afterLessonPeriod);
		beforeLessonArrange.setSubject(afterSubject);
		afterLessonArrange.setSubject(beforeSubject);
		lessonArrangeDao.save(beforeLessonArrange);
		lessonArrangeDao.save(afterLessonArrange);	
		}
		if (beforeSubject!=null&&afterSubject==null) {
			LessonArrange	beforeLessonArrange =  lessonArrangeService.findByClassIdAndSchoolYearAndTermAndWeekDayAndLessonPeriod(beforeSelect.getClassId(), year, term, beforeWeekday, beforeLessonPeriod);
			beforeLessonArrange.setWeekDay(afterWeekday);
			beforeLessonArrange.setLessonPeriod(afterLessonPeriod);
			lessonArrangeDao.save(beforeLessonArrange);
		}
		if (beforeSubject==null&&afterSubject!=null) {
			LessonArrange	afterLessonArrange =  lessonArrangeService.findByClassIdAndSchoolYearAndTermAndWeekDayAndLessonPeriod(afterSelect.getClassId(), year, term, afterWeekday, afterLessonPeriod);
			afterLessonArrange.setWeekDay(beforeWeekday);
			afterLessonArrange.setLessonPeriod(beforeLessonPeriod);
			lessonArrangeDao.save(afterLessonArrange);
		}
	
	}
	
	
	
	@RequestMapping(value="listTableData",method = RequestMethod.POST)
	@ResponseBody
	public String listTableData(@RequestBody conditionVo cv) {
			//新增默认全选功能
			SchoolYear year = schoolYearService.getCurrent();
			Term term = termService.getCurrent();
			List<Weekday> weekdays = weekdayService.findByhaveClass(Constants.HAVECLASS_FLAG_TRUE);
			List<LessonPeriod> lessonPeriods= lessonPeriodService.findBySchoolYearAndTerm(year, term);
			List<Map<String, Object>> dataList =  new ArrayList<Map<String,Object>>();
			for (LessonPeriod lessonPeriod : lessonPeriods) {
				Map<String, Object> map = new HashMap<String, Object>();
				//map.put("period",lessonPeriod.getStartTime()+"--"+lessonPeriod.getEndTime());
				map.put("period", lessonPeriod.getSeq()+"");
				for (Weekday weekday : weekdays) {
					LessonArrange lessonArrange =  lessonArrangeService.findByClassIdAndSchoolYearAndTermAndWeekDayAndLessonPeriod(cv.getClassId(), year, term, weekday.getSeq()+"", lessonPeriod);
					if (lessonArrange != null && lessonArrange.getSubject() !=null) {
						map.put( Constants.WEEKDAYMAP.get(weekday.getSeq()+"") , lessonArrange.getSubject().getName());
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
