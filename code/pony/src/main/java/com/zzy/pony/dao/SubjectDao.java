package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.Subject;


public interface SubjectDao extends JpaRepository<Subject,Integer>{
	List<Subject> findByTypeIn(List<Integer> types);
	List<Subject> findByType(Integer type);
	List<Subject> findByName(String name);
	List<Subject> findAllByOrderByImportance();

}
