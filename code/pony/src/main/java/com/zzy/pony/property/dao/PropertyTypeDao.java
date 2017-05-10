package com.zzy.pony.property.dao;





import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;




import com.zzy.pony.property.model.PropertyType;


public interface PropertyTypeDao extends JpaRepository<PropertyType,Integer>{
	List<PropertyType> findByCategory(Integer category);

}
