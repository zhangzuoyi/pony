package com.zzy.pony.property.service;

import java.util.List;

import com.zzy.pony.property.model.PropertyType;







public interface PropertyTypeService {
	List<PropertyType> list();
	void add(PropertyType propertyType);
	void update(PropertyType propertyType);
	void delete(int typeId);
	Boolean isExist(PropertyType propertyType);
}
