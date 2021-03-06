package com.zzy.pony.tiku.service;

import com.zzy.pony.tiku.mapper.DictMapper;
import com.zzy.pony.tiku.model.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-22
 * @Description
 */
@Service("ExamDictServiceImpl")//重命名,防止冲突
@Transactional
public class DictServiceImpl implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Override
    public List<Dict> selectByType(String type) {
        return dictMapper.selectByType(type);
    }
}
