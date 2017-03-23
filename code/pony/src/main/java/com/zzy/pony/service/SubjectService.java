package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.Subject;

public interface SubjectService {
	void add(Subject sy);
	List<Subject> findAll();
	Subject get(int id);
	void update(Subject sy);
	void delete(int id);
	/**
	 * 查找上课科目
	 * @return
	 */
	List<Subject> findClassSubject();
	/**
	 * 查找选修科目
	 * @return
	 */
	List<Subject> findSelectiveSubject();
	/**
	 * 查找主修科目
	 * @return
	 */
	List<Subject> findMajorSubject();
}
