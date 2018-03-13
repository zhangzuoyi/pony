package com.zzy.pony.oa.service;

import com.zzy.pony.config.Constants;
import com.zzy.pony.oa.mapper.WorkReportMapper;
import com.zzy.pony.oa.model.WorkReport;
import com.zzy.pony.oa.model.WorkReportLog;
import com.zzy.pony.oa.vo.WorkReportVo;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.vo.ConditionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author: wangchao262
 * @Date: 2018-02-27
 * @Description
 */
@Service
@Transactional
public class WorkReportServiceImpl implements WorkReportService {

    @Autowired
    private WorkReportMapper workReportMapper;
    @Autowired
    private WorkReportLogService workReportLogService;
    @Override
    public Page<WorkReportVo> list(ConditionVo cv) {

        List<WorkReportVo> list=workReportMapper.findPage(cv);
        int count=workReportMapper.findCount(cv);
        Pageable pageable = new PageRequest(cv.getCurrentPage(), cv.getPageSize());
        Page<WorkReportVo> result = new PageImpl<WorkReportVo>(list, pageable, count);
        return result;
    }

    @Override
    public Page<WorkReportVo> listMy(ConditionVo cv) {
        cv.setLoginName(ShiroUtil.getLoginUser().getShowName());
        List<WorkReportVo> list=workReportMapper.findMyPage(cv);
        int count=workReportMapper.findMyCount(cv);
        Pageable pageable = new PageRequest(cv.getCurrentPage(), cv.getPageSize());
        Page<WorkReportVo> result = new PageImpl<WorkReportVo>(list, pageable, count);
        return result;
    }

    @Override
    public WorkReport get(long reportId) {
        return workReportMapper.findOne(reportId);
    }

    @Override
    public long add(WorkReport workReport) {

        workReport.setCreateTime(new Date());
        workReport.setUpdateTime(new Date());
        workReport.setStatus(Constants.OA_STATUS_NEW);
        workReportMapper.add(workReport);

        WorkReportLog log = new WorkReportLog();
        log.setTypeCode(Constants.OA_STATUS_NEW);
        log.setTypeName("新建");
        log.setCreateTime(new Date());
        log.setCreateUser(ShiroUtil.getLoginName());
        log.setReportId(workReport.getId());
        workReportLogService.add(log);

        return workReport.getId();

    }

    @Override
    public void update(WorkReport workReport) {
        WorkReport old = workReportMapper.findOne(workReport.getId());
        old.setUpdateTime(new Date());
        workReportMapper.update(old);
    }

    @Override
    public void delete(long reportId) {
        workReportMapper.delete(reportId);
    }
}
