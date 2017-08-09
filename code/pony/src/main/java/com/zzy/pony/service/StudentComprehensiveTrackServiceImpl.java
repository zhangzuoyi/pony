package com.zzy.pony.service;


import java.awt.RenderingHints.Key;
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
import com.zzy.pony.dao.ExamDao;
import com.zzy.pony.mapper.ExamResultRankMapper;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Student;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.ExamResultRankVo;
import com.zzy.pony.vo.ConditionVo;
@Service
@Transactional
public class StudentComprehensiveTrackServiceImpl implements StudentComprehensiveTrackService {
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
	@Autowired
	private ExamResultRankService examResultRankService;
	@Autowired
	private ExamDao examDao;
	
	@Override
	public List<Map<String, Object>> findByCondition(ConditionVo cv) {
		// TODO Auto-generated method stub		
		//排名以及成绩展示需要处理  学生ID为键		
		//总成绩为考试对应科目的总成绩				
		//根据studentId获取所有参加的examId
		List<Map<String, Object>>  resultList = new ArrayList<Map<String,Object>>();
				List<Integer> examIds =	examResultRankService.findExamsByStudentId(cv.getStudentId());
				int classId = cv.getClassId();
				int studentId = cv.getStudentId();
				
				if (examIds!= null && examIds.size() >0) {
					for (Integer examId : examIds) {
						Exam exam = examDao.findOne(examId);
						cv.setExamId(examId);
						//科目为考试下的科目
						
						List<Subject> subjectList = subjectService.findByExam(examId);		
						String[] subjects = new String[subjectList.size()] ;
						for (int i = 0; i < subjectList.size(); i++) {
							subjects[i] = subjectList.get(i).getSubjectId()+"";
						}
						cv.setSubjects(subjects);
						//班级为考试下的班级
						List<SchoolClass> schoolClassList = exam.getSchoolClasses();
						String[] schoolClasses = new String[schoolClassList.size()] ;
						for (int i = 0; i < schoolClassList.size(); i++) {
							schoolClasses[i] = schoolClassList.get(i).getClassId()+"";
						}
						cv.setSchoolClasses(schoolClasses);
						
						//int classId = cv.getClassId();
						//int studentId = cv.getStudentId();
						//将classId和studentId置空
						cv.setClassId(0);
						cv.setStudentId(0);
						//学年
							cv.setYearId(exam.getSchoolYear().getYearId());
						//学期
							cv.setTermId(exam.getTerm().getTermId());
								
								List<ExamResultRankVo> ExamResultRankVos =  examResultRankMapper.findByCondition(cv);
								
								Map<Integer,Map<String, Object>> map = new HashMap<Integer, Map<String,Object>>();

								for (ExamResultRankVo examResultRankVo : ExamResultRankVos) {
									Map<String, Object>	map2=new HashMap<String, Object>();
									//第一条
									if (map == null || map.size() == 0) {	
										//综合跟踪
										map2.put("yearName", examResultRankVo.getYearName());
										map2.put("termName", examResultRankVo.getTermName());
										map2.put("examName", examResultRankVo.getExamName());
										
										
										//排名
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
									//	lists.add(map);	//需要新增
									}else {
										if (map.containsKey(examResultRankVo.getStudentId())) {
											//包含
											map2 = map.get(examResultRankVo.getStudentId());
											for (int i = 0; i < subjects.length; i++) {
												if ((examResultRankVo.getSubjectId()+"").equalsIgnoreCase(subjects[i])) {
													map2.put(Constants.SUBJETCS.get(examResultRankVo.getSubjectId()),examResultRankVo.getScore() );
													map2.put("sum", examResultRankVo.getScore()+ Float.valueOf((map2.get("sum").toString())));
													break;
												}
											}	
											map.put(examResultRankVo.getStudentId(), map2);//无需新增到list
										}else {
											//综合跟踪
											map2.put("yearName", examResultRankVo.getYearName());
											map2.put("termName", examResultRankVo.getTermName());
											map2.put("examName", examResultRankVo.getExamName());
											//排名
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
											//lists.add(map);	//需要新增
											
										}
														
									}									
											
								}	
								String[] rankClass ={classId+""};
								sortByClassRank(map,rankClass);//仅对所选班级进行排序
								sortByGradeRank(map,schoolClasses);	
								
								if (map!=null && map.size()!=0 && map.get(studentId)!= null) {
									resultList.add(map.get(studentId));
								}
								
			
					}
					
					
					
				}
		
		
	
		return resultList;	
				
		
	}
	
	
	@Override
	public Map<Integer, String> findExamRank(int examId) {
		// TODO Auto-generated method stub
		Map<Integer, String> result = new HashMap<Integer, String>();
		Exam exam = examDao.findOne(examId);
		ConditionVo cv  = new ConditionVo();
		cv.setExamId(examId);
		//科目为考试下的科目		
		List<Subject> subjectList = subjectService.findByExam(examId);		
		String[] subjects = new String[subjectList.size()] ;
		for (int i = 0; i < subjectList.size(); i++) {
			subjects[i] = subjectList.get(i).getSubjectId()+"";
		}
		cv.setSubjects(subjects);
		//班级为考试下的班级
		List<SchoolClass> schoolClassList = exam.getSchoolClasses();
		String[] schoolClasses = new String[schoolClassList.size()] ;
		for (int i = 0; i < schoolClassList.size(); i++) {
			schoolClasses[i] = schoolClassList.get(i).getClassId()+"";
		}
		cv.setSchoolClasses(schoolClasses);
		
		//学年
			cv.setYearId(exam.getSchoolYear().getYearId());
		//学期
			cv.setTermId(exam.getTerm().getTermId());
				
				List<ExamResultRankVo> ExamResultRankVos =  examResultRankMapper.findByCondition(cv);
				
				Map<Integer,Map<String, Object>> map = new HashMap<Integer, Map<String,Object>>();

				for (ExamResultRankVo examResultRankVo : ExamResultRankVos) {
					Map<String, Object>	map2=new HashMap<String, Object>();
					//第一条
					if (map == null || map.size() == 0) {	
						//综合跟踪
						map2.put("yearName", examResultRankVo.getYearName());
						map2.put("termName", examResultRankVo.getTermName());
						map2.put("examName", examResultRankVo.getExamName());											
						//排名
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
					//	lists.add(map);	//需要新增
					}else {
						if (map.containsKey(examResultRankVo.getStudentId())) {
							//包含
							map2 = map.get(examResultRankVo.getStudentId());
							for (int i = 0; i < subjects.length; i++) {
								if ((examResultRankVo.getSubjectId()+"").equalsIgnoreCase(subjects[i])) {
									map2.put(Constants.SUBJETCS.get(examResultRankVo.getSubjectId()),examResultRankVo.getScore() );
									map2.put("sum", examResultRankVo.getScore()+ Float.valueOf((map2.get("sum").toString())));
									break;
								}
							}	
							map.put(examResultRankVo.getStudentId(), map2);//无需新增到list
						}else {
							//综合跟踪
							map2.put("yearName", examResultRankVo.getYearName());
							map2.put("termName", examResultRankVo.getTermName());
							map2.put("examName", examResultRankVo.getExamName());
							//排名
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
							//lists.add(map);	//需要新增			
						}							
					}															
				}				
				sortByGradeRank(map,schoolClasses);	
				for ( Integer studentId : map.keySet()) {
					result.put(studentId, String.valueOf(map.get(studentId).get("gradeRank")));
				}
				
		return result;
	}


