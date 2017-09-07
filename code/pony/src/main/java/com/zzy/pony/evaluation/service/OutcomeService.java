package com.zzy.pony.evaluation.service;

import java.util.List;

import com.zzy.pony.evaluation.model.Outcome;
import com.zzy.pony.evaluation.vo.OutcomeVo;

public interface OutcomeService {
	List<OutcomeVo> findByTeacher(Integer teacherId);
	void add(Outcome outcome);
}
