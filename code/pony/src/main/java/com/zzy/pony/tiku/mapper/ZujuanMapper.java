package com.zzy.pony.tiku.mapper;

import com.zzy.pony.tiku.model.Zujuan;
import com.zzy.pony.tiku.vo.QuestionVo;
import com.zzy.pony.tiku.vo.ZujuanVo;
import com.zzy.pony.vo.ConditionVo;

import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-22
 * @Description
 */
public interface ZujuanMapper {
        Zujuan findById(long id);
        void add(Zujuan zujuan);
        void deleteById(long id);
        void update(Zujuan zujuan);

        List<ZujuanVo> findPage(ConditionVo cv);
        int findCount(ConditionVo cv);


}
