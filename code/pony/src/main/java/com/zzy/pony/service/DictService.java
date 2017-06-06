package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.CommonDict;

public interface DictService {
	
	
	List<CommonDict> list();
	void add(CommonDict commonDict);
	void update(CommonDict commonDict);
	void delete(int dictId);
	Boolean isExist(CommonDict commonDict);
	
	List<CommonDict> listByDictType(String dictType);
	
	List<CommonDict> findSexes();
	List<CommonDict> findCredentials();
	List<CommonDict> findStudentStatus();
	List<CommonDict> findEducationDegrees();
	/**
	 * 学生评价等级
	 * @return
	 */
	List<CommonDict> findStudentRemarkLevels();
	/**
	 * 学生类型（入学类型）
	 * @return
	 */
	List<CommonDict> findStudentTypes();
	List<CommonDict> findPropertyStatus();
	List<CommonDict> findSubjectTypes();
	List<CommonDict> findImportances();


	
	
	
	
}
