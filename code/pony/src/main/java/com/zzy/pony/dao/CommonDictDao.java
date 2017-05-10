package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.CommonDict;


public interface CommonDictDao extends JpaRepository<CommonDict,Integer>{
	List<CommonDict> findByDictType(String type);
	List<CommonDict> findByDictTypeAndCode(String type,String code);
	


}
