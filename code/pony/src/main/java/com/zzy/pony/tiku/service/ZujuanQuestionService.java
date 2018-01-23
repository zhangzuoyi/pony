package com.zzy.pony.tiku.service;

import com.zzy.pony.tiku.model.ZujuanQuestion;
import com.zzy.pony.tiku.vo.ZujuanQuestionVo;

import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-22
 * @Description
 */
public interface ZujuanQuestionService {

    List<ZujuanQuestionVo> listQuestion(long zujuanId);


    ZujuanQuestion findById(long id);
    void add(ZujuanQuestion zujuanQuestion);
    void update(ZujuanQuestion zujuanQuestion);
    void deleteById(long id);

    void deleteByZujuanId(long zujuanId);


}
