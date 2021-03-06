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
import com.zzy.pony.dao.ExamTypeDao;
import com.zzy.pony.model.ExamType;
import com.zzy.pony.model.Grade;
import com.zzy.pony.service.ExamResultRankService;
import com.zzy.pony.service.ExamService;
import com.zzy.pony.service.GradeService;
import com.zzy.pony.service.StudentComprehensiveTrackService;
import com.zzy.pony.vo.ExamVo;
import com.zzy.pony.vo.ConditionVo;

@Controller
@RequestMapping(value = "/studentComprehensiveTrack")
public class StudentComprehensiveTrackController {
	
	
	@Autowired
	private GradeService gradeService;
	@Autowired
	private ExamTypeDao examTypeDao;
	@Autowired
	private ExamResultRankService examResultRankService;
	
	
	@Autowired
	private StudentComprehensiveTrackService studentComprehensiveTrackService;
	@Autowired
	private ExamService examService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){

		
		
		List<Grade> grades = gradeService.findAll();
		List<ExamType> examTypes = examTypeDao.findAll();				
		
		model.addAttribute("grades", grades);
		model.addAttribute("examTypes", examTypes);
		

	
		
		return "studentComprehensiveTrack/main";
	}
	@RequestMapping(value="findByCondition",method = RequestMethod.POST)
	@ResponseBody
	public String findByCondition(@RequestBody ConditionVo cv) {
		//新增默认全选功能
		if (cv.getExamTypeIds()==null || cv.getExamTypeIds().length == 0) {
			List<ExamType> examTypes = examTypeDao.findAll();				
			String[] examTypeArray = new String[examTypes.size()] ;
			for (int i = 0; i < examTypes.size(); i++) {
				examTypeArray[i] = examTypes.get(i).getTypeId()+"";
			}
			cv.setExamTypeIds(examTypeArray);
		}
		   int studentId = cv.getStudentId();
		
		
			StringBuilder result = new StringBuilder();
			List<Map<String, Object>> dataList =  studentComprehensiveTrackService.findByCondition(cv);
			List<Map<String, Object>> headList = new ArrayList<Map<String,Object>>();
			
			Map<String, Object> yearNameMap = new HashMap<String, Object>();
			yearNameMap.put("prop", "yearName");
			yearNameMap.put("label", "学年");
			Map<String, Object> termNameMap = new HashMap<String, Object>();
			termNameMap.put("prop", "termName");
			termNameMap.put("label", "学期");
			Map<String, Object> examNameMap = new HashMap<String, Object>();
			examNameMap.put("prop", "examName");
			examNameMap.put("label", "考试名称");						
			
			Map<String, Object> sumMap = new HashMap<String, Object>();
			sumMap.put("prop", "sum");
			sumMap.put("label", "总成绩");
			Map<String, Object> classRankMap = new HashMap<String, Object>();
			classRankMap.put("prop", "classRank");
			classRankMap.put("label", "班级排名");
			classRankMap.put("sortable", "true");
			Map<String, Object> gradeRankMap = new HashMap<String, Object>();
			gradeRankMap.put("prop", "gradeRank");
			gradeRankMap.put("label", "年级排名");
			gradeRankMap.put("sortable", "true");
			headList.add(yearNameMap);
			headList.add(termNameMap);
			headList.add(examNameMap);
			headList.add(sumMap);
			headList.add(classRankMap);
			headList.add(gradeRankMap);
		
			GsonBuilder gb = new GsonBuilder();
			Gson gson = gb.create();
			String data = gson.toJson(dataList);
			
			Gson gson2 = gb.create();
			String head= gson2.toJson(headList);
			
			
			
			//新增echarts数据获取xAxis(学年+学期+考试名)yAxis(班级排名+年级排名)
			Map<String, Object> echartsMap = new LinkedHashMap<String, Object>();
			List<Integer> exams= examResultRankService.findExamsByStudentId(studentId);
			
			for (Integer exam : exams) {
				ExamVo examVo = examService.getVo(exam);
				if (dataList!=null&&dataList.size()!=0) {
					for (Map<String, Object> dataMap : dataList) {
						
						echartsMap.put(examVo.getSchoolYear().getName()+examVo.getTerm().getName()+examVo.getName(), "0"+"#"+"0");

						if (examVo.getSchoolYear().getName().equalsIgnoreCase(dataMap.get("yearName")+"")&&
								examVo.getTerm().getName().equalsIgnoreCase(dataMap.get("termName")+"")&&
										examVo.getName().equalsIgnoreCase(dataMap.get("examName")+"")) {
							echartsMap.put(examVo.getSchoolYear().getName()+examVo.getTerm().getName()+examVo.getName(), dataMap.get("classRank")+"#"+dataMap.get("gradeRank"));
							break;
						}						
					}
				}else {
					echartsMap.put(examVo.getSchoolYear().getName()+examVo.getTerm().getName()+examVo.getName(), "0"+"#"+"0");					
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


