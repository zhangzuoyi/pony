package com.zzy.pony.property.service;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;







import com.zzy.pony.property.dao.DepartmentDao;
import com.zzy.pony.property.dao.PropertyTypeDao;
import com.zzy.pony.property.model.Department;
import com.zzy.pony.property.model.PropertyType;


@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentDao departmentDao;
	
	@Override
	public List<Department> list() {
		// TODO Auto-generated method stub
		return departmentDao.findAll();
	}

	@Override
	public void add(Department department) {
		// TODO Auto-generated method stub
		departmentDao.save(department);
	}

	@Override
	public void update(Department department) {
		// TODO Auto-generated method stub
		Department old = departmentDao.findOne(department.getDeptId());
		old.setName(department.getName());
		departmentDao.save(old);
	}

	@Override
	public void delete(int deptId) {
		// TODO Auto-generated method stub	
		departmentDao.delete(deptId);
	}

	@Override
	public Boolean isExist(Department department) {
		// TODO Auto-generated method stub
		List<Department> dt = departmentDao.findByName(department.getName());
		if (dt == null || dt.size() == 0) {
			return false;
		}
		return true;
	}
	
	

	
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
}
