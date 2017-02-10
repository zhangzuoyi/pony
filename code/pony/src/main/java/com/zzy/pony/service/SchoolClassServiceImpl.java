package com.zzy.pony.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.SchoolClassDao;
import com.zzy.pony.model.SchoolClass;
@Service
@Transactional
public class SchoolClassServiceImpl implements SchoolClassService {
	@Autowired
	private SchoolClassDao dao;

	@Override
	public void add(SchoolClass sy) {
		dao.save(sy);

	}

	@Override
	public List<SchoolClass> findAll() {
		return dao.findAll();
	}

	@Override
	public SchoolClass get(int id) {
		return dao.findOne(id);
	}

	@Override
	public void update(SchoolClass sy) {
		SchoolClass old=dao.findOne(sy.getClassId());
		old.setGrade(sy.getGrade());
		old.setName(sy.getName());
		old.setUpdateUser(sy.getUpdateUser());
		old.setUpdateTime(sy.getUpdateTime());
		
		dao.save(old);
	}

	@Override
	public void delete(int id) {
		dao.delete(id);
		
	}

}
