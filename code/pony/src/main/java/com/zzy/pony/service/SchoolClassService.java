package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.SchoolClass;

public interface SchoolClassService {
	void add(SchoolClass sy);
	List<SchoolClass> findAll();
	SchoolClass get(int id);
	void update(SchoolClass sy);
	void delete(int id);
}
