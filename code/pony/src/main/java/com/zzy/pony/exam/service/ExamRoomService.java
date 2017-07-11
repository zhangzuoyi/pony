package com.zzy.pony.exam.service;


import java.util.List;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zzy.pony.exam.model.ExamRoom;

public interface ExamRoomService {
	List<ExamRoom> list();
	Page<ExamRoom> findAll(Pageable pageable);
	void add(ExamRoom examroom);
	void update(ExamRoom examroom);
	void delete(int id);
	Boolean isExist(ExamRoom examroom);
}
