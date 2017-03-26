package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.Grade;


public interface GradeDao extends JpaRepository<Grade,Integer>{
	List<Grade> findAllByOrderBySeq();
	List<Grade> findByGradeIdIn(Integer[] gradeIds);
}
