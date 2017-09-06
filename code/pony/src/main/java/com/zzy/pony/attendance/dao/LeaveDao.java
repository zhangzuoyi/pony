package com.zzy.pony.attendance.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.attendance.model.Leave;


public interface LeaveDao extends JpaRepository<Leave,Long>{


}
