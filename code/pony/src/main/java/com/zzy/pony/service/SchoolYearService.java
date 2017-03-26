package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.SchoolYear;

public interface SchoolYearService {
	void add(SchoolYear sy);
	List<SchoolYear> findAll();
	SchoolYear get(int id);
	void update(SchoolYear sy);
	void delete(int id);
	SchoolYear getCurrent();
	void setCurrent(Integer id);
	SchoolYear findByStartYear(int startYear);
}
