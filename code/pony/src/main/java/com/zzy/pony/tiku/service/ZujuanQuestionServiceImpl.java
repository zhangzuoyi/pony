package com.zzy.pony.tiku.service;

import com.zzy.pony.tiku.mapper.ZujuanQuestionMapper;
import com.zzy.pony.tiku.model.ZujuanQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-22
 * @Description
 */
@Service
@Transactional
public class ZujuanQuestionServiceImpl implements ZujuanQuestionService {

    @Autowired
    private ZujuanQuestionMapper zujuanQuestionMapper;

    @Override
    public ZujuanQuestion findById(long id) {
        return zujuanQuestionMapper.findById(id);
    }

    @Override
    public void add(ZujuanQuestion zujuanQuestion) {
        zujuanQuestionMapper.add(zujuanQuestion);
    }

    @Override
    public void update(ZujuanQuestion zujuanQuestion) {
        zujuanQuestionMapper.update(zujuanQuestion);
    }

    @Override
    public void deleteById(long id) {
        zujuanQuestionMapper.deleteById(id);
    }
}
