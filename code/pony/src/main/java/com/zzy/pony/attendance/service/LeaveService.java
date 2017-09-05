package com.zzy.pony.attendance.service;

import java.util.List;

import com.zzy.pony.attendance.model.Leave;

public interface LeaveService {
	void add(Leave leave);
	List<Leave> findTodoTasks(String loginName);
}
