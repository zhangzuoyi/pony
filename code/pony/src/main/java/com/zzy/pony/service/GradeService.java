package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.Grade;

public interface GradeService {
	void add(Grade sy);
	List<Grade> findAll();
	Grade get(int id);
	void update(Grade sy);
	void delete(int id);
	List<Grade> findByGradeIdIn(Integer[] gradeIds);
}
