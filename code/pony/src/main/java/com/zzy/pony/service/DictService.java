package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.CommonDict;

public interface DictService {
	List<CommonDict> findSexes();
	List<CommonDict> findCredentials();
	List<CommonDict> findStudentStatus();
	List<CommonDict> findEducationDegrees();
}
