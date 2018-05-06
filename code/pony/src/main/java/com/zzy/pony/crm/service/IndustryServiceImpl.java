package com.zzy.pony.crm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.crm.dao.IndustryDao;
import com.zzy.pony.crm.model.Industry;
@Service
public class IndustryServiceImpl implements IndustryService {
	@Autowired
	private IndustryDao dao;

	@Override
	public List<Industry> findAll() {
		return dao.findAll();
	}

}
