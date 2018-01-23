package com.zzy.pony.tiku.service;

import com.zzy.pony.tiku.vo.QuestionVo;
import com.zzy.pony.vo.ConditionVo;
import org.springframework.data.domain.Page;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-18
 * @Description
 */
public interface QuestionService {

    Page<QuestionVo> list(ConditionVo cv);


}
