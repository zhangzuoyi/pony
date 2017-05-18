package com.zzy.pony.property.service;

import java.util.List;

import com.zzy.pony.property.model.Consumable;







public interface ConsumableAdminService {
	List<Consumable> list();
	Consumable get(int id);
	void add(Consumable consumable);
}
