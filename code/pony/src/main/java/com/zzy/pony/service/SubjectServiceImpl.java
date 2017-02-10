package com.zzy.pony.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.SubjectDao;
import com.zzy.pony.model.Subject;
@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {
	@Autowired
	private SubjectDao dao;

	@Override
	public void add(Subject sy) {
		dao.save(sy);

	}

	@Override
	public List<Subject> findAll() {
		return dao.findAll();
	}

	@Override
	public Subject get(int id) {
		return dao.findOne(id);
	}

	@Override
	public void update(Subject sy) {
		Subject old=dao.findOne(sy.getSubjectId());
		old.setName(sy.getName());
		old.setType(sy.getType());
		
		dao.save(old);
	}

	@Override
	public void delete(int id) {
		dao.delete(id);
		
	}

	@Override
	public List<Subject> findClassSubject() {
		List<Integer> types=new ArrayList<Integer>();
		types.add(0);
		types.add(1);
		
		return dao.findByTypeIn(types);
	}

}
