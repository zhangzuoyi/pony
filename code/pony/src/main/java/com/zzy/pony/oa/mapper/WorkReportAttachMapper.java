package com.zzy.pony.oa.mapper;

import com.zzy.pony.oa.model.WorkReportAttach;


import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
public interface WorkReportAttachMapper {

    List<WorkReportAttach> findByTypeAndReportId(int type, long reportId);
    List<WorkReportAttach> findByReportId(long reportId);
    WorkReportAttach findByAttachId(long id);
    void addFile(WorkReportAttach workReportAttach);



}
