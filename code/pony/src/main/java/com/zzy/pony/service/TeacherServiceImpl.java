package com.zzy.pony.service;

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

	@Override
	public void add(Teacher sy) {
		dao.save(sy);

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

}
