package com.zzy.pony.service;

import java.util.List;



import com.zzy.pony.vo.ArrangeVo;



public interface PreLessonArrangeService {
	List<ArrangeVo> findVoByClassIdAndSubject(Integer classId,Integer subjectId);
	void deleteByClassIdAndSubject(Integer classId,Integer subjectId);
	void save(ArrangeVo arrangeVo);
}
