package com.zzy.pony.exam.service;











public interface ExamineeRoomArrangeService {
	void autoExamineeRoomArrange(int examId,int gradeId);
	void deleteAll();
	String findExamineeRoomArrangeByClassId(int classId,int gradeId,int examId,String type);//type  select 查询  export 导出
	String findExamineeRoomArrangeByRoomId(String roomId,int gradeId,int examId,String type);
	

	
	
}
