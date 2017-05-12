package com.zzy.pony.property.service;

import java.util.List;

import com.zzy.pony.property.model.Property;







public interface PropertyAdminService {
	List<Property> list();
	void receive(Property property,int teacherId);
	void back(Property property);
	void cancle(Property property);
	Property get(int id);
}
