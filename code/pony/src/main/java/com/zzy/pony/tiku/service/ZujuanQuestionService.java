package com.zzy.pony.tiku.service;

import com.zzy.pony.tiku.model.ZujuanQuestion;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-22
 * @Description
 */
public interface ZujuanQuestionService {

    ZujuanQuestion findById(long id);
    void add(ZujuanQuestion zujuanQuestion);
    void update(ZujuanQuestion zujuanQuestion);
    void deleteById(long id);

}
