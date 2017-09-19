package com.zzy.pony.exam.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.exam.model.AverageIndex;

public interface AverageIndexDao extends JpaRepository<AverageIndex, Long> {
	List<AverageIndex> findByExamIdAndGradeId(Integer examId, Integer gradeId);
}
