package com.zzy.pony.exam.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zzy.pony.exam.vo.ExamMonitorVo;

public interface ExamMonitorMapper {
	void insert(int examId, int gradeId, int teacherId);
	void insertWithCount(int examId, int gradeId, int teacherId, int count);
	List<ExamMonitorVo> find(int examId,int gradeId);
	void setCount(int examId,int gradeId, @Param(value="teacherIds")int[] teacherIds, int count);
	void deleteByIds(@Param(value="ids")int[] ids);
}
