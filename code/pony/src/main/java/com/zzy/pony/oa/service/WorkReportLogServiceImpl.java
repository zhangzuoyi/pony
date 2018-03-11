package com.zzy.pony.oa.service;

import com.zzy.pony.oa.mapper.WorkReportLogMapper;
import com.zzy.pony.oa.model.WorkReportLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: wangchao262
 * @Date: 2018-02-27
 * @Description
 */
@Service
@Transactional
public class WorkReportLogServiceImpl implements WorkReportLogService {

    @Autowired
    private WorkReportLogMapper workReportLogMapper;

    @Override
    public void add(WorkReportLog log) {
        workReportLogMapper.add(log);
    }
}
