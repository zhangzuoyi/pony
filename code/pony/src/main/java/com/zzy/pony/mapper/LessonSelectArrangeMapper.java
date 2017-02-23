package com.zzy.pony.mapper;

import java.util.List;

import com.zzy.pony.vo.LessonSelectArrangeVo;
import com.zzy.pony.vo.LessonSelectTimeVo;

public interface LessonSelectArrangeMapper {
	/**
	 * 
	 * @param yearId
	 * @param termId
	 * @param gradeId 可选
	 * @return
	 */
	List<LessonSelectArrangeVo> findBySchoolYearAndTerm(Integer yearId, Integer termId,Integer gradeId);
	/**
	 * 
	 * @param yearId
	 * @param termId
	 * @param gradeId 可选
	 * @return
	 */
	List<LessonSelectTimeVo> findTimeBySchoolYearAndTerm(Integer yearId, Integer termId,Integer gradeId);
	LessonSelectArrangeVo findById(Integer arrangeId);
	List<LessonSelectTimeVo> findByArrangeId(Integer arrangeId);
}
