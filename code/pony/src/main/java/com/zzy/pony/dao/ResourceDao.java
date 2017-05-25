package com.zzy.pony.dao;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.Resource;


public interface ResourceDao extends JpaRepository<Resource,Integer>{
	List<Resource> findByResLevel(int resLevel);
	List<Resource> findByPresId(int presId);

}
