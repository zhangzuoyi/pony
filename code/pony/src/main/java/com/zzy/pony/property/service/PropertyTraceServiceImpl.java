package com.zzy.pony.property.service;




import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


















import com.zzy.pony.property.dao.PropertyTraceDao;
import com.zzy.pony.property.model.PropertyTrace;


@Service
@Transactional
public class PropertyTraceServiceImpl implements PropertyTraceService {

	@Autowired
	private PropertyTraceDao propertyTraceDao;
	
	
	@Override
	public void add(PropertyTrace propertyTrace) {
		// TODO Auto-generated method stub
		propertyTraceDao.save(propertyTrace);
	}

	

	
	
	

	
	

	
	
	

	
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
}
