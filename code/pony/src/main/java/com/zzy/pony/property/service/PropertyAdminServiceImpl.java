package com.zzy.pony.property.service;


import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;









import com.zzy.pony.config.Constants;
import com.zzy.pony.property.dao.PropertyDao;
import com.zzy.pony.property.model.Property;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.UserService;


@Service
@Transactional
public class PropertyAdminServiceImpl implements PropertyAdminService {

	@Autowired
	private PropertyDao propertyDao;
	@Autowired
	private UserService userService;
	
	@Override
	public List<Property> list() {
		// TODO Auto-generated method stub
		return propertyDao.findAll();
	}

	

	@Override
	public void receive(Property property, int teacherId) {
		// TODO Auto-generated method stub
		property.setUpdateTime(new Date());
		property.setUpdateUser(ShiroUtil.getLoginUser().getLoginName());
		property.setStatus(Constants.PROPERTY_STATUS_OCCUPY);
		property.setUser(userService.findByTeacherId(teacherId));
		propertyDao.save(property);
	}

	@Override
	public void back(Property property) {
		// TODO Auto-generated method stub
		property.setUpdateTime(new Date());
		property.setUpdateUser(ShiroUtil.getLoginUser().getLoginName());
		property.setStatus(Constants.PROPERTY_STATUS_FREE);
		property.setUser(null);
		propertyDao.save(property);
	}

	@Override
	public void cancle(Property property) {
		// TODO Auto-generated method stub
		property.setUpdateTime(new Date());
		property.setUpdateUser(ShiroUtil.getLoginUser().getLoginName());
		property.setStatus(Constants.PROPERTY_STATUS_DESTROY);
		property.setUser(null);
		propertyDao.save(property);
	}

	@Override
	public Property get(int id) {
		// TODO Auto-generated method stub
		return propertyDao.findOne(id);
	}
	
	
	
	
	
	
	
	

	
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
}
