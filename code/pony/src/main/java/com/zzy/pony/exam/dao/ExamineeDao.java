package com.zzy.pony.exam.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.exam.model.Examinee;



public interface ExamineeDao extends JpaRepository<Examinee, Integer> {
	

}
