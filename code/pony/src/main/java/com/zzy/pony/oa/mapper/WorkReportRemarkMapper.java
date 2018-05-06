package com.zzy.pony.oa.mapper;

import com.zzy.pony.oa.model.TaskProgress;
import com.zzy.pony.oa.model.WorkReportRemark;

import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
public interface WorkReportRemarkMapper {


    WorkReportRemark findOne(long id);
    void update(WorkReportRemark remark);
    long add(WorkReportRemark remark);
    void delete(long id);
    List<WorkReportRemark> findByReportId(long reportId);

}
