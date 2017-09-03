package com.zzy.pony.exam.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zzy.pony.exam.vo.ExamMonitorVo;

public interface ExamMonitorMapper {
	void insert(int examId, int teacherId);
	List<ExamMonitorVo> find(int examId);
	void setCount(int examId, @Param(value="teacherIds")int[] teacherIds, int count);
	void deleteByIds(@Param(value="ids")int[] ids);
}
