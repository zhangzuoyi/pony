package com.zzy.pony.oa.service;

import com.zzy.pony.oa.model.TaskProgress;
import com.zzy.pony.oa.model.WorkReportRemark;

import java.util.List;


/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
public interface WorkReportRemarkService {

    WorkReportRemark get(long id);
    long add(WorkReportRemark workReportRemark);
    void update(WorkReportRemark workReportRemark);
    void delete(long id);
    List<WorkReportRemark> findByReportId(long reportId);



}
