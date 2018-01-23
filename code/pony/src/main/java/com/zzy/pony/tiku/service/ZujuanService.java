package com.zzy.pony.tiku.service;

import com.zzy.pony.tiku.model.Zujuan;
import com.zzy.pony.tiku.vo.ZujuanQuestionVo;
import com.zzy.pony.tiku.vo.ZujuanVo;
import com.zzy.pony.vo.ConditionVo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-22
 * @Description
 */
public interface ZujuanService {

    Page<ZujuanVo> list(ConditionVo cv);

    Zujuan findById(long id);
    void add(Zujuan zujuan);
    void update(Zujuan zujuan);
    void deleteById(long id);
    /*
     * Author: WANGCHAO262
     * Date  : 2018-01-22
     * Description: 根据name批量插入
     */
    void addBatch(String name,String gradeId,String subjectId, List<Long> questionIds);



}
