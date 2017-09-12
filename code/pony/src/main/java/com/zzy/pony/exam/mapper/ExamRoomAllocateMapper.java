package com.zzy.pony.exam.mapper;

import java.util.List;

import com.zzy.pony.exam.model.ExamRoomAllocate;
import com.zzy.pony.exam.vo.ExamRoomAllocateVo;

public interface ExamRoomAllocateMapper {
	List<ExamRoomAllocateVo> findByExam(Integer examId);
	/**
	 * 设置监考老师为空
	 * @param examId
	 */
	void setMonitorEmpty(Integer examId);
	void updateMonitor(Integer roomId,Integer teacherId);
	List<ExamRoomAllocateVo> findByExamAndRoom(Integer examId,String roomName);
	List<String> roomList(Integer examId);
	List<ExamRoomAllocateVo> findByArrangeId(int arrangeId);
}
