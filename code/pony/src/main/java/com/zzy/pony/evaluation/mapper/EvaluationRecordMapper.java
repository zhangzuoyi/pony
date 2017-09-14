package com.zzy.pony.evaluation.mapper;

import java.util.List;

import com.zzy.pony.evaluation.vo.EvaluationRecordVo;

public interface EvaluationRecordMapper {
	List<EvaluationRecordVo> findBySubjectId(Long subjectId);
}
