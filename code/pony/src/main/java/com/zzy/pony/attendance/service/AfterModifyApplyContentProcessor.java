package com.zzy.pony.attendance.service;

import java.util.Date;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.zzy.pony.attendance.dao.LeaveDao;
import com.zzy.pony.attendance.model.Leave;

/**
 * 调整请假内容处理器
 *
 */
@Component
@Transactional
public class AfterModifyApplyContentProcessor implements TaskListener {

    private static final long serialVersionUID = 1L;

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

        leave.setLeaveType((String) delegateTask.getVariable("leaveType"));
        leave.setStartTime((Date) delegateTask.getVariable("startTime"));
        leave.setEndTime((Date) delegateTask.getVariable("endTime"));
        leave.setReason((String) delegateTask.getVariable("reason"));

        leaveDao.save(leave);
    }

}
