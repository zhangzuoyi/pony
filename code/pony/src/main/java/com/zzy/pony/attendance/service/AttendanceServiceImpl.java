package com.zzy.pony.attendance.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.attendance.dao.AttendanceDao;
import com.zzy.pony.attendance.model.Attendance;
import com.zzy.pony.model.User;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {
	@Autowired
	private AttendanceDao dao;

	@Override
	public void checkIn(int userId) {
		Date now=new Date();
		User user=new User();
		user.setUserId(userId);
		Attendance att=dao.findByUserAndWorkDate(user, now);
		if(att == null){
			att=new Attendance();
			att.setStartTime(now);
			att.setUser(user);
			att.setWorkDate(now);
		}else{
			att.setStartTime(now);
		}
		dao.save(att);
	}

	@Override
	public void checkOut(int userId) {
		Date now=new Date();
		User user=new User();
		user.setUserId(userId);
		Attendance att=dao.findByUserAndWorkDate(user, now);
		if(att == null){
			att=new Attendance();
			att.setEndTime(now);
			att.setUser(user);
			att.setWorkDate(now);
		}else{
			att.setEndTime(now);
		}
		dao.save(att);

	}

	@Override
	public List<Attendance> findByUser(int userId) {
		User user=new User();
		user.setUserId(userId);
		return dao.findByUser(user);
	}

}
