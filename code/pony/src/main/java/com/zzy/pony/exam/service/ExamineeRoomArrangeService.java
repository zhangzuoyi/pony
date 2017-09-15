package com.zzy.pony.exam.service;











public interface ExamineeRoomArrangeService {
	void autoExamineeRoomArrange(int examId,int gradeId);
	void deleteAll();
	String findExamineeRoomArrangeByClassId(int classId,int gradeId,int examId);
	String findExamineeRoomArrangeByRoomId(String roomId,int gradeId,int examId);
	

	
	
}
