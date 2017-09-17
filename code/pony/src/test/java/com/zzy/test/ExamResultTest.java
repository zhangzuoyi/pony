/**
 * @author WANGCHAO262
 * @version
 */

package com.zzy.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zzy.pony.model.Exam;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.Student;
import com.zzy.pony.model.Subject;
import com.zzy.pony.service.ExamResultService;
import com.zzy.pony.service.ExamService;
import com.zzy.pony.service.StudentService;
import com.zzy.pony.service.SubjectService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext.xml","/shiroFilter.xml"})
public class ExamResultTest {
	
	@Autowired
	private ExamResultService erService;
	@Autowired
	private ExamService examService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private SubjectService subjectService;
	 
	/**
	 * 随机产生一次考试的成绩并插入数据库
	 */
	@Test
	public void testInsertTestData(){
		//取得考试和科目
		Exam exam=examService.get(24);
		List<Subject> subjects=subjectService.findByExam(exam.getExamId());
		//按班级取得学生
		for(SchoolClass sc: exam.getSchoolClasses()){
			List<Student> students=studentService.findBySchoolClass(sc.getClassId());
			Random random = new Random();
			for(Subject subject: subjects){
				//入库
				erService.batchSave(getRandomScores(students, random ), exam.getExamId(), subject.getSubjectId(), "test");
			}
		}
		
	}
	//生成随机成绩
	private Map<Student, Float> getRandomScores(List<Student> students,Random random){
		Map<Student, Float> scores=new HashMap<Student, Float>();
		for(Student student: students){
			scores.put(student, Float.valueOf(getRandomScore(random)));
		}
		return scores;
	}
	private int getRandomScore(Random random){
		int i=0;
		while(true){
			int a=random.nextInt(60);
			int b=110-a;
			if(b<=100){
				i=b;
				break;
			}
		}
		return i;
	}
}
