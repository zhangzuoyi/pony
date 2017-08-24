package com.zzy.pony.disk.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.disk.model.DiskInfo;
import com.zzy.pony.disk.model.DiskShare;

public interface DiskShareDao extends JpaRepository<DiskShare,Long> {
	List<DiskShare> findByCreator(String creator);
	DiskShare findByDiskInfo(DiskInfo diskInfo);
}
