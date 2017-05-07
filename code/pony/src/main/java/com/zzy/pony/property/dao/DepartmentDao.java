package com.zzy.pony.property.dao;





import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;





import com.zzy.pony.property.model.Department;


public interface DepartmentDao extends JpaRepository<Department,Integer>{
	List<Department> findByName(String name);

}
