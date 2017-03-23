package com.zzy.pony.service;

import java.util.List;

import com.zzy.pony.model.PrizePunish;

public interface PrizePunishService {
	List<PrizePunish> findByStudent(Integer studentId);

	void add(PrizePunish pp, String loginName);

	void delete(int id);

	void update(PrizePunish pp, String loginName);
}