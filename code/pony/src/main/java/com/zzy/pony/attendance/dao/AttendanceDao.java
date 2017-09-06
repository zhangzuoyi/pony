package com.zzy.pony.attendance.dao;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.attendance.model.Attendance;
import com.zzy.pony.model.User;


public interface AttendanceDao extends JpaRepository<Attendance,Long>{
	Attendance findByUserAndWorkDate(User user, Date workDate);
	List<Attendance> findByUser(User user);

}
