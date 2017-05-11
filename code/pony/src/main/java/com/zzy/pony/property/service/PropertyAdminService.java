package com.zzy.pony.property.service;

import java.util.List;

import com.zzy.pony.property.model.Property;
import com.zzy.pony.property.model.PropertyType;







public interface PropertyAdminService {
	List<Property> list();
	void receive(Property property);
	void back(Property property);
	void cancle(Property property);
	Property get(int id);
}
