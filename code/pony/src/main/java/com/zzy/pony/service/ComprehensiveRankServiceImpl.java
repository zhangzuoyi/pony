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

import com.zzy.pony.dao.ExamResultDao;
import com.zzy.pony.exam.dao.ExamineeDao;
import com.zzy.pony.exam.model.Examinee;
import com.zzy.pony.exam.service.ExamineeService;
import com.zzy.pony.mapper.ExamResultMapper;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.ExamResult;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.Student;
import com.zzy.pony.model.Subject;
import com.zzy.pony.vo.ConditionVo;








@Service
@Transactional
public class ComprehensiveRankServiceImpl implements ComprehensiveRankService {

	@Autowired
	private ExamResultMapper examResultMapper;
	@Autowired
	private ExamResultDao examResultDao;
	@Autowired
	private ExamService examService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SchoolClassService schoolClassService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private ExamineeService examineeService;
	@Autowired
	private ExamineeDao examineeDao;
	
	
	
	@Override
	public void rankExamReult(ConditionVo cv) {
		// TODO Auto-generated method stub
		List<Integer> subjectIds = examResultMapper.findSubjectByExam(cv.getExamId());
		Exam exam = examService.get(cv.getExamId());	
		for (Integer subjectId : subjectIds) {
			Subject subject = subjectService.get(subjectId);
			List<ExamResult> examResults = examResultDao.findByExamAndSubject(exam, subject);//所有该科目的成绩
			Map<Integer, Map<String, Float>> unsortMap = new HashMap<Integer, Map<String,Float>>();
			for (ExamResult examResult : examResults) {
				Map<String, Float> innerMap = new HashMap<String, Float>();
				innerMap.put("sum", examResult.getScore());
				unsortMap.put(examResult.getStudent().getStudentId(), innerMap);
			}
			sortByRank(unsortMap, cv.getGradeId());
			for (Integer studentId : unsortMap.keySet()) {
				Student student = new Student();
				student.setStudentId(studentId);
				ExamResult examResult = examResultDao.findByExamAndSubjectAndStudent(exam, subject, student);
				examResult.setClassRank(unsortMap.get(studentId).get("classRank").intValue());
				examResult.setGradeRank(unsortMap.get(studentId).get("gradeRank").intValue());
				examResultDao.save(examResult);
			}			
		}								
	}

	@Override
	public void rankExaminee(ConditionVo cv) {
		// TODO Auto-generated method stub
		//计算total_score
		//List<Examinee> exmainees = examineeService.findByExamId(cv.getExamId());
		List<Map<String, Object>> totalScoreMap = examResultMapper.findTotalScoreByExam(cv.getExamId());
		Map<Integer, Map<String, Float>> unsortMap = new HashMap<Integer, Map<String,Float>>();
		for (Map<String, Object> map : totalScoreMap) {
			Map<String, Float> innerMap = new HashMap<String, Float>();
			//System.out.println(map.get("totalScore").getClass().getName()); 
			innerMap.put("sum", Float.valueOf(map.get("totalScore").toString()));
			unsortMap.put(Integer.valueOf(map.get("studentId").toString()), innerMap);
		}
		sortByRank(unsortMap, cv.getGradeId());
		for (Integer studentId : unsortMap.keySet()) {
			Examinee examinee = examineeService.findByExamAndStudent(cv.getExamId(), studentId);
			examinee.setTotalScore(unsortMap.get(studentId).get("sum"));
			examinee.setClassRank(unsortMap.get(studentId).get("classRank").intValue());
			examinee.setGradeRank(unsortMap.get(studentId).get("gradeRank").intValue());
			examineeDao.save(examinee);									
		}
		
		
		
	}
	
	private Map<Integer, Map<String, Float>> sortByRank(Map<Integer, Map<String, Float>> unsortMap,int gradeId ){
		Map<Integer, Float> map = new HashMap<Integer, Float>();	
		//年级排名
		for (Integer studentId : unsortMap.keySet()) {		
			map.put(studentId, unsortMap.get(studentId).get("sum"));
			
		}
		List<Map.Entry<Integer, Float>> list = new ArrayList<Map.Entry<Integer,Float>>(map.entrySet());
		
		Collections.sort(list, new Comparator<Map.Entry<Integer, Float>>() {

			@Override
			public int compare(Map.Entry<Integer, Float> o1,
					Map.Entry<Integer, Float> o2) {
				// TODO Auto-generated method stub
				return o2.getValue().compareTo(o1.getValue());
			}				
		});
		for (int i = 0; i < list.size(); i++) {
			if (unsortMap.containsKey(list.get(i).getKey()) && i != 0) {
				//新增相同处理逻辑
				if (unsortMap.get(list.get(i).getKey()).get("sum") == list.get(i-1).getValue() ) {
					unsortMap.get(list.get(i).getKey()).put("gradeRank", unsortMap.get(list.get(i-1).getKey()).get("gradeRank"));															
				}else {
					unsortMap.get(list.get(i).getKey()).put("gradeRank", (float)(i+1));												
				}	
    				
			}else {
				unsortMap.get(list.get(i).getKey()).put("gradeRank", (float)(i+1));	
			}
		}		
		//班级排名
		List<SchoolClass> schoolClasses = schoolClassService.findByGrade(gradeId);
		for (SchoolClass schoolClass : schoolClasses) {
			List<Student> students = studentService.findBySchoolClass(schoolClass.getClassId());
			map.clear();
			for (Student student : students) {
				if (unsortMap.containsKey(student.getStudentId())) {
					map.put(student.getStudentId(), unsortMap.get(student.getStudentId()).get("sum"));
				}
			}
			List<Map.Entry<Integer, Float>> classList = new ArrayList<Map.Entry<Integer,Float>>(map.entrySet());
			
			Collections.sort(classList, new Comparator<Map.Entry<Integer, Float>>() {

				@Override
				public int compare(Map.Entry<Integer, Float> o1,
						Map.Entry<Integer, Float> o2) {
					// TODO Auto-generated method stub
					return o2.getValue().compareTo(o1.getValue());
				}				
			});
			for (int i = 0; i < classList.size(); i++) {
				if (unsortMap.containsKey(classList.get(i).getKey()) && i != 0) {
					//新增相同处理逻辑
					if (unsortMap.get(classList.get(i).getKey()).get("sum") == classList.get(i-1).getValue() ) {
						unsortMap.get(classList.get(i).getKey()).put("classRank", unsortMap.get(classList.get(i-1).getKey()).get("classRank"));															
					}else {
						unsortMap.get(classList.get(i).getKey()).put("classRank", (float)(i+1));												
					}	
	    				
				}else {
					unsortMap.get(classList.get(i).getKey()).put("classRank", (float)(i+1));	
				}
			}
			
			
		}
		
		return unsortMap;
	}	

	


	
	
}
