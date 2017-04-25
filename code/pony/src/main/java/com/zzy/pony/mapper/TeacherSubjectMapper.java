package com.zzy.pony.mapper;

import java.util.List;

import com.zzy.pony.vo.TeacherSubjectVo;
import com.zzy.pony.vo.conditionVo;

public interface TeacherSubjectMapper {
	List<TeacherSubjectVo> findByCondition(conditionVo cv);
}
