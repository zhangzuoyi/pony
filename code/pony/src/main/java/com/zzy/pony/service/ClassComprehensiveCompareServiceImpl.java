package com.zzy.pony.service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.config.Constants;
import com.zzy.pony.mapper.ExamResultRankMapper;
import com.zzy.pony.model.Student;
import com.zzy.pony.model.Subject;
import com.zzy.pony.vo.ExamResultRankVo;
import com.zzy.pony.vo.ConditionVo;
@Service
@Transactional
public class ClassComprehensiveCompareServiceImpl implements ClassComprehensiveCompareService {
	@Autowired
	private ExamResultRankMapper examResultRankMapper;
	@Autowired
	private StudentService studentService;
	@Autowired
	private SchoolClassService schoolClassService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	
	
	@Override
	public List<Map<String, Object>> findByCondition(ConditionVo cv) {
		// TODO Auto-generated method stub	
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		//班级ID为键
		Map<String,Map<String, Object>> map = new HashMap<String, Map<String,Object>>();
		
		
		for (String classId : cv.getSchoolClasses()) {			
			//科目为考试下的科目
			List<Subject> subjectList = subjectService.findByExam(cv.getExamId());		
			String[] subjects = new String[subjectList.size()] ;
			for (int i = 0; i < subjectList.size(); i++) {
				subjects[i] = subjectList.get(i).getSubjectId()+"";
			}
			cv.setSubjects(subjects);
			cv.setClassId(Integer.valueOf(classId));
			cv.setSchoolClasses(null);
			List<ExamResultRankVo> ExamResultRankVos =  examResultRankMapper.findByCondition(cv);
			//学生总分
			Map<Integer, Float> studentSumMap = new HashMap<Integer, Float>();
			Map<String, Object>	map2=new HashMap<String, Object>();
			for (ExamResultRankVo examResultRankVo : ExamResultRankVos) {
			//第一条
				if (map2 == null || map2.size() == 0) {				
					map2.put("classId", classId);
					//学生人数   
					List<Student> list=studentService.findBySchoolClass(Integer.valueOf(classId));						
					map2.put("className", examResultRankVo.getClassName());
					map2.put("headTeacherName", examResultRankVo.getHeadTeacherName());
					map2.put("studentCount", list.size());	
					map2.put(Constants.SUBJETCS.get(examResultRankVo.getSubjectName()),examResultRankVo.getScore() );						
					studentSumMap.put(examResultRankVo.getStudentId(), examResultRankVo.getScore());
												
				}else {
					if (map2.containsKey(Constants.SUBJETCS.get(examResultRankVo.getSubjectId()))) {
						float sum=  examResultRankVo.getScore() + Float.valueOf(map2.get(Constants.SUBJETCS.get(examResultRankVo.getSubjectName()))+"");
						map2.put(Constants.SUBJETCS.get(examResultRankVo.getSubjectName()), sum);
					}else{
						map2.put(Constants.SUBJETCS.get(examResultRankVo.getSubjectName()),examResultRankVo.getScore() );						
					}
					if (studentSumMap.containsKey(examResultRankVo.getStudentId())) {
						float studentSum =  studentSumMap.get(examResultRankVo.getStudentId()) + examResultRankVo.getScore();
						studentSumMap.put(examResultRankVo.getStudentId(), studentSum);				
					}else {
						studentSumMap.put(examResultRankVo.getStudentId(), examResultRankVo.getScore());
					}
							
				}									
						
			}	
			int studentCount =0;
			if (map2.get("studentCount")!=null) {
				studentCount =   Integer.valueOf(map2.get("studentCount")+"");
			}
			
			float sumCount = 0F;
			
			for (Subject subject : subjectList) {
				if (map2.get(Constants.SUBJETCS.get(subject.getName()))!= null) {
					float subjectSum = Float.valueOf(map2.get(Constants.SUBJETCS.get(subject.getName()))+"");				
					//科目平均分
					if (studentCount!=0) {
						map2.put(Constants.SUBJETCS.get(subject.getName())+"Average", subjectSum/studentCount);				
					}else {
						map2.put(Constants.SUBJETCS.get(subject.getName())+"Average", 0);				
					}
					sumCount += subjectSum;
				}
				
			}		
			//总平均分
			if (studentCount!=0) {
				map2.put("sumAverage", sumCount/studentCount);	
			}else {
				map2.put("sumAverage", 0);	
			}
							
			//最高分(最低分)
			map2.putAll(sortBySumScore(studentSumMap));
		
			map.put(classId, map2);
			
			
			resultList.add(map.get(classId));
			
			
		}
		
		
		
		
		
		  
	
		
		
		
		
		return resultList;	
				
		
	}
	
	//最高分和最低分   
		private Map<String, Object> sortBySumScore(Map<Integer, Float>  unsortMap){	
				//map进行排序
			Map<String, Object> map = new HashMap<String, Object>();

			if (unsortMap != null && unsortMap.size()>0 ) {
				// 将map.entrySet()转换成list
		        List<Map.Entry<Integer, Float>> list = new ArrayList<Map.Entry<Integer, Float>>(unsortMap.entrySet());
		        // 通过比较器来实现排序
		        Collections.sort(list, new Comparator<Map.Entry<Integer, Float>>() {
		            @Override
		            public int compare(Map.Entry<Integer, Float> o1, Map.Entry<Integer, Float> o2) {
		                // 降序排序 分数值越高排名值越小
		                return o2.getValue().compareTo(o1.getValue());
		            }
		        });		        		        	
		        	map.put("top", list.get(0).getValue());
		        	map.put("bottom", list.get(list.size()-1).getValue());
			}else {
				map.put("top", 0);
	        	map.put("bottom",0);
			}
				        	
				
			return map;
			
			}
		
		
		
		

	

}
