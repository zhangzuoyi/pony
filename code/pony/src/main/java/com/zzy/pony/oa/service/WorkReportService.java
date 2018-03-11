package com.zzy.pony.oa.service;

import com.zzy.pony.oa.model.WorkReport;
import com.zzy.pony.oa.vo.WorkReportVo;
import com.zzy.pony.vo.ConditionVo;
import org.springframework.data.domain.Page;



/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
public interface WorkReportService {



    Page<WorkReportVo> list(ConditionVo cv);
    Page<WorkReportVo> listMy(ConditionVo cv);
    WorkReport get(long reportId);
    long add(WorkReport workReport);
    void update(WorkReport workReport);
    void delete(long reportId);



}
