package com.zzy.pony.service;

import java.util.List;
import java.util.Map;

import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.vo.ArrangeVo;







public interface AutoLessonArrangeService {
	boolean autoLessonArrange(int gradeId);
	boolean autoLessonArrangeTwo(int gradeId);
	void save(List<ArrangeVo> list);
	void saveTwo(Map<Integer,Map<Integer,Integer>> autoArrangeMap, SchoolYear year, Term term,Map<Integer, List<Integer>> rotationSubjectMap);
	void saveUnable(Map<Integer,Map<Integer,Integer>> unableMap,SchoolYear year,Term term,int gradeId);
}
