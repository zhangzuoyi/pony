package com.zzy.pony.disk.service;

import org.springframework.stereotype.Component;

@Component
public class TenantHolder {
	public String getTenantId(){
		return "1";
	}
}
