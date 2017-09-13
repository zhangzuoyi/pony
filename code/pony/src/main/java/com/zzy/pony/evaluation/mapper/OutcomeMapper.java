package com.zzy.pony.evaluation.mapper;

import java.util.List;

import com.zzy.pony.evaluation.vo.OutcomeVo;

public interface OutcomeMapper {
	List<OutcomeVo> findByTeacher(Integer teacherId);
	List<OutcomeVo> findByTeacherAndStatus(Integer teacherId, int status);
	List<OutcomeVo> findAll();
}
