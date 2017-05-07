package com.zzy.pony.property.service;

import java.util.List;

import com.zzy.pony.property.model.Department;







public interface DepartmentService {
	List<Department> list();
	void add(Department department);
	void update(Department department);
	void delete(int typeId);
	Boolean isExist(Department department);
}
