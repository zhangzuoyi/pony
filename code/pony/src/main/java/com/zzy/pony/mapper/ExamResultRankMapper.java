package com.zzy.pony.mapper;

import java.util.List;

import com.zzy.pony.vo.ExamResultRankVo;
import com.zzy.pony.vo.conditionVo;

public interface ExamResultRankMapper {
	List<ExamResultRankVo> findByCondition(conditionVo cv);
	List<Integer> findExamsByStudentId(int studentId);
}
