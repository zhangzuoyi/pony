package com.zzy.pony.ss.dao;







import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.ss.model.SubjectSelectConfig;


public interface SubjectSelectConfigDao extends JpaRepository<SubjectSelectConfig,Integer>{
	List<SubjectSelectConfig> findByIsCurrent(String isCurrent);
	
	
}
