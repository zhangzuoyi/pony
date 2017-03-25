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
import com.zzy.pony.dao.ExamTypeDao;
import com.zzy.pony.dao.SchoolClassDao;
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Term;
import com.zzy.pony.service.ClassComprehensiveCompareService;
import com.zzy.pony.service.GradeService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.vo.conditionVo;

@Controller
@RequestMapping(value = "/classComprehensiveCompare")
public class ClassComprehensiveCompareController {
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
	private ClassComprehensiveCompareService classComprehensiveCompareService;
	
	
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
		

	
		
		return "classComprehensiveCompare/main";
	}
	@RequestMapping(value="findByCondition",method = RequestMethod.POST)
	@ResponseBody
	public String findByCondition(@RequestBody conditionVo cv) {
		//新增默认全选功能
			List<SchoolClass> schoolClasses = new ArrayList<SchoolClass>();
		
		if (cv.getSchoolClasses()==null || cv.getSchoolClasses().length == 0) {
			schoolClasses = schoolClassService.findByGrade(cv.getGradeId());
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
			List<Map<String, Object>> dataList =  classComprehensiveCompareService.findByCondition(cv);
			List<Map<String, Object>> headList = new ArrayList<Map<String,Object>>();
			
			Map<String, Object> classNameMap = new HashMap<String, Object>();
			classNameMap.put("field", "className");
			classNameMap.put("title", "班级");
			Map<String, Object> headTeacherNameMap = new HashMap<String, Object>();
			headTeacherNameMap.put("field", "headTeacherName");
			headTeacherNameMap.put("title", "任课教师");
			Map<String, Object> studentCountMap = new HashMap<String, Object>();
			studentCountMap.put("field", "studentCount");
			studentCountMap.put("title", "考生人数");
			
			String[] subjects  =   cv.getSubjects();
			for (String subjectId : subjects) {
				Map<String, Object> headMap = new HashMap<String, Object>();
				Subject subject = subjectService.get(Integer.valueOf(subjectId));
				headMap.put("field", Constants.SUBJETCS.get(subject.getSubjectId())+"Average");
				headMap.put("title", subject.getName()+"平均分");
				headList.add(headMap);
			}
			
			Map<String, Object> sumAverageMap = new HashMap<String, Object>();
			sumAverageMap.put("field", "sumAverage");
			sumAverageMap.put("title", "平均分");
			Map<String, Object> topMap = new HashMap<String, Object>();
			topMap.put("field", "top");
			topMap.put("title", "最高分");
			Map<String, Object> bottomMap = new HashMap<String, Object>();
			bottomMap.put("field", "bottom");
			bottomMap.put("title", "最低分");
			headList.add(classNameMap);
			headList.add(headTeacherNameMap);
			headList.add(studentCountMap);
			headList.add(sumAverageMap);
			headList.add(topMap);
			headList.add(bottomMap);
		
			GsonBuilder gb = new GsonBuilder();
			Gson gson = gb.create();
			String data = gson.toJson(dataList);
			Gson gson2 = gb.create();
			String head= gson2.toJson(headList);
			
			//新增echarts数据获取xAxis(班级)yAxis(平均分最高分最低分)
			
			
			Map<String, Object> echartsMap = new HashMap<String, Object>();
	
			for (SchoolClass schoolClass : schoolClasses) {			
				if (dataList!=null&&dataList.size()!=0) {
					for (Map<String, Object> dataMap : dataList) {
						if ( schoolClass.getClassId().toString().equalsIgnoreCase(dataMap.get("classId")+"") ) {
							echartsMap.put(schoolClass.getName(), dataMap.get("sumAverage")+"#"+dataMap.get("top")+"#"+dataMap.get("bottom"));
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
