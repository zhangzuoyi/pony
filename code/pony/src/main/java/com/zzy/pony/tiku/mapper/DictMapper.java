package com.zzy.pony.tiku.mapper;

import com.zzy.pony.tiku.model.Dict;

import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-22
 * @Description
 */
public interface DictMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Dict record);

    Dict selectByPrimaryKey(Long id);

    int updateByPrimaryKey(Dict record);

    List<Dict> selectByType(String type);
}
