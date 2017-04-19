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
import org.springframework.web.bind.annotation.ResponseBody;













import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzy.pony.config.Constants;
import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.model.Weekday;
import com.zzy.pony.service.AutoLessonArrangeService;
import com.zzy.pony.service.LessonArrangeService;
import com.zzy.pony.service.LessonPeriodService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.service.WeekdayService;
import com.zzy.pony.vo.conditionVo;



@Controller
@RequestMapping(value = "/autoLessonArrange")
public class AutoLessonArrangeController {
	
	@Autowired
	private AutoLessonArrangeService autoLessonArrangeService;
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

	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){	
		return "autoLessonArrange/main";
	}
	
	@RequestMapping(value="autoLessonArrange",method = RequestMethod.GET)
	@ResponseBody
	public void autoLessonArrange(){
		//删除当前学年，当前学期所有自动排课和调课类型的数据
		SchoolYear year = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		List<LessonArrange> autoList = lessonArrangeService.findBySchooleYearAndTermAndSourceType(year, term, Constants.SOURCE_TYPE_AUTO);
		List<LessonArrange> changeList = lessonArrangeService.findBySchooleYearAndTermAndSourceType(year, term, Constants.SOURCE_TYPE_CHANGE);
		autoList.addAll(changeList);
		lessonArrangeService.deleteList(autoList);	
		autoLessonArrangeService.autoLessonArrange();		
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
				map.put("period",lessonPeriod.getStartTime()+"--"+lessonPeriod.getEndTime());
				for (Weekday weekday : weekdays) {
					LessonArrange lessonArrange =  lessonArrangeService.findByClassIdAndSchoolYearAndTermAndWeekDayAndLessonPeriod(cv.getClassId(), year, term, weekday.getSeq()+"", lessonPeriod);
					if (lessonArrange != null && lessonArrange.getSubject() !=null) {
						map.put( Constants.WEEKDAYMAP.get(weekday.getSeq()) , lessonArrange.getSubject().getName());
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
