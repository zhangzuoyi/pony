package com.zzy.pony.exam.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.exam.model.ExamMonitor;



public interface ExamMonitorDao extends JpaRepository<ExamMonitor, Integer> {
	
}
