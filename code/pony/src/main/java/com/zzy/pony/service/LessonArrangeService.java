package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.vo.LessonArrangeVo;

public interface LessonArrangeService {
	void add(LessonArrange sy);
	List<LessonArrange> findAll();
	LessonArrange get(int id);
	void update(LessonArrange sy);
	void delete(int id);
	LessonArrangeVo findArrangeVo(Integer classId);
}
