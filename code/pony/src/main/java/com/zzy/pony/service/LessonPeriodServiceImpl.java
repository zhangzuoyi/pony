package com.zzy.pony.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.LessonPeriodDao;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
@Service
@Transactional
public class LessonPeriodServiceImpl implements LessonPeriodService {
	@Autowired
	private LessonPeriodDao dao;

	@Override
	public void add(LessonPeriod sy) {
		dao.save(sy);

	}

	@Override
	public List<LessonPeriod> findAll() {
		return dao.findAll();
	}

	@Override
	public LessonPeriod get(int id) {
		return dao.findOne(id);
	}

	@Override
	public void update(LessonPeriod sy) {
		LessonPeriod old=dao.findOne(sy.getPeriodId());
		old.setEndTime(sy.getEndTime());
		old.setSchoolYear(sy.getSchoolYear());
		old.setSeq(sy.getSeq());
		old.setStartTime(sy.getStartTime());
		old.setTerm(sy.getTerm());
		
		dao.save(old);
	}

	@Override
	public void delete(int id) {
		dao.delete(id);
		
	}

	@Override
	public List<LessonPeriod> findBySchoolYearAndTerm(SchoolYear year, Term term) {
		return dao.findBySchoolYearAndTermOrderBySeq(year, term);
	}

}
