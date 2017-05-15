package com.zzy.pony.property.dao;






import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;








import com.zzy.pony.property.model.Consumable;
import com.zzy.pony.property.model.Property;


public interface ConsumableDao extends JpaRepository<Consumable,Integer>{

	
	
}
