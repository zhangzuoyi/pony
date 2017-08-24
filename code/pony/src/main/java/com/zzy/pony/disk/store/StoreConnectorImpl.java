package com.zzy.pony.disk.store;

import javax.activation.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.disk.dao.StoreInfoDao;
import com.zzy.pony.disk.model.StoreInfo;
import com.zzy.pony.disk.service.StoreService;
@Service
public class StoreConnectorImpl implements StoreConnector {
    private Logger logger = LoggerFactory.getLogger(StoreConnectorImpl.class);
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreInfoDao storeInfoDao;
    @Autowired
    private InternalStoreConnector internalStoreConnector;

    public StoreDTO saveStore(String model, DataSource dataSource,
            String tenantId) throws Exception {
        return storeService.saveStore(model, dataSource, tenantId);
    }

    public StoreDTO saveStore(String model, String key, DataSource dataSource,
            String tenantId) throws Exception {
        return storeService.saveStore(model, key, dataSource, tenantId);
    }

    public StoreDTO getStore(String model, String key, String tenantId)
            throws Exception {
        StoreDTO storeDto = internalStoreConnector.getStore(model, key,
                tenantId);

        if (storeDto == null) {
            return null;
        }

        StoreInfo storeInfo = storeInfoDao.findByPathAndTenantId(key, tenantId);

        if (storeInfo == null) {
            storeDto.setDisplayName(key);
        } else {
            storeDto.setDisplayName(storeInfo.getName());
        }

        return storeDto;
    }

    public void removeStore(String model, String key, String tenantId)
            throws Exception {
        internalStoreConnector.removeStore(model, key, tenantId);
    }
}
