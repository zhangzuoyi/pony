package com.zzy.pony.exam.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.zzy.pony.exam.dao.ExamineeDao;
import com.zzy.pony.exam.mapper.ExamineeMapper;
import com.zzy.pony.exam.model.Examinee;
import com.zzy.pony.exam.vo.ExamineeVo;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Student;
import com.zzy.pony.model.Term;
import com.zzy.pony.service.ExamService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.StudentComprehensiveTrackService;
import com.zzy.pony.service.StudentService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.util.ReadExcelUtils;
import com.zzy.pony.vo.ExamVo;

@Service
@Transactional
public class ExamineeServiceImpl implements ExamineeService {

	@Autowired
	private SchoolClassService schoolClassService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private ExamineeDao examineeDao;
	@Autowired
	private ExamService examService;
	@Autowired
	private ExamineeMapper examineeMapper;
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	@Autowired
	private StudentComprehensiveTrackService studentComprehensiveTrackService;

	@Override
	public synchronized void generateNo(int examId, int gradeId, String prefixNo, int bitNo) {
		if (isGenerateShow(examId, gradeId)) {// 先判断未产生考生号，避免重复生成
			List<Examinee> examinees = new ArrayList<Examinee>();
			Exam exam = examService.get(examId);
			SchoolYear year = schoolYearService.getCurrent();
			List<SchoolClass> schoolClasses = schoolClassService.findByYearAndGradeOrderBySeq(year.getYearId(), gradeId);
			Map<Integer, String> map = this.generateRegNo(examId, gradeId, prefixNo, bitNo);
			for (SchoolClass schoolClass : schoolClasses) {
				List<Student> students = studentService.findBySchoolClassAndStatus(schoolClass.getClassId(),StudentService.STUDENT_STATUS_ZD);
				for (Student student : students) {
					Examinee examinee = new Examinee();
					examinee.setExam(exam);
					examinee.setStudent(student);
					if (map.get(student.getStudentId()) != null) {
						examinee.setRegNo(map.get(student.getStudentId()));
					}
					examinees.add(examinee);
				}
			}
			examineeDao.save(examinees);
		}
	}

