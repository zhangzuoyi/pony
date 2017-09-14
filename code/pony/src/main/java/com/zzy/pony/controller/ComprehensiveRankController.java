package com.zzy.pony.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;













import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.zzy.pony.service.*;
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
import com.zzy.pony.exam.model.Examinee;
import com.zzy.pony.exam.service.ExamineeService;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.vo.ConditionVo;

@Controller
@RequestMapping(value = "/comprehensiveRank")
public class ComprehensiveRankController {
	
	@Autowired
	private ExamineeService examineeService;
	@Autowired
	private ComprehensiveRankService comprehensiveRankService;
	@Autowired
	private ExamResultService examResultService;
	@Autowired
	private SchoolYearService schoolYearService;
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){			
		return "comprehensiveRank/main";
	}
	
	@RequestMapping(value="rank",method=RequestMethod.POST)
	@ResponseBody
	public String rank(@RequestBody final ConditionVo cv){
		String result = "1";
		List<Examinee> examineees = examineeService.findByExamAndTotalScoreIsNull(cv.getExamId());
		if (examineees == null || examineees.size() == 0) {
			result = "0";
			return result;
		}else {
			//增加熔断		
			ExecutorService executor = Executors.newSingleThreadExecutor();
			FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					// TODO Auto-generated method stub
					comprehensiveRankService.rankExaminee(cv);
					comprehensiveRankService.rankExamReult(cv);				
					return null;
				}
				
			});
		executor.execute(future);
		
		try {
			future.get(5, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			future.cancel(true);
			executor.shutdown();			
		}						
		}
		
		return result;			
		
		
	}
	
	@RequestMapping(value="findByCondition",method = RequestMethod.POST)
	@ResponseBody
	public String findByCondition(@RequestBody ConditionVo cv) {

			StringBuilder result = new StringBuilder();
			SchoolYear year = schoolYearService.getCurrent();
			List<Map<String, Object>> dataList =  comprehensiveRankService.findRankByExam(cv.getExamId(),year.getYearId(),cv.getGradeId());
			List<Map<String, Object>> headList = new ArrayList<Map<String,Object>>();

			Map<String, Object> studentNameMap = new HashMap<String, Object>();
			studentNameMap.put("prop", "studentName");
			studentNameMap.put("label", "姓名");
			Map<String, Object> classNameMap = new HashMap<String, Object>();
			classNameMap.put("prop", "seq");
			classNameMap.put("label", "班级");
			headList.add(studentNameMap);
			headList.add(classNameMap);

			List<Subject> subjects = examResultService.findSubjectByExam(cv.getExamId());
			for (Subject subject : subjects) {
				Map<String, Object> headMap = new HashMap<String, Object>();
				headMap.put("prop", Constants.SUBJETCS.get(subject.getName()));
				headMap.put("label", subject.getName());
				headList.add(headMap);
				Map<String, Object> headRankMap = new HashMap<String, Object>();
				headRankMap.put("prop", Constants.SUBJETCS.get(subject.getName())+"Rank");
				headRankMap.put("label", "年级排名");
				headList.add(headRankMap);

			}

			Map<String, Object> totalScoreMap = new HashMap<String, Object>();
			totalScoreMap.put("prop", "totalScore");
			totalScoreMap.put("label", "总得分");
			Map<String, Object> classRankMap = new HashMap<String, Object>();
			classRankMap.put("prop", "classRank");
			classRankMap.put("label", "班级排名");
			Map<String, Object> gradeRankMap = new HashMap<String, Object>();
			gradeRankMap.put("prop", "gradeRank");
			gradeRankMap.put("label", "年级排名");
			


			headList.add(totalScoreMap);
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
