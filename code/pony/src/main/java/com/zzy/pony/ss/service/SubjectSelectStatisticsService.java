package com.zzy.pony.ss.service;

import java.util.List;
import java.util.Map;

import com.zzy.pony.ss.vo.StudentSubjectSelectVo;
import com.zzy.pony.ss.vo.StudentSubjectStatisticsVo;

public interface SubjectSelectStatisticsService {
	
	/**
	 * @param configId
	 * @return 已选总人数
	 */
	int findTotalSelectByConfig(int configId);
	
	/**
	 * @param configId
	 * @return 分组
	 */
	List<StudentSubjectStatisticsVo> group(int configId);
	
	/**
	 * @param list
	 * @return 每个学生的选课结果
	 */
	Map<Integer, String> studentSelect(List<StudentSubjectSelectVo> list);
	
}
