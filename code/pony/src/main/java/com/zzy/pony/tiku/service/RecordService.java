package com.zzy.pony.tiku.service;

import com.zzy.pony.tiku.model.Answer;
import com.zzy.pony.tiku.model.Record;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-24
 * @Description
 */
public interface RecordService {

    void deleteById(Long id);

    void insert(Record record);

    Record selectById(Long id);

    void updateById(Record record);


}
