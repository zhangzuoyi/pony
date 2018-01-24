package com.zzy.pony.tiku.mapper;

import com.zzy.pony.tiku.model.Record;

import java.util.List;

public interface RecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Record record);

    Record selectByPrimaryKey(Long id);

    int update(Record record);

    void addBatch(List<Record> records);
}