package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.vo.ArrangeVo;







public interface AutoLessonArrangeService {
	boolean autoLessonArrange(int gradeId);
	void save(List<ArrangeVo> list);
}
