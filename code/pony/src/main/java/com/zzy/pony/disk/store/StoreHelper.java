package com.zzy.pony.disk.store;

import javax.activation.DataSource;

public interface StoreHelper {
    StoreResult getStore(String model, String key) throws Exception;

    void removeStore(String model, String key) throws Exception;

    StoreResult saveStore(String model, DataSource dataSource) throws Exception;

    StoreResult saveStore(String model, String key, DataSource dataSource)
            throws Exception;

    void mkdir(String path);
}
