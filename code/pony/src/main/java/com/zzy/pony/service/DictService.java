package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.CommonDict;

public interface DictService {
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
}
