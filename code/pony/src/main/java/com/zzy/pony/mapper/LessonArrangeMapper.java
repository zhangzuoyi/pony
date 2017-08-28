package com.zzy.pony.mapper;

import java.util.List;

import com.zzy.pony.vo.ArrangeVo;
import com.zzy.pony.vo.ConditionVo;

public interface LessonArrangeMapper {
	List<ArrangeVo> findByCondition(ConditionVo cv);
	List<ArrangeVo> findPreLessonArrange(ConditionVo cv);
	List<ArrangeVo> findPreTeacherAlready(ConditionVo cv);

}
