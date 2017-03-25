package com.zzy.pony.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.GradeDao;
import com.zzy.pony.model.Grade;
@Service
@Transactional
public class GradeServiceImpl implements GradeService {
	@Autowired
	private GradeDao dao;

	@Override
	public void add(Grade sy) {
		dao.save(sy);

	}

	@Override
	public List<Grade> findAll() {
		return dao.findAll();
	}

	@Override
	public Grade get(int id) {
		return dao.findOne(id);
	}

	@Override
	public void update(Grade sy) {
		Grade old=dao.findOne(sy.getGradeId());
		old.setName(sy.getName());
		old.setSeq(sy.getSeq());
		
		dao.save(old);
	}

	@Override
	public void delete(int id) {
		dao.delete(id);
		
	}

}
