package com.zzy.pony.property.service;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;







import com.zzy.pony.property.dao.PropertyDao;
import com.zzy.pony.property.dao.PropertyTypeDao;
import com.zzy.pony.property.model.Property;
import com.zzy.pony.property.model.PropertyType;


@Service
@Transactional
public class PropertyAdminServiceImpl implements PropertyAdminService {

	@Autowired
	private PropertyDao propertyDao;
	
	@Override
	public List<Property> list() {
		// TODO Auto-generated method stub
		return propertyDao.findAll();
	}

	@Override
	public void receive(Property property) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void back(Property property) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancle(Property property) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Property get(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	
	

	
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
}
