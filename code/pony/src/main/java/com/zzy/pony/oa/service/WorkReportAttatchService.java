package com.zzy.pony.oa.service;

import com.zzy.pony.oa.model.WorkReportAttach;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
public interface WorkReportAttatchService {




    void addFile(MultipartFile file, long reportId, int typeId);
    List<WorkReportAttach> findByReportId(long reportId);
    List<WorkReportAttach> findByTypeAndReportId(int type, long reportId);
    WorkReportAttach findByAttachId(long id);

}
