package com.zzy.pony.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.CommonDictDao;
import com.zzy.pony.model.CommonDict;

@Service
public class DictServiceImpl implements DictService {
	@Autowired
	private CommonDictDao dao;

	@Override
	public List<CommonDict> findSexes() {
		return dao.findByDictType("sex");
	}

	@Override
	public List<CommonDict> findCredentials() {
		return dao.findByDictType("credential");
	}

	@Override
	public List<CommonDict> findStudentStatus() {
		return dao.findByDictType("student_status");
	}

	@Override
	public List<CommonDict> findEducationDegrees() {
		return dao.findByDictType("edu_degree");
	}

	@Override
	public List<CommonDict> findStudentRemarkLevels() {
		return dao.findByDictType("stu_remark_level");
	}

}
