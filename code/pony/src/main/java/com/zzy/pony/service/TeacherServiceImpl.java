package com.zzy.pony.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.TeacherDao;
import com.zzy.pony.model.Teacher;
@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {
	@Autowired
	private TeacherDao dao;
	@Autowired
	private UserService userService;

	@Override
	public void add(Teacher sy) {
		dao.save(sy);
		userService.addFromTeacher(sy);
	}

	@Override
	public List<Teacher> findAll() {
		return dao.findAll();
	}

	@Override
	public Teacher get(int id) {
		return dao.findOne(id);
	}

	@Override
	public void update(Teacher sy) {
		Teacher old=dao.findOne(sy.getTeacherId());
		old.setBirthday(sy.getBirthday());
		old.setEmail(sy.getEmail());
		old.setEntranceDate(sy.getEntranceDate());
		old.setGraduateDate(sy.getGraduateDate());
		old.setGraduateSchool(sy.getGraduateSchool());
		old.setHomeAddr(sy.getHomeAddr());
		old.setHomeZipcode(sy.getHomeZipcode());
		old.setIdNo(sy.getIdNo());
		old.setIdType(sy.getIdType());
		old.setMajor(sy.getMajor());
		old.setName(sy.getName());
		old.setNation(sy.getNation());
		old.setNativeAddr(sy.getNativeAddr());
		old.setNativePlace(sy.getNativePlace());
		old.setPhone(sy.getPhone());
		old.setSex(sy.getSex());
		old.setStartWorkDate(sy.getStartWorkDate());
		old.setSubject(sy.getSubject());
		old.setTeacherNo(sy.getTeacherNo());
		old.setTitle(sy.getTitle());
		old.setUpdateTime(sy.getUpdateTime());
		old.setUpdateUser(sy.getUpdateUser());
		old.setWorkLength(sy.getWorkLength());
		
		dao.save(old);
	}

	@Override
	public void delete(int id) {
		dao.delete(id);
		
	}

	@Override
	public Teacher findByTeacherNo(String teacherNo) {
		List<Teacher> list=dao.findByTeacherNo(teacherNo);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void upload(List<Teacher> teachers, String loginUser) {
		Date now=new Date();
		for(Teacher tea: teachers){
			List<Teacher> list=dao.findByTeacherNo(tea.getTeacherNo());
			Teacher old=null;
			if(list.size()>0){
				old=list.get(0);
			}
			if(old == null){
				tea.setCreateTime(now);
				tea.setCreateUser(loginUser);
				tea.setUpdateTime(now);
				tea.setUpdateUser(loginUser);
				
				add(tea);
			}else{
				old.setBirthday(tea.getBirthday());
				old.setEmail(tea.getEmail());
//				old.setEntranceDate(stu.getEntranceDate());
				old.setHomeAddr(tea.getHomeAddr());
				old.setHomeZipcode(tea.getHomeZipcode());
				old.setIdNo(tea.getIdNo());
				old.setIdType(tea.getIdType());
				old.setName(tea.getName());
				old.setNation(tea.getNation());
				old.setNativeAddr(tea.getNativeAddr());
				old.setNativePlace(tea.getNativePlace());
				old.setPhone(tea.getPhone());
				old.setSex(tea.getSex());
				old.setUpdateTime(now);
				old.setUpdateUser(loginUser);
				old.setDegree(tea.getDegree());
				old.setGraduateDate(tea.getGraduateDate());
				old.setGraduateSchool(tea.getGraduateSchool());
				old.setMajor(tea.getMajor());
				old.setSubject(tea.getSubject());
				old.setTitle(tea.getTitle());
				
				dao.save(old);
			}
		}
	}
}
