package com.zzy.pony.evaluation.mapper;

import java.util.List;

import com.zzy.pony.evaluation.vo.EvaluationItemDataVo;

public interface EvaluationItemDataMapper {
	List<EvaluationItemDataVo> findByRecord(Long recordId);
}
