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
import com.zzy.pony.model.ExamType;
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Term;
import com.zzy.pony.service.ExamResultRankService;
import com.zzy.pony.service.GradeService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.vo.conditionVo;

@Controller
@RequestMapping(value = "/examResultRank")
public class ExamResultRankController {
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
	private ExamResultRankService examResultRankService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){

		List<SchoolYear> schoolYears = schoolYearService.findAll();
		List<Term> terms = termService.findAll();
		List<Grade> grades = gradeService.findAll();
		List<ExamType> examTypes = examTypeDao.findAll();
		List<SchoolClass> schoolClasses = schoolClassService.findAll();
		List<Subject> subjects = subjectService.findMajorSubject();//查询主修科目
		model.addAttribute("schoolYears", schoolYears);
		model.addAttribute("terms", terms);
		model.addAttribute("grades", grades);
		model.addAttribute("examTypes", examTypes);
		model.addAttribute("schoolClasses", schoolClasses);
		model.addAttribute("subjects", subjects);

	
		
		return "examResultRank/main";
	}
	@SuppressWarnings("serial")
	@RequestMapping(value="findByCondition",method = RequestMethod.POST)
	@ResponseBody
	public String findByCondition(@RequestBody conditionVo cv) {
			StringBuilder result = new StringBuilder();
			List<Map<String, Object>> dataList =  examResultRankService.findByCondition(cv);
			List<Map<String, Object>> headList = new ArrayList<Map<String,Object>>();
			
			Map<String, Object> classNameMap = new HashMap<String, Object>();
			classNameMap.put("field", "className");
			classNameMap.put("title", "班级");
			Map<String, Object> studentNoMap = new HashMap<String, Object>();
			studentNoMap.put("field", "studentNo");
			studentNoMap.put("title", "学号");
			Map<String, Object> studentNameMap = new HashMap<String, Object>();
			studentNameMap.put("field", "studentName");
			studentNameMap.put("title", "姓名");
			
			String[] subjects  =   cv.getSubjects();
			for (String subjectId : subjects) {
				Map<String, Object> headMap = new HashMap<String, Object>();
				Subject subject = subjectService.get(Integer.valueOf(subjectId));
				headMap.put("field", Constants.SUBJETCS.get(subject.getSubjectId()));
				headMap.put("title", subject.getName());
				headList.add(headMap);
			}
			
			Map<String, Object> sumMap = new HashMap<String, Object>();
			sumMap.put("field", "sum");
			sumMap.put("title", "总成绩");
			Map<String, Object> classRankMap = new HashMap<String, Object>();
			classRankMap.put("field", "classRank");
			classRankMap.put("title", "班级排名");
			Map<String, Object> gradeRankMap = new HashMap<String, Object>();
			gradeRankMap.put("field", "gradeRank");
			gradeRankMap.put("title", "年级排名");
			headList.add(classNameMap);
			headList.add(studentNoMap);
			headList.add(studentNameMap);
			headList.add(sumMap);
			headList.add(classRankMap);
			headList.add(gradeRankMap);
		
			GsonBuilder gb = new GsonBuilder();
			Gson gson = gb.create();
			String data = gson.toJson(dataList);
			Gson gson2 = gb.create();
			String head= gson2.toJson(headList);
			
			result.append("{\"total\"");
			result.append(":");
			result.append(dataList.size());
			result.append(",\"rows\":");
			result.append(data);
			result.append(",\"title\":");
			result.append(head);
			result.append("}");
		
            return result.toString();
			

	}
	
	
}
