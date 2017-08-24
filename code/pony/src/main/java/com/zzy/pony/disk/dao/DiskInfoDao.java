package com.zzy.pony.disk.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zzy.pony.disk.model.DiskInfo;

public interface DiskInfoDao extends JpaRepository<DiskInfo,Long> {
	@Query("select t from DiskInfo t where t.creator=?1 and t.parentPath=?2 and t.status='active' order by t.dirType")
	List<DiskInfo> findByCreatorAndParentPath(String creator, String parentPath);
	@Query("select t.name from DiskInfo t where t.creator=?1 and t.parentPath=?2")
	List<String> findNameByCreatorAndParentPath(String creator, String parentPath);
	DiskInfo findByParentPathAndName(String parentPath, String name);
	DiskInfo findByParentPathAndNameAndCreator(String parentPath, String name, String creator);
	@Query("select t.name from DiskInfo t where t.creator=?1 and t.parentPath=?2 and t.id != ?3")
	List<String> findNameByCondition(String creator, String parentPath, Long id);
	List<DiskInfo> findByCreatorAndParentPathAndType(String creator, String parentPath,String type);
}