	//班级排名   
	private Map<Integer,Map<String, Object>> sortByClassRank(Map<Integer,Map<String, Object>> unsortMap,String[] classes){
		
		for (int i = 0; i < classes.length; i++) {
			Map<Integer, Float> map = new HashMap<Integer, Float>();
			List<Student> students = studentService.findBySchoolClass(Integer.valueOf(classes[i]));
			for (Student student : students) {//班级所有学生
				
					if (unsortMap.containsKey(student.getStudentId())) {						
						map.put(student.getStudentId(),Float.valueOf(unsortMap.get(student.getStudentId()).get("sum")+""));
						
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
					
					if (unsortMap.containsKey(list.get(j).getKey()) && j != 0) {
						//新增相同处理逻辑
						if (Float.valueOf(unsortMap.get(list.get(j).getKey()).get("sum")+"").floatValue() == list.get(j-1).getValue().floatValue() ) {
							unsortMap.get(list.get(j).getKey()).put("classRank", unsortMap.get(list.get(j-1).getKey()).get("classRank"));															
						}else {
							unsortMap.get(list.get(j).getKey()).put("classRank", j+1);												
						}						
					}else {
						unsortMap.get(list.get(j).getKey()).put("classRank", j+1);	
					}	
     	
	        	
	        }	
			}
		return unsortMap;
		
		}
	//年级排名
	private Map<Integer,Map<String, Object>> sortByGradeRank(Map<Integer,Map<String, Object>> unsortMap,String[] classes){
			//条件中的class须为同一年级里的班级，页面需要做联动控制
			List<Student> students = new ArrayList<Student>();
			for (int i = 0; i < classes.length; i++) {
				students.addAll(studentService.findBySchoolClass(Integer.valueOf(classes[i])));
			}
	
			Map<Integer, Float> map = new HashMap<Integer, Float>();
			for (Student student : students) {//年级所有学生
				
					if (unsortMap.containsKey(student.getStudentId())) {						
						map.put(student.getStudentId(),Float.valueOf(unsortMap.get(student.getStudentId()).get("sum")+""));
						
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
	        	
	        		/*if (unsortMap.containsKey(list.get(j).getKey()) && j != 0) {
						//新增相同处理逻辑
						if (Float.valueOf(unsortMap.get(list.get(j).getKey()).get("sum")+"").floatValue() == list.get(j-1).getValue().floatValue() ) {
							unsortMap.get(list.get(j).getKey()).put("gradeRank", unsortMap.get(list.get(j-1).getKey()).get("gradeRank"));															
						}else {
							unsortMap.get(list.get(j).getKey()).put("gradeRank", j+1);												
						}	
	        				
					}else {
						unsortMap.get(list.get(j).getKey()).put("gradeRank", j+1);	
					}*/	
	        		
	        		//去除相同处理逻辑，在生成考生号时即使成绩相同考生号仍不相同
        			unsortMap.get(list.get(j).getKey()).put("gradeRank", j+1);
	        }	
			
		return unsortMap;
		
		}
		
		
		

	

}
