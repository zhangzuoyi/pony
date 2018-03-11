package com.zzy.pony.crm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.crm.dao.AreaDao;
import com.zzy.pony.crm.dao.ProvinceDao;
import com.zzy.pony.crm.model.Area;
import com.zzy.pony.crm.model.Province;
@Service
public class RegionServiceImpl implements RegionService {
	@Autowired
	private AreaDao areaDao;
	@Autowired
	private ProvinceDao provinceDao;

	@Override
	public List<Area> areas() {
		return areaDao.findAll();
	}

	@Override
	public List<Province> provinces() {
		return provinceDao.findAll();
	}

}
