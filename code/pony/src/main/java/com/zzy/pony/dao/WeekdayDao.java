package com.zzy.pony.dao;



import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.Weekday;


public interface WeekdayDao extends JpaRepository<Weekday,Integer>{
	

}
