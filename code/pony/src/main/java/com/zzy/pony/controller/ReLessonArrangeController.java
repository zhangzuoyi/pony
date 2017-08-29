package com.zzy.pony.controller;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
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
import com.zzy.pony.dao.LessonArrangeDao;
import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.model.Term;
import com.zzy.pony.model.Weekday;
import com.zzy.pony.service.LessonArrangeService;
import com.zzy.pony.service.LessonPeriodService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.service.TeacherSubjectService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.service.WeekdayService;
import com.zzy.pony.vo.ArrangeVo;
import com.zzy.pony.vo.ConditionVo;



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
	@Autowired
	private TeacherSubjectService teacherSubjectService;
	@Autowired
	private SchoolClassService schoolClassService;

	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){	
		return "reLessonArrange/main";
	}
	@RequestMapping(value="rearrange",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> rearrange(@RequestBody List<ArrangeVo> arrangeVos){
		Map<String, String> resultMap = new HashMap<String, String>();
		String resultKey = "key";
		String resultValue="";
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
		LessonArrange	beforeLessonArrange =  lessonArrangeService.findByClassIdAndSubjectAndSchoolYearAndTermAndWeekDayAndLessonPeriod(beforeSelect.getClassId(),beforeSubject, year, term, beforeWeekday, beforeLessonPeriod);
		LessonArrange	afterLessonArrange =  lessonArrangeService.findByClassIdAndSubjectAndSchoolYearAndTermAndWeekDayAndLessonPeriod(afterSelect.getClassId(),afterSubject, year, term, afterWeekday, afterLessonPeriod);
		resultValue = isLessonArrangeConflict(beforeLessonArrange,afterLessonArrange,beforeWeekday,beforeLessonPeriod,afterWeekday,afterLessonPeriod);
		if (!"".equalsIgnoreCase(resultValue)) {
			resultMap.put(resultKey, resultValue);
			return resultMap;
		}	
		beforeLessonArrange.setSubject(afterSubject);
		afterLessonArrange.setSubject(beforeSubject);
		lessonArrangeDao.save(beforeLessonArrange);
		lessonArrangeDao.save(afterLessonArrange);	
		}
		if (beforeSubject!=null&&afterSubject==null) {
			LessonArrange	beforeLessonArrange =  lessonArrangeService.findByClassIdAndSubjectAndSchoolYearAndTermAndWeekDayAndLessonPeriod(beforeSelect.getClassId(),beforeSubject, year, term, beforeWeekday, beforeLessonPeriod);
			resultValue = isLessonArrangeConflict(beforeLessonArrange,null,beforeWeekday,beforeLessonPeriod,afterWeekday,afterLessonPeriod);
			if (!"".equalsIgnoreCase(resultValue)) {
				resultMap.put(resultKey, resultValue);
				return resultMap;
			}
			beforeLessonArrange.setWeekDay(afterWeekday);
			beforeLessonArrange.setLessonPeriod(afterLessonPeriod);
			lessonArrangeDao.save(beforeLessonArrange);
		}
		if (beforeSubject==null&&afterSubject!=null) {
			LessonArrange	afterLessonArrange =  lessonArrangeService.findByClassIdAndSubjectAndSchoolYearAndTermAndWeekDayAndLessonPeriod(afterSelect.getClassId(),afterSubject, year, term, afterWeekday, afterLessonPeriod);
			resultValue = isLessonArrangeConflict(null,afterLessonArrange,beforeWeekday,beforeLessonPeriod,afterWeekday,afterLessonPeriod);
			if (!"".equalsIgnoreCase(resultValue)) {
				resultMap.put(resultKey, resultValue);
				return resultMap;
			}
			afterLessonArrange.setWeekDay(beforeWeekday);
			afterLessonArrange.setLessonPeriod(beforeLessonPeriod);
			lessonArrangeDao.save(afterLessonArrange);
		}
		
		return resultMap;
	
	}
	
	
	
	@RequestMapping(value="listTableData",method = RequestMethod.POST)
	@ResponseBody
	public String listTableData(@RequestBody ConditionVo cv) {
			//新增默认全选功能
			SchoolYear year = schoolYearService.getCurrent();
			Term term = termService.getCurrent();
			List<Weekday> weekdays = weekdayService.findByhaveClass(Constants.HAVECLASS_FLAG_TRUE);
			List<LessonPeriod> lessonPeriods= lessonPeriodService.findBySchoolYearAndTerm(year, term);
			List<Map<String, Object>> dataList =  new ArrayList<Map<String,Object>>();
			List<LessonArrange> allLessonArranges =  lessonArrangeService.findByClassIdAndSchoolYearAndTerm(cv.getClassId(), year, term);
			for (LessonPeriod lessonPeriod : lessonPeriods) {
				Map<String, Object> map = new HashMap<String, Object>();
				//map.put("period",lessonPeriod.getStartTime()+"--"+lessonPeriod.getEndTime());
				map.put("period", lessonPeriod.getSeq()+"");
				for (Weekday weekday : weekdays) {
//					List<LessonArrange> lessonArranges =  lessonArrangeService.findByClassIdAndSchoolYearAndTermAndWeekDayAndLessonPeriod(cv.getClassId(), year, term, weekday.getSeq()+"", lessonPeriod);
					List<LessonArrange> lessonArranges =  getLessonArranges(allLessonArranges, weekday.getSeq()+"", lessonPeriod);
					if (!lessonArranges.isEmpty()) {
						StringBuilder sb = new StringBuilder();
						for (LessonArrange la : lessonArranges) {
							if (la.getSubject()!=null) {
								sb.append(la.getSubject().getName());
							}
						}											
						map.put( Constants.WEEKDAYMAP.get(weekday.getSeq()+"") , sb);
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
	
	private List<LessonArrange> getLessonArranges(List<LessonArrange> allLessonArranges,String weekDay,LessonPeriod lessonPeriod){
		List<LessonArrange> list=new ArrayList<LessonArrange>();
		for(LessonArrange la: allLessonArranges){
			if(la.getLessonPeriod().getPeriodId().equals(lessonPeriod.getPeriodId()) && la.getWeekDay().equals(weekDay)){
				list.add(la);
			}
		}
		return list;
	}
	private String isLessonArrangeConflict(LessonArrange beforeLessonArrange,LessonArrange afterLessonArrange,String beforeWeekday,LessonPeriod beforeLessonPeriod,String afterWeekday,LessonPeriod afterLessonPeriod){
		
		if (beforeLessonArrange != null && afterLessonArrange != null) {
			//SchoolClass  beforeClass = schoolClassService.get(beforeLessonArrange.getClassId());
			//SchoolClass  afterClass = schoolClassService.get(afterLessonArrange.getClassId());	
			SchoolClass beforeClass = new SchoolClass();
			beforeClass.setClassId(beforeLessonArrange.getClassId());
			SchoolClass afterClass = new SchoolClass();
			afterClass.setClassId(afterLessonArrange.getClassId());

			TeacherSubject beforeTeacherSubject =  teacherSubjectService.findCurrentByClassAndSubject(beforeClass, beforeLessonArrange.getSubject());
			TeacherSubject afterTeacherSubject =  teacherSubjectService.findCurrentByClassAndSubject(afterClass, afterLessonArrange.getSubject());

			boolean flag1 =  lessonArrangeService.isTeacherConflict(Integer.valueOf(afterLessonArrange.getWeekDay()), afterLessonArrange.getLessonPeriod().getPeriodId(), beforeTeacherSubject.getTeacher().getTeacherId());
			boolean flag2 =  lessonArrangeService.isTeacherConflict(Integer.valueOf(beforeLessonArrange.getWeekDay()), beforeLessonArrange.getLessonPeriod().getPeriodId(), afterTeacherSubject.getTeacher().getTeacherId());
			String result= "";
			if (flag1) {
				result += beforeTeacherSubject.getTeacher().getName()+"("+beforeTeacherSubject.getSubject().getName()+")"+" ";
			} 
			if (flag2) {
				result += afterTeacherSubject.getTeacher().getName()+"("+afterTeacherSubject.getSubject().getName()+")";
			}
			return result;
			
		}else if (beforeLessonArrange == null && afterLessonArrange != null) {
			//SchoolClass  afterClass = schoolClassService.get(afterLessonArrange.getClassId());
			SchoolClass afterClass = new SchoolClass();
			afterClass.setClassId(afterLessonArrange.getClassId());
			TeacherSubject afterTeacherSubject =  teacherSubjectService.findCurrentByClassAndSubject(afterClass, afterLessonArrange.getSubject());
			boolean flag2 =  lessonArrangeService.isTeacherConflict(Integer.valueOf(beforeWeekday), beforeLessonPeriod.getPeriodId(), afterTeacherSubject.getTeacher().getTeacherId());
			
			if (flag2) {
				return afterTeacherSubject.getTeacher().getName()+"("+afterTeacherSubject.getSubject().getName()+")";
			}
			
			
		}else if (beforeLessonArrange != null && afterLessonArrange == null) {
			//SchoolClass  beforeClass = schoolClassService.get(beforeLessonArrange.getClassId());
			SchoolClass beforeClass = new SchoolClass();
			beforeClass.setClassId(beforeLessonArrange.getClassId());
			TeacherSubject beforeTeacherSubject =  teacherSubjectService.findCurrentByClassAndSubject(beforeClass, beforeLessonArrange.getSubject());
			boolean flag1 =  lessonArrangeService.isTeacherConflict(Integer.valueOf(afterWeekday), afterLessonPeriod.getPeriodId(), beforeTeacherSubject.getTeacher().getTeacherId());
			
			if (flag1) {
				return beforeTeacherSubject.getTeacher().getName()+"("+beforeTeacherSubject.getSubject().getName()+")";
			}
		}		
		return "";
		
	}
	
}
