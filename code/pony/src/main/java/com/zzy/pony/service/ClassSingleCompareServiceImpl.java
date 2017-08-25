package com.zzy.pony.service;


import java.util.ArrayList;
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
public class ClassSingleCompareServiceImpl implements ClassSingleCompareService {
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
		Subject subject  = subjectService.get(cv.getSubjectId());
		
		for (String classId : cv.getSchoolClasses()) {			
			//科目为页面勾选的科目
			
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
					map2.put("teacherName", examResultRankVo.getTeacherName());
					map2.put("studentCount", list.size());	
					map2.put(Constants.SUBJETCS.get(examResultRankVo.getSubjectName()),examResultRankVo.getScore() );						
					map2.put("top", examResultRankVo.getScore());//最高分
					map2.put("bottom", examResultRankVo.getScore());//最低分
												
				}else {
					
						float sum=  examResultRankVo.getScore() + Float.valueOf(map2.get(Constants.SUBJETCS.get(examResultRankVo.getSubjectName()))+"");
						map2.put(Constants.SUBJETCS.get(examResultRankVo.getSubjectName()), sum);
						if (examResultRankVo.getScore() > Float.valueOf(map2.get("top")+"")) {
							map2.put("top", examResultRankVo.getScore());
						}
						if (examResultRankVo.getScore() < Float.valueOf(map2.get("bottom")+"")) {
							map2.put("bottom", examResultRankVo.getScore());
						}					
				}									
						
			}	
			int studentCount =0;
			if (map2.get("studentCount")!=null) {
				studentCount =   Integer.valueOf(map2.get("studentCount")+"");
			}
			
			
			
			
				if (map2.get(Constants.SUBJETCS.get(subject.getName()))!= null) {
					float subjectSum = Float.valueOf(map2.get(Constants.SUBJETCS.get(subject.getName()))+"");				
					//科目平均分
					if (studentCount!=0) {
						map2.put(Constants.SUBJETCS.get(subject.getName())+"Average", subjectSum/studentCount);				
					}else {
						map2.put(Constants.SUBJETCS.get(subject.getName())+"Average", 0);				
					}					
				}
				
	
			map.put(classId, map2);
			
			
			resultList.add(map.get(classId));
			
			
		}
				
		return resultList;	
				
		
	}
	
	
		
		
		
		

	

}
