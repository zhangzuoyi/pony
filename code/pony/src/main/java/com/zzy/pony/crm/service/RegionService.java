package com.zzy.pony.crm.service;

import java.util.List;

import com.zzy.pony.crm.model.Area;
import com.zzy.pony.crm.model.Province;

public interface RegionService {
	List<Area> areas();
	List<Province> provinces();
}
