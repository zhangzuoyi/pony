package com.zzy.pony.mapper;

import java.util.List;

import com.zzy.pony.vo.TeacherSubjectVo;
import com.zzy.pony.vo.ConditionVo;

public interface TeacherSubjectMapper {
	List<TeacherSubjectVo> findByCondition(ConditionVo cv);
	List<TeacherSubjectVo> findCurrentByGroup(ConditionVo cv);
	List<TeacherSubjectVo> findArrangeSeq(ConditionVo cv);
	List<TeacherSubjectVo> findByGrade(int yearId,int termId,int gradeId);


}
