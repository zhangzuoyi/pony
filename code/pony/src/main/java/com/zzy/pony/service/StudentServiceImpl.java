package com.zzy.pony.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.StudentDao;
import com.zzy.pony.dao.StudentStatusChangeDao;
import com.zzy.pony.mapper.StudentMapper;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Student;
import com.zzy.pony.model.StudentStatusChange;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.StudentStatusChangeVo;
@Service
@Transactional
public class StudentServiceImpl implements StudentService {
	@Autowired
	private StudentDao dao;
	@Autowired
	private StudentStatusChangeDao changeDao;
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private TermService termService;
	@Autowired
	private StudentMapper studentMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private StudentMapper mapper;
	@Autowired
	private SchoolClassService scService;

	@Override
	public void add(Student sy) {
		dao.save(sy);
		mapper.insertStudentClassRelation(sy.getStudentId(), sy.getSchoolClass().getClassId());
		addStatusChange(sy);
		userService.addFromStudent(sy);
	}
	private void addStatusChange(Student stu){
		StudentStatusChange change=new StudentStatusChange();
		if(StudentService.STUDENT_TYPE_JR.equals(stu.getEntranceType())){
			change.setChangeType("借入");
		}else if(StudentService.STUDENT_TYPE_TZ.equals(stu.getEntranceType())){
			change.setChangeType("入学");
		}else if(StudentService.STUDENT_TYPE_ZR.equals(stu.getEntranceType())){
			change.setChangeType("转入");
		}
		change.setCreateTime(new Date());
		change.setCreateUser(stu.getCreateUser());
		change.setOccurDate(stu.getEntranceDate());
		change.setStudentId(stu.getStudentId());
		change.setSchoolYear(yearService.getCurrent());
		change.setTerm(termService.getCurrent());
		changeDao.save(change);
	}

	@Override
	public List<Student> findAll() {
		return dao.findAll();
	}

	@Override
	public Student get(int id) {
		return dao.findOne(id);
	}

	@Override
	public void update(Student sy) {
		Student old=dao.findOne(sy.getStudentId());
		old.setBirthday(sy.getBirthday());
		old.setEmail(sy.getEmail());
		old.setEntranceDate(sy.getEntranceDate());
		old.setEntranceType(sy.getEntranceType());
		old.setGraduateDate(sy.getGraduateDate());
		old.setGraduateType(sy.getGraduateType());
		old.setHomeAddr(sy.getHomeAddr());
		old.setHomeZipcode(sy.getHomeZipcode());
		old.setIdNo(sy.getIdNo());
		old.setIdType(sy.getIdType());
		old.setName(sy.getName());
		old.setNation(sy.getNation());
		old.setNativeAddr(sy.getNativeAddr());
		old.setNativePlace(sy.getNativePlace());
		old.setPhone(sy.getPhone());
		old.setSex(sy.getSex());
//		old.setStudentNo(sy.getStudentNo());//学号不能改
		old.setUpdateTime(sy.getUpdateTime());
		old.setUpdateUser(sy.getUpdateUser());
		old.setSchoolClass(sy.getSchoolClass());
		
		dao.save(old);
	}

	@Override
	public void delete(int id) {
		dao.delete(id);
		
	}

	@Override
	public List<Student> findBySchoolClass(Integer classId) {
		SchoolClass sc=new SchoolClass();
		sc.setClassId(classId);
		return dao.findBySchoolClass(sc);
	}
	
	

