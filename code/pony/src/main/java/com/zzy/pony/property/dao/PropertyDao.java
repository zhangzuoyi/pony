package com.zzy.pony.property.dao;






import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;







import com.zzy.pony.property.model.Property;


public interface PropertyDao extends JpaRepository<Property,Integer>{

	List<Property> findByPropCodeStartingWith(String date);
	
}
