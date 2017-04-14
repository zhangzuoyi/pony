package com.zzy.pony.attendance.service;

import java.util.List;

import com.zzy.pony.attendance.model.Attendance;

public interface AttendanceService {
	/**
	 * 上班
	 * @param userId
	 */
	void checkIn(int userId);
	/**
	 * 下班
	 * @param userId
	 */
	void checkOut(int userId);
	List<Attendance> findByUser(int userId);
}
