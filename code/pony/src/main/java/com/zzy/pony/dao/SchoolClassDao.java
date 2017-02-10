package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.SchoolClass;


public interface SchoolClassDao extends JpaRepository<SchoolClass,Integer>{
	List<SchoolClass> findByClassIdIn(List<Integer> ids);

}
