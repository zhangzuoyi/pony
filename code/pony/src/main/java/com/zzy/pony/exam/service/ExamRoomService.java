package com.zzy.pony.exam.service;


import java.util.List;


import com.zzy.pony.exam.model.ExamArrange;
import com.zzy.pony.exam.vo.ExamRoomAllocateVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zzy.pony.exam.model.ExamRoom;
import com.zzy.pony.exam.model.ExamRoomAllocate;

public interface ExamRoomService {
	List<ExamRoom> list();
	Page<ExamRoom> findAll(Pageable pageable);
	void add(ExamRoom examroom);
	void update(ExamRoom examroom);
	void delete(int id);
	Boolean isExist(ExamRoom examroom);
	List<ExamRoom> getExamRooms(int[] roomIds);
	void save(int[] subjectIds,int[] roomIds,int examId,int gradeId);
	List<ExamRoomAllocate> findByExamArrangeOrderByRoomSeq(int arrangeId);
	ExamRoom get(int roomId);
	List<ExamRoomAllocateVo> findByArrangeId(int arrangeId);
}
