package com.zzy.pony.exam.service;

import java.util.List;



import com.zzy.pony.vo.ExamineeRoomArrangeVo;







public interface ExamineeRoomArrangeService {
	void autoExamineeRoomArrange(int examId,int gradeId);
	void deleteAll();
	List<ExamineeRoomArrangeVo> findExamineeRoomArrangeByClassId(int classId,int examId);
	List<ExamineeRoomArrangeVo> findExamineeRoomArrangeByRoomId(int roomId,int examId);

	
	
}
