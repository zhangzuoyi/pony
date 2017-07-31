package com.zzy.pony.exam.dao;

import java.util.List;

import com.zzy.pony.exam.model.ExamArrange;
import com.zzy.pony.exam.model.ExamRoomAllocate;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ExamRoomAllocateDao extends JpaRepository<ExamRoomAllocate,Integer> {
		List<ExamRoomAllocate> findByExamArrange(ExamArrange examArrange);
}
