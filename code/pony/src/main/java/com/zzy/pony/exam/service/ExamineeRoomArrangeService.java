package com.zzy.pony.exam.service;











public interface ExamineeRoomArrangeService {
	void autoExamineeRoomArrange(int examId,int gradeId);
	void deleteAll();
	String findExamineeRoomArrangeByClassId(int classId,int examId);
	String findExamineeRoomArrangeByRoomId(int roomId,int examId);

	
	
}
