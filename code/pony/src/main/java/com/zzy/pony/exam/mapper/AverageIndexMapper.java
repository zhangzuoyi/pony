package com.zzy.pony.exam.mapper;

import java.util.List;

import com.zzy.pony.exam.vo.AverageIndexVo;

public interface AverageIndexMapper {
	List<AverageIndexVo> findByExamAndGradeAndSubject(int examId,int gradeId,int subjectId);
}
