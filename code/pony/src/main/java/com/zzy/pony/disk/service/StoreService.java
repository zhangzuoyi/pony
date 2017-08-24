package com.zzy.pony.disk.service;

import java.util.Date;

import javax.activation.DataSource;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzy.pony.disk.dao.StoreInfoDao;
import com.zzy.pony.disk.model.StoreInfo;
import com.zzy.pony.disk.store.InternalStoreConnector;
import com.zzy.pony.disk.store.StoreDTO;

@Service
@Transactional
public class StoreService {
	@Autowired
    private StoreInfoDao storeInfoDao;
	@Autowired
    private InternalStoreConnector internalStoreConnector;

    public StoreDTO saveStore(String model, String key, DataSource dataSource,
            String tenantId) throws Exception {
        StoreDTO storeDto = this.internalStoreConnector.saveStore(model, key,
                dataSource, tenantId);

        StoreInfo storeInfo = new StoreInfo();
        storeInfo.setName(dataSource.getName());
        storeInfo.setModel(model);
        storeInfo.setPath(storeDto.getKey());
        storeInfo.setCreateTime(new Date());
        storeInfo.setTenantId(tenantId);
        storeInfoDao.save(storeInfo);

        return storeDto;
    }

    public StoreDTO saveStore(String model, DataSource dataSource,
            String tenantId) throws Exception {
        StoreDTO storeDto = this.internalStoreConnector.saveStore(model,
                dataSource, tenantId);

        StoreInfo storeInfo = new StoreInfo();
        storeInfo.setName(dataSource.getName());
        storeInfo.setModel(model);
        storeInfo.setPath(storeDto.getKey());
        storeInfo.setCreateTime(new Date());
        storeInfo.setTenantId(tenantId);
        storeInfoDao.save(storeInfo);

        return storeDto;
    }

}
