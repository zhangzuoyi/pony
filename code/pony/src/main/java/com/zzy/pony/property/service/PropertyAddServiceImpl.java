package com.zzy.pony.property.service;



import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;











import com.zzy.pony.property.dao.PropertyDao;
import com.zzy.pony.property.model.Property;


@Service
@Transactional
public class PropertyAddServiceImpl implements PropertyAddService {

	@Autowired
	private PropertyDao propertyDao;
	
	@Override
	public void add(Property property) {
		// TODO Auto-generated method stub
		propertyDao.save(property);
	}

	@Override
	public int maxCode(String date) {
		// TODO Auto-generated method stub
		List<Property> list = propertyDao.findByPropCodeStartingWith(date);
		int max  = Integer.valueOf(list.get(0).getPropCode()) ;
		for (Property property : list) {
			int code = Integer.valueOf(property.getPropCode()) ; 
			if(code > max){
				max = code;
			}		
		}
		int result =  Integer.valueOf(String.valueOf(max).substring(6));		
		return result;
	}
	

	
	
	

	
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
}
