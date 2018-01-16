package com.zzy.pony.ss.mapper;

import com.zzy.pony.ss.vo.StudentSubjectResultVo;
import com.zzy.pony.ss.vo.StudentSubjectSelectVo;
import com.zzy.pony.vo.ConditionVo;

import java.util.List;

public interface SubjectSelectResultMapper {
	
	List<StudentSubjectResultVo> findBySelected(ConditionVo cv);


	

	
}
