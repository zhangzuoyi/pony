package com.zzy.pony.ss.mapper;

import com.zzy.pony.ss.vo.StudentSubjectResultVo;
import com.zzy.pony.ss.vo.StudentSubjectSingleVo;
import com.zzy.pony.vo.ConditionVo;

import java.util.List;

public interface SubjectSelectSingleMapper {
	
	List<StudentSubjectSingleVo> list(int configId);


	

	
}
