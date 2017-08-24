package com.zzy.pony.disk.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.disk.model.DiskAcl;

public interface DiskAclDao extends JpaRepository<DiskAcl,Long> {

}
