package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.vo.LessonSelectArrangeVo;

public interface LessonSelectService {
	/**
	 * 学生可选课程，排除已选课程
	 * @param studentId
	 * @return
	 */
	List<LessonSelectArrangeVo> lessonForStudentSelect(Integer studentId);
	/**
	 * 选择课程
	 * @param studentId
	 * @param arrangeId
	 */
	void selectLesson(Integer studentId, Integer arrangeId);
	/**
	 * 学生已选课程
	 * @param studentId
	 * @return
	 */
	List<LessonSelectArrangeVo> lessonStudentSelected(Integer studentId);
	/**
	 * 删除学生已选课程
	 * @param studentId
	 * @param arrangeId
	 */
	void deleteSelect(Integer studentId, Integer arrangeId);
}
