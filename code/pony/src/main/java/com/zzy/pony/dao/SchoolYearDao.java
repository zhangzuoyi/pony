package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.SchoolYear;


public interface SchoolYearDao extends JpaRepository<SchoolYear,Integer>{
	List<SchoolYear> findByIsCurrent(String isCurrent);

}
