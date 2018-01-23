package com.zzy.pony.tiku.mapper;


import com.zzy.pony.tiku.model.ZujuanQuestion;
import com.zzy.pony.tiku.vo.QuestionVo;
import com.zzy.pony.vo.ConditionVo;

import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-18
 * @Description
 */
public interface QuestionMapper {
    List<QuestionVo> findPage(ConditionVo cv);
    int findCount(ConditionVo cv);

    List<QuestionVo> findByIds(List<Long> ids);


}
