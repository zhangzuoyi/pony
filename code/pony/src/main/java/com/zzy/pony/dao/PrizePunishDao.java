package com.zzy.pony.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.PrizePunish;
import com.zzy.pony.model.Student;

public interface PrizePunishDao extends JpaRepository<PrizePunish, Integer> {
	List<PrizePunish> findByStudent(Student student);
}