package com.zzy.pony.disk.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.disk.model.StoreInfo;

public interface StoreInfoDao extends JpaRepository<StoreInfo,Long> {
	StoreInfo findByPathAndTenantId(String path, String tenantId);
}
