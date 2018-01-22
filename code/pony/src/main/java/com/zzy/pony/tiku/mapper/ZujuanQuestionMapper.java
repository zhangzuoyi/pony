package com.zzy.pony.tiku.mapper;


import com.zzy.pony.tiku.model.Zujuan;
import com.zzy.pony.tiku.model.ZujuanQuestion;
import com.zzy.pony.tiku.vo.ZujuanQuestionVo;

import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-22
 * @Description
 */
public interface ZujuanQuestionMapper {
    ZujuanQuestion findById(long id);
    void add(ZujuanQuestion zujuanQuestion);
    void addBatch(List<ZujuanQuestion> zujuanQuestions);
    void deleteById(long id);
    void update(ZujuanQuestion zujuanQuestion);

    List<ZujuanQuestionVo> findByZujuanId(long zujuanId);

}
