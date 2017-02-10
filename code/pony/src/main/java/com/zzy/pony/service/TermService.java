package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.Term;

public interface TermService {
	void add(Term sy);
	List<Term> findAll();
	Term get(int id);
	void update(Term sy);
	void delete(int id);
	Term getCurrent();
}
