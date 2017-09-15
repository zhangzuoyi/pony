package com.zzy.pony.disk.service;

import java.util.Date;
import java.util.List;

import javax.activation.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zzy.pony.disk.dao.DiskInfoDao;
import com.zzy.pony.disk.model.DiskInfo;
import com.zzy.pony.disk.store.InternalStoreConnector;
import com.zzy.pony.disk.store.StoreConnector;
import com.zzy.pony.disk.store.StoreDTO;
import com.zzy.pony.disk.util.FileUtils;

@Service
public class DiskService {
    private static Logger logger = LoggerFactory.getLogger(DiskService.class);
    @Autowired
    private DiskInfoDao diskInfoDao;
    @Autowired
    private StoreConnector storeConnector;
    @Autowired
    private InternalStoreConnector internalStoreConnector;
    private String baseUrl;

    /**
     * 显示对应用户，对应目录下的所有文件.
     */
    public List<DiskInfo> listFiles(String userId, String parentPath) {

        return diskInfoDao.findByCreatorAndParentPath(userId, parentPath);
    }

    /**
     * 上传文件.
     */
    public DiskInfo createFile(String userId, DataSource dataSource,
            String name, long size, String parentPath, String tenantId)
            throws Exception {
        String modelName = "disk/user/" + userId;
        String keyName = parentPath + "/" + FileUtils.calculateName(name);//parentPath + "/" + name;
        StoreDTO storeDto = storeConnector.saveStore(modelName, keyName,
                dataSource, tenantId);
        String type = FileUtils.getSuffix(name);

        return this.createDiskInfo(userId, name, size, storeDto.getKey(), type,
                1, parentPath);
    }

    /**
     * 新建文件夹.
     */
    public DiskInfo createDir(String userId, String name, String parentPath) {
        internalStoreConnector.mkdir("1/disk/user/" + userId + "/" + parentPath
                + "/" + name);

        return this.createDiskInfo(userId, name, 0, null, "dir", 0, parentPath);
    }

    /**
     * 上传文件，或新建文件夹.
     */
    public DiskInfo createDiskInfo(String userId, String name, long size,
            String ref, String type, int dirType, String parentPath) {
        if (name == null) {
            logger.info("name cannot be null");

            return null;
        }

        name = name.trim();

        if (name.length() == 0) {
            logger.info("name cannot be empty");

            return null;
        }

        if (parentPath == null) {
            parentPath = "";
        } else {
            parentPath = parentPath.trim();
        }

        if (parentPath.length() != 0) {
            if (!parentPath.startsWith("/")) {
                parentPath = "/" + parentPath;
            }

            int index = parentPath.lastIndexOf("/");
            String targetParentPath = parentPath.substring(0, index);
            String targetName = parentPath.substring(index + 1);
            DiskInfo parent = diskInfoDao.findByParentPathAndName(targetParentPath, targetName);

            if (parent == null) {
                logger.info("cannot find : {} {} {}", parentPath,
                        targetParentPath, targetName);

                return null;
            }
        }

        List<String> currentNames = diskInfoDao.findNameByCreatorAndParentPath(userId, parentPath);
        String targetName = FileUtils.calculateName(name, currentNames);

        Date now = new Date();
        DiskInfo diskInfo = new DiskInfo();
        diskInfo.setName(targetName);
        diskInfo.setType(type);
        diskInfo.setFileSize(size);
        diskInfo.setCreator(userId);
        diskInfo.setCreateTime(now);
        diskInfo.setLastModifier(userId);
        diskInfo.setLastModifiedTime(now);
        diskInfo.setDirType(dirType);
        diskInfo.setRef(ref);
        diskInfo.setStatus("active");
        diskInfo.setParentPath(parentPath);
        diskInfoDao.save(diskInfo);

        return diskInfo;
    }

    /**
     * 删除.
     */
    public String remove(Long id) {
        DiskInfo diskInfo = diskInfoDao.findOne(id);

        if (diskInfo == null) {
            return "";
        }

        diskInfo.setStatus("trash");
        diskInfoDao.save(diskInfo);

        return diskInfo.getParentPath();
    }

    /**
     * 判断是否重复.
     */
    public boolean isDumplicated(String userId, String name, String path) {

        return diskInfoDao.findByParentPathAndNameAndCreator(path, name, userId) != null;
    }

    /**
     * 重命名.
     */
    public String rename(String userId, Long id, String name) {
        DiskInfo diskInfo = diskInfoDao.findOne(id);
        String parentPath = diskInfo.getParentPath();
        String type = FileUtils.getSuffix(name);
        String hql = "select name from DiskInfo where creator=? and parentPath=? and id!=?";
        List<String> currentNames = diskInfoDao.findNameByCondition(userId, parentPath, id);
        String targetName = FileUtils.calculateName(name, currentNames);
        diskInfo.setName(targetName);
        diskInfo.setType(type);
        diskInfoDao.save(diskInfo);

        return parentPath;
    }

    /**
     * 移动.
     */
    public String move(String userId, Long id, Long parentId) {
        DiskInfo diskInfo = diskInfoDao.findOne(id);
        String parentPath = diskInfo.getParentPath();

        if (id == parentId) {
            logger.info("{} is equals {}", id, parentId);

            return diskInfo.getParentPath();
        }

        if (parentId != 0) {
            DiskInfo parent = diskInfoDao.findOne(parentId);

            if (!"dir".equals(parent.getType())) {
                logger.info("{}({}) is not directory", parent.getParentPath()
                        + "/" + parent.getName(), parentId);

                return diskInfo.getParentPath();
            }

            String currentPath = diskInfo.getParentPath() + "/"
                    + diskInfo.getName() + "/";
            String checkedParentPath = parent.getParentPath() + "/";

            if ("dir".equals(diskInfo.getType())
                    && checkedParentPath.startsWith(currentPath)) {
                logger.info("{}({}) is sub directory of {}({})",
                        parent.getParentPath() + "/" + parent.getName(),
                        parentId,
                        diskInfo.getParentPath() + "/" + diskInfo.getName(), id);

                return diskInfo.getParentPath();
            }

            diskInfo.setParentPath(parent.getParentPath() + "/"
                    + parent.getName());
        } else {
            diskInfo.setParentPath("");
        }

        String name = diskInfo.getName();
        String hql = "select name from DiskInfo where creator=? and parentPath=? and id!=?";
        List<String> currentNames = diskInfoDao.findNameByCondition(userId, parentPath, id);
        String targetName = FileUtils.calculateName(name, currentNames);
        diskInfo.setName(targetName);

        diskInfoDao.save(diskInfo);

        return parentPath;
    }

    @Value("${application.baseUrl}")
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

	public String getBaseUrl() {
		return baseUrl;
	}
    
}
