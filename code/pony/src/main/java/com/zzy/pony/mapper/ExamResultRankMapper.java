package com.zzy.pony.mapper;

import java.util.List;

import com.zzy.pony.vo.ExamResultRankVo;
import com.zzy.pony.vo.ConditionVo;

public interface ExamResultRankMapper {
	List<ExamResultRankVo> findByCondition(ConditionVo cv);
	List<Integer> findExamsByStudentId(int studentId);
}
