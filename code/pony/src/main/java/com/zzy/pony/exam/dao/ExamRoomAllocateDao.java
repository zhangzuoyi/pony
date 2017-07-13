package com.zzy.pony.exam.dao;

import com.zzy.pony.exam.model.ExamRoom;
import com.zzy.pony.exam.model.ExamRoomAllocate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamRoomAllocateDao extends JpaRepository<ExamRoomAllocate,Integer> {

}
