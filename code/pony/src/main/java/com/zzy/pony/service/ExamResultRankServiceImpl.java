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
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.Student;
import com.zzy.pony.vo.ExamResultRankVo;
import com.zzy.pony.vo.conditionVo;
@Service
@Transactional
public class ExamResultRankServiceImpl implements ExamResultRankService {
	@Autowired
	private ExamResultRankMapper examResultRankMapper;
	@Autowired
	private StudentService studentService;
	@Autowired
	private SchoolClassService schoolClassService;
	
	@Override
	public List<Map<String, Object>> findByCondition(conditionVo cv) {
		// TODO Auto-generated method stub
		List<ExamResultRankVo> ExamResultRankVos =  examResultRankMapper.findByCondition(cv);
		//排名以及成绩展示需要处理  学生ID为键
		List<Map<Integer,Map<String, Object>>> lists = new ArrayList<Map<Integer,Map<String,Object>>>();
		String[] subjects = cv.getSubjects();
		Map<Integer,Map<String, Object>> map = new HashMap<Integer, Map<String,Object>>();

		for (ExamResultRankVo examResultRankVo : ExamResultRankVos) {
			Map<String, Object>	map2=new HashMap<String, Object>();
			//第一条
			if (lists == null || lists.size() == 0) {				
				map2.put("className", examResultRankVo.getClassName());
				map2.put("studentNo", examResultRankVo.getStudentNo());
				map2.put("studentName", examResultRankVo.getStudentName());				
				for (int i = 0; i < subjects.length; i++) {
					if ((examResultRankVo.getSubjectId()+"").equalsIgnoreCase(subjects[i])) {
						map2.put(Constants.SUBJETCS.get(examResultRankVo.getSubjectId()),examResultRankVo.getScore() );
						map2.put("sum", examResultRankVo.getScore());
						break;
					}
				}
				map.put(examResultRankVo.getStudentId(), map2);				
			}else {
				if (map.containsKey(examResultRankVo.getStudentId())) {
					//包含
					Map<String, Object> map3 = map.get(examResultRankVo.getStudentId());
					for (int i = 0; i < subjects.length; i++) {
						if ((examResultRankVo.getSubjectId()+"").equalsIgnoreCase(subjects[i])) {
							map3.put(examResultRankVo.getSubjectId()+"",examResultRankVo.getScore() );
							map3.put("sum", examResultRankVo.getScore()+ Float.valueOf((map3.get("sum").toString())));
							break;
						}
					}										
				}else {
					map2.put("className", examResultRankVo.getClassName());
					map2.put("studentNo", examResultRankVo.getStudentNo());
					map2.put("studentName", examResultRankVo.getStudentName());					
					for (int i = 0; i < subjects.length; i++) {
						if ((examResultRankVo.getSubjectId()+"").equalsIgnoreCase(subjects[i])) {
							map2.put(Constants.SUBJETCS.get(examResultRankVo.getSubjectId()),examResultRankVo.getScore() );
							map2.put("sum", examResultRankVo.getScore());
							break;
						}
					}															
				}
				map.put(examResultRankVo.getStudentId(), map2);				
			}									
			lists.add(map);			
		}		
		sortByClassRank(lists,cv.getSchoolClasses());
		sortByGradeRank(lists,cv.getSchoolClasses());
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		for (Map<Integer,Map<String, Object>> list : lists) {
			  resultList.addAll(list.values()) ;
		}		
		return resultList;				
	}
	
	//班级排名   
	private List<Map<Integer,Map<String, Object>>> sortByClassRank(List<Map<Integer,Map<String, Object>>> lists,String[] classes){
		
		for (int i = 0; i < classes.length; i++) {
			Map<Integer, Float> map = new HashMap<Integer, Float>();
			List<Student> students = studentService.findBySchoolClass(Integer.valueOf(classes[i]));
			for (Student student : students) {//班级所有学生
				for (Map<Integer,Map<String, Object>> map2 : lists) {
					if (map2.containsKey(student.getStudentId())) {						
						map.put(student.getStudentId(),Float.valueOf(map2.get(student.getStudentId()).get("sum")+""));
						break;
					}										
				}
			
			}
			
			//map进行排序
			// 将map.entrySet()转换成list
	        List<Map.Entry<Integer, Float>> list = new ArrayList<Map.Entry<Integer, Float>>(map.entrySet());
	        // 通过比较器来实现排序
	        Collections.sort(list, new Comparator<Map.Entry<Integer, Float>>() {
	            @Override
	            public int compare(Map.Entry<Integer, Float> o1, Map.Entry<Integer, Float> o2) {
	                // 降序排序 分数值越高排名值越小
	                return o2.getValue().compareTo(o1.getValue());
	            }
	        });
	        for (int j = 0; j < list.size(); j++) {		
	           // System.out.println("key:"+mapping.getKey() + "  value:" + mapping.getValue());
	        	for (Map<Integer,Map<String, Object>> map3 : lists) {
					if (map3.containsKey(list.get(j).getKey())) {
						map3.get(list.get(j).getKey()).put("classRank", j+1);	
						break;
					}
     	
	        	}
	        }	
			}
		return lists;
		
		}
	//年级排名
	private List<Map<Integer,Map<String, Object>>> sortByGradeRank(List<Map<Integer,Map<String, Object>>> lists,String[] classes){
			//条件中的class须为同一年级里的班级，页面需要做联动控制
			List<Student> students = new ArrayList<Student>();
			for (int i = 0; i < classes.length; i++) {
				students.addAll(studentService.findBySchoolClass(Integer.valueOf(classes[i])));
			}
	
			Map<Integer, Float> map = new HashMap<Integer, Float>();
			for (Student student : students) {//年级所有学生
				for (Map<Integer,Map<String, Object>> map2 : lists) {
					if (map2.containsKey(student.getStudentId())) {						
						map.put(student.getStudentId(),Float.valueOf(map2.get(student.getStudentId()).get("sum")+""));
						break;
					}										
				}
			
			}
			
			//map进行排序
			// 将map.entrySet()转换成list
	        List<Map.Entry<Integer, Float>> list = new ArrayList<Map.Entry<Integer, Float>>(map.entrySet());
	        // 通过比较器来实现排序
	        Collections.sort(list, new Comparator<Map.Entry<Integer, Float>>() {
	            @Override
	            public int compare(Map.Entry<Integer, Float> o1, Map.Entry<Integer, Float> o2) {
	                // 降序排序 分数值越高排名值越小
	                return o2.getValue().compareTo(o1.getValue());
	            }
	        });
	        for (int j = 0; j < list.size(); j++) {		
	           // System.out.println("key:"+mapping.getKey() + "  value:" + mapping.getValue());
	        	for (Map<Integer,Map<String, Object>> map3 : lists) {
					if (map3.containsKey(list.get(j).getKey())) {
						map3.get(list.get(j).getKey()).put("gradeRank", j+1);	
						break;
					}
     	
	        	}
	        }	
			
		return lists;
		
		}
		
		
		

	

}
