package com.zzy.pony.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.PrizePunishDao;
import com.zzy.pony.model.Student;
import com.zzy.pony.model.PrizePunish;

@Service
public class PrizePunishServiceImpl implements PrizePunishService {
	@Autowired
	private PrizePunishDao dao;

	@Override
	public List<PrizePunish> findByStudent(Integer studentId) {
		Student student = new Student();
		student.setStudentId(studentId);
		return dao.findByStudent(student);
	}

	@Override
	public void add(PrizePunish pp, String loginName) {
		pp.setCreateTime(new Date());
		pp.setCreateUser(loginName);
		pp.setUpdateTime(new Date());
		pp.setUpdateUser(loginName);
		dao.save(pp);

	}

	@Override
	public void delete(int id) {
		dao.delete(id);

	}

	@Override
	public void update(PrizePunish pp, String loginName) {
		PrizePunish old = dao.findOne(pp.getId());
		old.setInfo(pp.getInfo());
		old.setOccurDate(pp.getOccurDate());
		old.setReason(pp.getReason());
		old.setType(pp.getType());
		old.setUpdateTime(new Date());
		old.setUpdateUser(loginName);

		dao.save(old);
	}

}