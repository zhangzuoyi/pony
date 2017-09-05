package com.zzy.pony.exam.service;







public interface ExamineeArrangeService {
	void submitByClass(int examId,int[] classIds,int[] arrangeIds );
	void submitByStudent(int[] examineeIds,int[] arrangeIds);
	void delete(int[] arrangeIds);
	
}
