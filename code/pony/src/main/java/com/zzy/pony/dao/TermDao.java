package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.Term;


public interface TermDao extends JpaRepository<Term,Integer>{
	List<Term> findByIsCurrent(String isCurrent);

}
