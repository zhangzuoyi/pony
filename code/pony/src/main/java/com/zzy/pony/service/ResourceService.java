package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.Resource;



public interface ResourceService {
	List<Resource> findByResLevel(int resLevel);
	List<Resource> findByPresId(int presId);
	void add(Resource resource);
	void delete(int resId);
	void update(Resource resource);

}
