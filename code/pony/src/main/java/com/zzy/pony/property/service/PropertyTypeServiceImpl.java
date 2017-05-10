package com.zzy.pony.property.service;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;





import com.zzy.pony.property.dao.PropertyTypeDao;
import com.zzy.pony.property.model.PropertyType;


@Service
@Transactional
public class PropertyTypeServiceImpl implements PropertyTypeService {

	@Autowired
	private PropertyTypeDao propertyTypeDao;
	
	@Override
	public List<PropertyType> list() {
		// TODO Auto-generated method stub
		return propertyTypeDao.findAll();
	}

	@Override
	public void add(PropertyType propertyType) {
		// TODO Auto-generated method stub
		propertyTypeDao.save(propertyType);
	}

	@Override
	public void update(PropertyType propertyType) {
		// TODO Auto-generated method stub
		PropertyType old = propertyTypeDao.findOne(propertyType.getTypeId());
		old.setCategory(propertyType.getCategory());
		old.setName(propertyType.getName());
		propertyTypeDao.save(old);
	}

	@Override
	public void delete(int typeId) {
		// TODO Auto-generated method stub	
		propertyTypeDao.delete(typeId);
	}

	@Override
	public Boolean isExist(PropertyType propertyType) {
		// TODO Auto-generated method stub
		List<PropertyType> propertyTypes = propertyTypeDao.findByCategory(propertyType.getCategory());
		if (propertyTypes == null || propertyTypes.isEmpty()) {
			return false;
		}
		return true;
	}
	
	

	
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
}
