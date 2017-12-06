package com.zzy.pony.ss.mapper;

import java.util.List;

import com.zzy.pony.ss.vo.StudentSubjectSelectVo;

public interface SubjectSelectStatisticsMapper {
	
	int findTotalSelectByConfig(int configId);
	
	List<StudentSubjectSelectVo> findAllByConfig(int configId);
	
	
}
