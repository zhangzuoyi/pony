package com.zzy.pony.ss.service;

import java.util.List;

public interface StudentSubjectSelectService {
	/**
	 * 学生当前选择的科目列表
	 * @param loginName
	 * @return
	 */
	List<String> findCurrentSelect(String loginName);
	void save(Integer configId,List<String> subjects,String loginName);
}
