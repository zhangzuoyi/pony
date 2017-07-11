package com.zzy.pony.exam.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.exam.model.ExamRoom;

public interface ExamRoomDao extends JpaRepository<ExamRoom, Integer> {
	List<ExamRoom> findByName(String name);

}
