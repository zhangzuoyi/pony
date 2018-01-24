package com.zzy.pony.tiku.service;

import com.zzy.pony.tiku.model.Answer;
import com.zzy.pony.tiku.vo.ZujuanQuestionVo;

import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-24
 * @Description
 */
public interface AnswerService {

    void deleteById(Long id);

    void insert(Answer record);

    Answer selectById(Long id);

    void updateById(Answer record);
    /*
     * Author: WANGCHAO262
     * Date  : 2018-01-24
     * Description: 批量插入record和answer
     */
    void addBatch(List<ZujuanQuestionVo> list);
}
