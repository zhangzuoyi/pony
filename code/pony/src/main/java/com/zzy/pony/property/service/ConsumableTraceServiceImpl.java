package com.zzy.pony.property.service;




import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
















import com.zzy.pony.property.dao.ConsumableTraceDao;
import com.zzy.pony.property.model.ConsumableTrace;


@Service
@Transactional
public class ConsumableTraceServiceImpl implements ConsumableTraceService {

	@Autowired
	private ConsumableTraceDao consumableTraceDao;
	
	
	@Override
	public void add(ConsumableTrace consumableTrace) {
		// TODO Auto-generated method stub
		consumableTraceDao.save(consumableTrace);
	}

	

	
	
	

	
	

	
	
	

	
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
}
