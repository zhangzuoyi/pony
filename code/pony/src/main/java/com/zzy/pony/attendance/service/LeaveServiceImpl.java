package com.zzy.pony.attendance.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.attendance.dao.LeaveDao;
import com.zzy.pony.attendance.model.Leave;
@Service
@Transactional
public class LeaveServiceImpl implements LeaveService {
	private static final Logger logger=LoggerFactory.getLogger(LeaveServiceImpl.class);
	
	@Autowired
	private LeaveDao dao;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
    protected TaskService taskService;
	@Autowired
    protected HistoryService historyService;
	@Autowired
    protected RepositoryService repositoryService;
    @Autowired
    private IdentityService identityService;

	@Override
	public void add(Leave leave) {
		dao.save(leave);

		String businessKey = leave.getId().toString();

        ProcessInstance processInstance = null;
        try {
            // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(leave.getUser().getLoginName());

            processInstance = runtimeService.startProcessInstanceByKey("leave", businessKey, new HashMap<String, Object>());
            String processInstanceId = processInstance.getId();
            leave.setProcessInstanceId(processInstanceId);
            logger.debug("start process of {key={}, bkey={}, pid={}", new Object[]{"leave", businessKey, processInstanceId});
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
	}

	@Override
	public List<Leave> findTodoTasks(String loginName) {
		List<Leave> results = new ArrayList<Leave>();
		// 根据当前人的ID查询
        TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey("leave").taskCandidateOrAssigned(loginName);
        List<Task> tasks = taskQuery.list();
        // 根据流程的业务ID查询实体并关联
        for (Task task : tasks) {
            String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
            if (processInstance == null) {
                continue;
            }
            String businessKey = processInstance.getBusinessKey();
            if (businessKey == null) {
                continue;
            }
            Leave leave = dao.findOne(new Long(businessKey));
            leave.setTask(task);
            results.add(leave);
        }
		return results;
	}

}
