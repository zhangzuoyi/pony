package com.zzy.pony.mapper;

import java.util.List;

import com.zzy.pony.vo.ExamResultVo;

public interface ExamResultMapper {
	List<ExamResultVo> find(Integer examId, Integer classId);
}