	@Override
	public List<Student> findBySchoolClassAndStatus(Integer classId, String status) {
		// TODO Auto-generated method stub
		SchoolClass sc=new SchoolClass();
		sc.setClassId(classId);
		return dao.findBySchoolClassAndStatus(sc, status);
	}
	@Override
	public void upload(List<Student> students, String loginUser) {
		Date now=new Date();
		for(Student stu: students){
			Student old=dao.findByStudentNo(stu.getStudentNo());
			if(old == null){
				stu.setCreateTime(now);
				stu.setCreateUser(loginUser);
				stu.setUpdateTime(now);
				stu.setUpdateUser(loginUser);
				
				add(stu);
			}else{
				old.setBirthday(stu.getBirthday());
				old.setEmail(stu.getEmail());
				old.setEntranceDate(stu.getEntranceDate());
				old.setHomeAddr(stu.getHomeAddr());
				old.setHomeZipcode(stu.getHomeZipcode());
				old.setIdNo(stu.getIdNo());
				old.setIdType(stu.getIdType());
				old.setName(stu.getName());
				old.setNation(stu.getNation());
				old.setNativeAddr(stu.getNativeAddr());
				old.setNativePlace(stu.getNativePlace());
				old.setPhone(stu.getPhone());
				old.setSchoolClass(stu.getSchoolClass());
				old.setSex(stu.getSex());
				old.setUpdateTime(now);
				old.setUpdateUser(loginUser);
				
				dao.save(old);
			}
		}
	}

	@Override
	public void changeStatus(StudentStatusChange sc) {
		sc.setSchoolYear(yearService.getCurrent());
		sc.setTerm(termService.getCurrent());
		Student student=dao.findOne(sc.getStudentId());
		student.setStatus(getStatusByChangeType(sc.getChangeType()));
		
		dao.save(student);
		changeDao.save(sc);
	}
	
	
	

	@Override
	public List<Student> findByGradeOrderByStudentId(int gradeId,int yearId) {
		// TODO Auto-generated method stub
		return studentMapper.findByGradeOrderByStudentId(gradeId,yearId);
	}
	
	
	
	
	private String getStatusByChangeType(String changeType){
		if("开除".equals(changeType)){
			return STUDENT_STATUS_KC;
		}else if("辍学".equals(changeType)){
			return STUDENT_STATUS_CX;
		}else if("退学".equals(changeType)){
			return STUDENT_STATUS_TX;
		}else if("肄业".equals(changeType)){
			return STUDENT_STATUS_YY;
		}else if("死亡".equals(changeType)){
			return STUDENT_STATUS_SW;
		}else if("转出".equals(changeType)){
			return STUDENT_STATUS_ZC;
		}else if("借出".equals(changeType)){
			return STUDENT_STATUS_JC;
		}else if("休学".equals(changeType)){
			return STUDENT_STATUS_XX;
		}else if("在读".equals(changeType)){
			return STUDENT_STATUS_ZD;
		}else if("不参评".equals(changeType)){
			return STUDENT_STATUS_BCP;
		}
		
		return STUDENT_STATUS_ZD;
	}
	@Override
	public void setStudentSubjects(String[] subjects, Integer[] studentIds) {
		String subjectsName=StringUtils.join(subjects, ",");
		for(Integer studentId: studentIds){
			Student student=dao.findOne(studentId);
			student.setExamSubjects(subjectsName);
			dao.save(student);
		}
		
	}
	@Override
	public void changeStatus(StudentStatusChangeVo vo) {
		String[] ids=vo.getStudentIds().split(",");
		SchoolYear year=yearService.getCurrent();
		Term term=termService.getCurrent();
		for(String id: ids) {
			StudentStatusChange sc=new StudentStatusChange();
			sc.setSchoolYear(year);
			sc.setTerm(term);
			sc.setChangeType(vo.getChangeType());
			sc.setCreateTime(vo.getCreateTime());
			sc.setCreateUser(vo.getCreateUser());
			sc.setDescription(vo.getDescription());
			sc.setOccurDate(vo.getOccurDate());
			sc.setStudentId(Integer.valueOf(id));
			Student student=dao.findOne(Integer.valueOf(id));
			student.setStatus(getStatusByChangeType(sc.getChangeType()));
			
			dao.save(student);
			changeDao.save(sc);
		}
		
	}
	@Override
	public List<Student> findByGrade(Integer gradeId) {
		List<SchoolClass> classes=scService.findByGrade(gradeId);
		List<Student> students=dao.findBySchoolClasses(classes);
		return students;
	}
}
