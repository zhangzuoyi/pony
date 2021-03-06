package com.zzy.pony.exam.mapper;

import java.util.List;

import com.zzy.pony.vo.ExamineeRoomArrangeVo;




public interface ExamineeRoomArrangeMapper {
	void deleteByExamId(int examId);
	List<ExamineeRoomArrangeVo> findExamineeRoomArrangeByClassId(int classId,int examId);
	List<ExamineeRoomArrangeVo> findExamineeRoomArrangeByRoomId(String roomId,int examId);
	List<ExamineeRoomArrangeVo> findExamineeRoomArrangeBySubjectId(int subjectId,int examId);
	void insertExamineeRoomArrange(ExamineeRoomArrangeVo vo);
	void insertBatchExamineeRoomArrang(List<ExamineeRoomArrangeVo> examineeRoomArrangeVos);


}
