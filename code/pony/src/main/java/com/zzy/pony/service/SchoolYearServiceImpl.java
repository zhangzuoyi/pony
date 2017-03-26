package com.zzy.pony.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.config.Constants;
import com.zzy.pony.dao.SchoolYearDao;
import com.zzy.pony.model.SchoolYear;
@Service
@Transactional
public class SchoolYearServiceImpl implements SchoolYearService {
	@Autowired
	private SchoolYearDao dao;

	@Override
	public void add(SchoolYear sy) {
		dao.save(sy);

	}

	@Override
	public List<SchoolYear> findAll() {
		return dao.findAll();
	}

	@Override
	public SchoolYear get(int id) {
		return dao.findOne(id);
	}

	@Override
	public void update(SchoolYear sy) {
		SchoolYear old=dao.findOne(sy.getYearId());
		old.setStartYear(sy.getStartYear());
		old.setEndYear(sy.getEndYear());
		
		dao.save(old);
	}

	@Override
	public void delete(int id) {
		dao.delete(id);
		
	}

	@Override
	public SchoolYear getCurrent() {
		List<SchoolYear> list=dao.findByIsCurrent(Constants.CURRENT_FLAG_TRUE);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void setCurrent(Integer id) {
		List<SchoolYear> list=dao.findByIsCurrent(Constants.CURRENT_FLAG_TRUE);
		for(SchoolYear sy: list){
			if(sy.getYearId() != id){
				sy.setIsCurrent(Constants.CURRENT_FLAG_FALSE);
			}
		}
		SchoolYear sy=dao.findOne(id);
		sy.setIsCurrent(Constants.CURRENT_FLAG_TRUE);
		
	}

	@Override
	public SchoolYear findByStartYear(int startYear) {
		List<SchoolYear> list=dao.findByStartYear(startYear);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
