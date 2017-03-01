package com.zzy.pony.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.StudentDao;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.Student;
@Service
@Transactional
public class StudentServiceImpl implements StudentService {
	@Autowired
	private StudentDao dao;

	@Override
	public void add(Student sy) {
		dao.save(sy);

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
	public void upload(List<Student> students, String loginUser) {
		Date now=new Date();
		for(Student stu: students){
			Student old=dao.findByStudentNo(stu.getStudentNo());
			if(old == null){
				stu.setCreateTime(now);
				stu.setCreateUser(loginUser);
				stu.setUpdateTime(now);
				stu.setUpdateUser(loginUser);
				
				dao.save(stu);
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

}
