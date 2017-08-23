package com.zzy.pony.attendance.service;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.zzy.pony.attendance.dao.LeaveDao;
import com.zzy.pony.attendance.model.Leave;

/**
 * 销假后处理器
 * <p>设置销假时间</p>
 * <p>使用Spring代理，可以注入Bean，管理事物</p>
 *
 */
@Component
@Transactional
public class ReportBackEndProcessor implements TaskListener {

    private static final long serialVersionUID = 1L;
    private static final Logger log=LoggerFactory.getLogger(ReportBackEndProcessor.class);

    @Autowired
    LeaveDao leaveDao;

    @Autowired
    RuntimeService runtimeService;

    /* (non-Javadoc)
     * @see org.activiti.engine.delegate.TaskListener#notify(org.activiti.engine.delegate.DelegateTask)
     */
    public void notify(DelegateTask delegateTask) {
        String processInstanceId = delegateTask.getProcessInstanceId();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        Leave leave = leaveDao.findOne(new Long(processInstance.getBusinessKey()));

        Object realityStartTime = delegateTask.getVariable("actualStartTime");
        Object realityEndTime = delegateTask.getVariable("actualEndTime");

        log.info("instanceId:{} leaveId:{} actualStartTime:{} actualEndTime:{}", processInstanceId,leave.getId(),realityStartTime,realityEndTime);
    }

}
