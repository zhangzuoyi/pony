package com.zzy.pony.oa.mapper;

import com.zzy.pony.oa.model.WorkReport;
import com.zzy.pony.oa.vo.WorkReportVo;
import com.zzy.pony.vo.ConditionVo;

import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
public interface WorkReportMapper {


    List<WorkReportVo> findPage(ConditionVo cv);
    int findCount(ConditionVo cv);
    List<WorkReportVo> findMyPage(ConditionVo cv);
    int findMyCount(ConditionVo cv);
    WorkReport findOne(long reportId);
    void add(WorkReport workReport);
    void update(WorkReport workReport);
    void delete(long id);





}
