package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.LessonSelectArrange;
import com.zzy.pony.vo.LessonSelectArrangeVo;

public interface LessonSelectArrangeService {
	void add(LessonSelectArrange sy);
	List<LessonSelectArrange> findAll();
	LessonSelectArrange get(int id);
	void update(LessonSelectArrange sy);
	void delete(int id);
	void add(LessonSelectArrangeVo vo);
	/**
	 * 当前学年和当前学期数据
	 * @return
	 */
	List<LessonSelectArrangeVo> findCurrent();
	List<LessonSelectArrangeVo> findCurrentByGrade(Integer gradeId);
	LessonSelectArrangeVo findById(Integer id);
}
