package com.zzy.pony.mapper;

import java.util.List;

import com.zzy.pony.vo.ArrangeVo;
import com.zzy.pony.vo.ConditionVo;

public interface LessonArrangeMapper {
	List<ArrangeVo> findByCondition(ConditionVo cv);
	List<ArrangeVo> findPreLessonArrange(ConditionVo cv);
	List<ArrangeVo> findPreTeacherAlready(ConditionVo cv);
	//查询教师是否安排了课程
	List<Integer> findByWeekAndPeriodAndTeacherId(int yearId,int termId, int week,int period,int teacherId);

}
