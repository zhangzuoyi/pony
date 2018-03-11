package com.zzy.pony.oa.service;

import com.zzy.pony.oa.mapper.WorkReportRemarkMapper;
import com.zzy.pony.oa.model.WorkReportRemark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: wangchao262
 * @Date: 2018-02-27
 * @Description
 */
@Service
@Transactional
public class WorkReportRemarkServiceImpl implements WorkReportRemarkService {

    @Autowired
    private WorkReportRemarkMapper workReportRemarkMapper;

    @Override
    public WorkReportRemark get(long id) {
        return workReportRemarkMapper.findOne(id);
    }

    @Override
    public long add(WorkReportRemark workReportRemark) {
        return workReportRemarkMapper.add(workReportRemark);
    }

    @Override
    public void update(WorkReportRemark workReportRemark) {
        workReportRemarkMapper.update(workReportRemark);
    }

    @Override
    public void delete(long id) {
        workReportRemarkMapper.delete(id);
    }

    @Override
    public List<WorkReportRemark> findByReportId(long reportId) {
        return workReportRemarkMapper.findByReportId(reportId);
    }
}
