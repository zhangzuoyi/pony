package com.zzy.pony.property.service;


import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;











import com.zzy.pony.config.Constants;
import com.zzy.pony.property.dao.ConsumableDao;
import com.zzy.pony.property.dao.PropertyDao;
import com.zzy.pony.property.model.Consumable;
import com.zzy.pony.property.model.Property;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.UserService;


@Service
@Transactional
public class ConsumableAdminServiceImpl implements ConsumableAdminService {

	@Autowired
	private ConsumableDao consumableDao;
	
	
	@Override
	public List<Consumable> list() {
		// TODO Auto-generated method stub
		return consumableDao.findAll();
	}
	@Override
	public Consumable get(int id) {
		// TODO Auto-generated method stub
		return consumableDao.findOne(id);
	}
	@Override
	public void add(Consumable consumable) {
		// TODO Auto-generated method stub
		consumableDao.save(consumable);
	}
	
	
	
	
	
	
	
	

	
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
}
