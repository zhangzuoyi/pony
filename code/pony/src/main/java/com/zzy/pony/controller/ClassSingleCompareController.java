package com.zzy.pony.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.zzy.pony.dao.ExamTypeDao;
import com.zzy.pony.dao.SchoolClassDao;
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.service.ClassSingleCompareService;
import com.zzy.pony.service.GradeService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.vo.ConditionVo;

@Controller
@RequestMapping(value = "/classSingleCompare")
public class ClassSingleCompareController {
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private ExamTypeDao examTypeDao;
	@Autowired
	private SchoolClassService schoolClassService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SchoolClassDao schoolClassDao;
	@Autowired
	private ClassSingleCompareService classSingleCompareService;
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){

		List<SchoolYear> schoolYears = schoolYearService.findAll();
		List<Term> terms = termService.findAll();
		List<Grade> grades = gradeService.findAll();
		//List<ExamType> examTypes = examTypeDao.findAll();
		//List<SchoolClass> schoolClasses = schoolClassService.findAll();
	
		model.addAttribute("schoolYears", schoolYears);
		model.addAttribute("terms", terms);
		model.addAttribute("grades", grades);
		//model.addAttribute("examTypes", examTypes);
		//model.addAttribute("schoolClasses", schoolClasses);
		

	
		
		return "classSingleCompare/main";
	}
	@RequestMapping(value="findByCondition",method = RequestMethod.POST)
	@ResponseBody
	public String findByCondition(@RequestBody ConditionVo cv) {
		//新增默认全选功能
			List<SchoolClass> schoolClasses = new ArrayList<SchoolClass>();
		SchoolYear year =  schoolYearService.getCurrent();
		if (cv.getSchoolClasses()==null || cv.getSchoolClasses().length == 0) {
			
			schoolClasses = schoolClassService.findByYearAndGrade(year.getYearId(), cv.getGradeId());
			String[] schoolClassArray = new String[schoolClasses.size()] ;
			for (int i = 0; i < schoolClasses.size(); i++) {
				schoolClassArray[i] = schoolClasses.get(i).getClassId()+"";
			}
			cv.setSchoolClasses(schoolClassArray);
		}else {
			for (String classId : cv.getSchoolClasses()) {
				schoolClasses.add(schoolClassDao.findOne(Integer.valueOf(classId)));
			}			
		}
		
		
		
			StringBuilder result = new StringBuilder();
			List<Map<String, Object>> dataList =  classSingleCompareService.findByCondition(cv);
			List<Map<String, Object>> headList = new ArrayList<Map<String,Object>>();
			
			Map<String, Object> classNameMap = new HashMap<String, Object>();
			classNameMap.put("prop", "className");
			classNameMap.put("label", "班级");
			Map<String, Object> teacherNameMap = new HashMap<String, Object>();
			teacherNameMap.put("prop", "teacherName");
			teacherNameMap.put("label", "任课教师");
			Map<String, Object> studentCountMap = new HashMap<String, Object>();
			studentCountMap.put("prop", "studentCount");
			studentCountMap.put("label", "考生人数");
	
			Map<String, Object> averageMap = new HashMap<String, Object>();
			averageMap.put("prop", Constants.SUBJETCS.get(cv.getSubjectId())+"Average");
			averageMap.put("label", "平均分");
			Map<String, Object> topMap = new HashMap<String, Object>();
			topMap.put("prop", "top");
			topMap.put("label", "最高分");
			Map<String, Object> bottomMap = new HashMap<String, Object>();
			bottomMap.put("prop", "bottom");
			bottomMap.put("label", "最低分");
			headList.add(classNameMap);
			headList.add(teacherNameMap);
			headList.add(studentCountMap);
			headList.add(averageMap);
			headList.add(topMap);
			headList.add(bottomMap);
		
			GsonBuilder gb = new GsonBuilder();
			Gson gson = gb.create();
			String data = gson.toJson(dataList);
			Gson gson2 = gb.create();
			String head= gson2.toJson(headList);
			
			//新增echarts数据获取xAxis(班级)yAxis(平均分最高分最低分)
			
			
			Map<String, Object> echartsMap = new LinkedHashMap<String, Object>();
	
			for (SchoolClass schoolClass : schoolClasses) {			
				if (dataList!=null&&dataList.size()!=0) {
					for (Map<String, Object> dataMap : dataList) {
						if ( schoolClass.getClassId().toString().equalsIgnoreCase(dataMap.get("classId")+"") ) {
							echartsMap.put(schoolClass.getName(), dataMap.get(Constants.SUBJETCS.get(cv.getSubjectId())+"Average")+"#"+dataMap.get("top")+"#"+dataMap.get("bottom"));
						}											
					}
				}else {
					echartsMap.put(schoolClass.getName(), "0"+"#"+"0"+"#"+"0");
				}			
			}
			Gson gson3 = gb.create();
			String echarts= gson3.toJson(echartsMap);
			
			
			result.append("{\"total\"");
			result.append(":");
			result.append(dataList.size());
			result.append(",\"rows\":");
			result.append(data);
			result.append(",\"title\":");
			result.append(head);
			result.append(",\"echarts\":");
			result.append(echarts);	
			result.append("}");
		
			
			
			
			
			
            return result.toString();
			

	}
	
	
}
