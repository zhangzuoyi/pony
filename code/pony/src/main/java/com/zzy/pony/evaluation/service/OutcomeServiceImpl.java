package com.zzy.pony.evaluation.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.evaluation.dao.OutcomeDao;
import com.zzy.pony.evaluation.mapper.OutcomeMapper;
import com.zzy.pony.evaluation.model.Outcome;
import com.zzy.pony.evaluation.vo.OutcomeVo;
@Service
@Transactional
public class OutcomeServiceImpl implements OutcomeService {
	@Autowired
	private OutcomeMapper mapper;
	@Autowired
	private OutcomeDao dao;

	@Override
	public List<OutcomeVo> findByTeacher(Integer teacherId) {
		return mapper.findByTeacher(teacherId);
	}

	@Override
	public void add(Outcome outcome) {
		dao.save(outcome);
	}

}
