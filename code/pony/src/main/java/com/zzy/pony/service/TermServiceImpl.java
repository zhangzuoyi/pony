package com.zzy.pony.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.config.Constants;
import com.zzy.pony.dao.TermDao;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
@Service
@Transactional
public class TermServiceImpl implements TermService {
	@Autowired
	private TermDao dao;

	@Override
	public void add(Term sy) {
		dao.save(sy);

	}

	@Override
	public List<Term> findAll() {
		return dao.findAll();
	}

	@Override
	public Term get(int id) {
		return dao.findOne(id);
	}

	@Override
	public void update(Term sy) {
		Term old=dao.findOne(sy.getTermId());
		old.setName(sy.getName());
		old.setComments(sy.getComments());
		old.setSeq(sy.getSeq());
		
		dao.save(old);
	}

	@Override
	public void delete(int id) {
		dao.delete(id);
		
	}

	@Override
	public Term getCurrent() {
		List<Term> list=dao.findByIsCurrent(Constants.CURRENT_FLAG_TRUE);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	@Override
	public void setCurrent(Integer id) {
		List<Term> list=dao.findByIsCurrent(Constants.CURRENT_FLAG_TRUE);
		for(Term term: list){
			if(term.getTermId() != id){
				term.setIsCurrent(Constants.CURRENT_FLAG_FALSE);
			}
		}
		Term term=dao.findOne(id);
		term.setIsCurrent(Constants.CURRENT_FLAG_TRUE);
		
	}

}