	@Override
	public synchronized void generateNoByFile(int examId, int gradeId, String prefixNo, int bitNo, MultipartFile file) {
		// TODO Auto-generated method stub
		Workbook wb = ReadExcelUtils.ReadExcelByFile(file);
		try {
			// key 行序 value map(key 列序 value 值)
			Map<Integer, Map<Integer, Object>> map = ReadExcelUtils.readExcelContent(wb);
			Map<String, String> sortMap = sortByMap(map, prefixNo, bitNo);
			int count = sortMap.size();
			// map按照总成绩排序
			if (isGenerateShow(examId, gradeId)) {// 先判断未产生考生号，避免重复生成
				List<Examinee> examinees = new ArrayList<Examinee>();
				Exam exam = examService.get(examId);
				SchoolYear year = schoolYearService.getCurrent();
				List<SchoolClass> schoolClasses = schoolClassService.findByYearAndGradeOrderBySeq(year.getYearId(), gradeId);
				for (SchoolClass schoolClass : schoolClasses) {
					List<Student> students = studentService.findBySchoolClassAndStatus(schoolClass.getClassId(), StudentService.STUDENT_STATUS_ZD);
					for (Student student : students) {
						Examinee examinee = new Examinee();
						examinee.setExam(exam);
						examinee.setStudent(student);
						if (sortMap.get(student.getStudentNo()) != null) {
							examinee.setRegNo(sortMap.get(student.getStudentNo()));
						}else {
							examinee.setRegNo(prefixNo + String.format("%0" + bitNo + "d", count+1));
							count ++;
						}
						examinees.add(examinee);
					}
				}
				examineeDao.save(examinees);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public Page<ExamineeVo> findPageByClass(int currentPage, int pageSize, int examId, int classId) {
		// TODO Auto-generated method stub
		List<ExamineeVo> content = examineeMapper.findPageByClass(currentPage * pageSize, pageSize, examId, classId);
		Pageable pageable = new PageRequest(currentPage, pageSize);
		Integer total = examineeMapper.findCountByClass(examId, classId);
		Page<ExamineeVo> result = new PageImpl<ExamineeVo>(content, pageable, total);
		return result;
	}

	@Override
	public Page<ExamineeVo> findPageByClassAndArrange(int currentPage, int pageSize, int examId, int classId,
			int arrangeId) {
		// TODO Auto-generated method stub
		List<ExamineeVo> content = examineeMapper.findPageByClassAndArrange(currentPage * pageSize, pageSize, examId,
				classId, arrangeId);
		Pageable pageable = new PageRequest(currentPage, pageSize);
		Integer total = examineeMapper.findCountByClassAndArrange(examId, classId, arrangeId);
		Page<ExamineeVo> result = new PageImpl<ExamineeVo>(content, pageable, total);
		return result;
	}

	@Override
	public List<Examinee> findByExamIdAndClassId(int examId, int classId) {
		// TODO Auto-generated method stub
		return examineeMapper.findByExamIdAndClassId(examId, classId);
	}

	@Override
	public List<Examinee> findByArrangeId(int arrangeId) {
		// TODO Auto-generated method stub
		return examineeMapper.findByArrangeId(arrangeId);
	}

	// key:studentId value:regNo
	private Map<Integer, String> generateRegNo(int examId, int gradeId, String prefixNo, int bitNo) {
		// 若不为空则隐藏此按钮。考生号由前缀加序号生成，前缀和序号位数可灵活配置。序号是考生在上次考试中的排名
		// 1 在没有考试的情况下使用考生班级以及studentId的方式排列 2 在有考试的情况下: 如何区分上次考试
		Map<Integer, String> result = new HashMap<Integer, String>();
		SchoolYear year = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		List<ExamVo> examVos = examService.findByYearAndTermOrderByExamDate(year, term);
		if (examVos == null || examVos.size() == 0 || examVos.size() == 1) {
			// 按规则生成考生号 先按照同一班级，同一班级的按照studentId进行排序
			List<Student> students = studentService.findByGradeOrderByStudentId(gradeId, year.getYearId());
			for (int i = 1; i <= students.size(); i++) {
				result.put(students.get(i - 1).getStudentId(), prefixNo + String.format("%0" + bitNo + "d", i));
			}
		} else {
			// 0为当前考试的,1为上一次考试
			ExamVo examVo = examVos.get(1);
			// 确定考试名次
			Map<Integer, String> map = studentComprehensiveTrackService.findExamRank(examVo.getExamId());
			if (map == null || map.size() == 0) {
				// 按规则生成考生号 先按照同一班级，同一班级的按照studentId进行排序
				List<Student> students = studentService.findByGradeOrderByStudentId(gradeId, year.getYearId());
				for (int i = 1; i <= students.size(); i++) {
					result.put(students.get(i - 1).getStudentId(), prefixNo + String.format("%0" + bitNo + "d", i));
				}
			} else {
				for (Integer studentId : map.keySet()) {
					result.put(studentId,
							prefixNo + String.format("%0" + bitNo + "d", Integer.valueOf(map.get(studentId))));
				}
			}

		}
		return result;
	}

	@Override
	public boolean isGenerateShow(int examId, int gradeId) {
		// TODO Auto-generated method stub
		SchoolYear year = schoolYearService.getCurrent();
		List<SchoolClass> schoolClasses = schoolClassService.findByYearAndGradeOrderBySeq(year.getYearId(), gradeId);
		for (SchoolClass schoolClass : schoolClasses) {
			List<Examinee> examinees = examineeMapper.findByExamIdAndClassId(examId, schoolClass.getClassId());
			if (examinees != null && examinees.size() > 0) {
				return false;
			}
		}
		return true;
	}

	@Override
	public List<Examinee> findByExamIdAndClassIds(int examId, int[] classIds) {
		// TODO Auto-generated method stub
		return examineeMapper.findByExamIdAndClassIds(examId, classIds);
	}

	@Override
	public List<Examinee> findByExamAndTotalScoreIsNull(int examId) {
		// TODO Auto-generated method stub
		Exam exam = examService.get(examId);
		return examineeDao.findByExamAndTotalScoreIsNull(exam);
	}

	@Override
	public List<Examinee> findByExamId(int examId) {
		// TODO Auto-generated method stub
		Exam exam = examService.get(examId);
		return examineeDao.findByExam(exam);
	}

	@Override
	public Examinee findByExamAndStudent(int examId, int studentId) {
		// TODO Auto-generated method stub
		Exam exam = new Exam();
		exam.setExamId(examId);
		Student student = new Student();
		student.setStudentId(studentId);
		List<Examinee> examinees = examineeDao.findByExamAndStudent(exam, student);
		if (examinees != null) {
			return examinees.get(0);
		}
		return null;
	}

	@Override
	public List<ExamineeVo> findByClass(int examId, int classId) {
		return examineeMapper.findByClass(examId, classId);
	}

	@Override
	public List<ExamineeVo> findVoByArrangeId(int arrangeId, int yearId) {
		return examineeMapper.findVoByArrangeId(arrangeId, yearId);
	}

	@Override
	public List<ExamineeVo> findByExamIdAndSubjects(int examId, String[] subjects) {
		return examineeMapper.findByExamIdAndSubjects(examId, subjects);
	}

	private Map<String, String> sortByMap(Map<Integer, Map<Integer,Object>> map,String prefixNo, int bitNo) throws NumberFormatException, ParseException{
		Map<String, String> result = new LinkedHashMap<String, String>();
		Map<String, Float> unsortMap = new HashMap<String, Float>();
		DecimalFormat df = new DecimalFormat("0");
		for (int i = 1; i <= map.size(); i++) {  
            if (map.get(i).get(2) == null || map.get(i).get(2) =="") {
            	//double-->int
            	String key ="";
            	try {
            		key =  df.parse(map.get(i).get(0).toString()).intValue()+"";
				} catch (Exception e) {
					key = map.get(i).get(0).toString();
				}           	           	
            	unsortMap.put(key , 0f);           	           	
			}           
            else {
            	String key ="";
            	try {
            		key =  df.parse(map.get(i).get(0).toString()).intValue()+"";
				} catch (Exception e) {
					key = map.get(i).get(0).toString();
				}            	
				unsortMap.put(key, Float.valueOf((map.get(i).get(2).toString())));  	
			}
        } 			
		//map进行排序
		// 将map.entrySet()转换成list
		List<Map.Entry<String, Float>> list = new ArrayList<Map.Entry<String, Float>>(unsortMap.entrySet());
				// 通过比较器来实现排序
				Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
				  @Override
				  public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
				   // 降序排序 分数值越高排名值越小
				   return o2.getValue().compareTo(o1.getValue());
				         }
				     });		        		        					        	
			
        for (int j = 0; j < list.size(); j++) {
        	result.put(list.get(j).getKey(), prefixNo + String.format("%0" + bitNo + "d", j+1));       	
        }		
	return result;
}
	@Override
	public List<ExamineeVo> findByExam(int examId) {
		return examineeMapper.findByExam(examId);
	}
}