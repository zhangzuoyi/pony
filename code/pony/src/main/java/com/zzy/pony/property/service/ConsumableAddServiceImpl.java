package com.zzy.pony.property.service;




import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;














import com.zzy.pony.property.dao.ConsumableDao;
import com.zzy.pony.property.model.Consumable;


@Service
@Transactional
public class ConsumableAddServiceImpl implements ConsumableAddService {

	@Autowired
	private ConsumableDao consumableDao;
	
	
	@Override
	public void add(Consumable consumable) {
		// TODO Auto-generated method stub
		consumableDao.save(consumable);
	}

	

	
	
	

	
	

	
	
	

	
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
}
