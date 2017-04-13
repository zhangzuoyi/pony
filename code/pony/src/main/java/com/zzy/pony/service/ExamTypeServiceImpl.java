package com.zzy.pony.service;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.ExamTypeDao;
import com.zzy.pony.model.ExamType;


@Service
@Transactional
public class ExamTypeServiceImpl implements ExamTypeService {

	@Autowired
	private ExamTypeDao examTypeDao;
	
	@Override
	public List<ExamType> findAll() {
		// TODO Auto-generated method stub
		return examTypeDao.findAll();
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
}
