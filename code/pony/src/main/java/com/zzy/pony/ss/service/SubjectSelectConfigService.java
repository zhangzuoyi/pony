package com.zzy.pony.ss.service;

import java.util.List;

import com.zzy.pony.ss.model.SubjectSelectConfig;

public interface SubjectSelectConfigService {
	String IS_CURRENT_Y="1";
	String IS_CURRENT_N="0";
	
	void add(SubjectSelectConfig config);
	void update(SubjectSelectConfig config, String loginName);
	void delete(Integer id);
	List<SubjectSelectConfig> findAll();
	SubjectSelectConfig getCurrent();
	SubjectSelectConfig get(int configId);
}
