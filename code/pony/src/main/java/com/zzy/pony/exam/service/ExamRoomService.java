package com.zzy.pony.exam.service;


import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zzy.pony.exam.model.ExamArrange;
import com.zzy.pony.exam.model.ExamRoom;

public interface ExamRoomService {
	List<ExamRoom> list();
}
