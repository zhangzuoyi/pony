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
import com.zzy.pony.exam.model.Examinee;
import com.zzy.pony.exam.service.ExamineeService;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.service.ClassComprehensiveCompareService;
import com.zzy.pony.service.ComprehensiveRankService;
import com.zzy.pony.service.GradeService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.vo.ConditionVo;

@Controller
@RequestMapping(value = "/comprehensiveRank")
public class ComprehensiveRankController {
	
	@Autowired
	private ExamineeService examineeService;
	@Autowired
	private ComprehensiveRankService comprehensiveRankService;
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){			
		return "comprehensiveRank/main";
	}
	
	@RequestMapping(value="rank",method=RequestMethod.POST)
	@ResponseBody
	public String rank(@RequestBody final ConditionVo cv){
		String result = "1";
		List<Examinee> examineees = examineeService.findByExamAndTotalScoreIsNull(cv.getExamId());
		if (examineees == null) {
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
	
	/*@RequestMapping(value="findByCondition",method = RequestMethod.POST)
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
			List<Map<String, Object>> dataList =  classComprehensiveCompareService.findByCondition(cv);
			List<Map<String, Object>> headList = new ArrayList<Map<String,Object>>();
			
			Map<String, Object> classNameMap = new HashMap<String, Object>();
			classNameMap.put("prop", "className");
			classNameMap.put("label", "班级");
			Map<String, Object> headTeacherNameMap = new HashMap<String, Object>();
			headTeacherNameMap.put("prop", "headTeacherName");
			headTeacherNameMap.put("label", "任课教师");
			Map<String, Object> studentCountMap = new HashMap<String, Object>();
			studentCountMap.put("prop", "studentCount");
			studentCountMap.put("label", "考生人数");
			
			String[] subjects  =   cv.getSubjects();
			for (String subjectId : subjects) {
				Map<String, Object> headMap = new HashMap<String, Object>();
				Subject subject = subjectService.get(Integer.valueOf(subjectId));
				headMap.put("prop", Constants.SUBJETCS.get(subject.getName())+"Average");
				headMap.put("label", subject.getName()+"平均分");
				headList.add(headMap);
			}
			
			Map<String, Object> sumAverageMap = new HashMap<String, Object>();
			sumAverageMap.put("prop", "sumAverage");
			sumAverageMap.put("label", "平均分");
			Map<String, Object> topMap = new HashMap<String, Object>();
			topMap.put("prop", "top");
			topMap.put("label", "最高分");
			Map<String, Object> bottomMap = new HashMap<String, Object>();
			bottomMap.put("prop", "bottom");
			bottomMap.put("label", "最低分");
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
			
			
			
			
			
			
			
			result.append("{\"total\"");
			result.append(":");
			result.append(dataList.size());
			result.append(",\"rows\":");
			result.append(data);
			result.append(",\"title\":");
			result.append(head);			
			result.append("}");
	  
					
			
			
			
			
            return result.toString();
			

	}*/
	
	
}
