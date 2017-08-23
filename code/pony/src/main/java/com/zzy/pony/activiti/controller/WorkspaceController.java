package com.zzy.pony.activiti.controller;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.zzy.pony.activiti.cmd.FindHistoryGraphCmd;
import com.zzy.pony.activiti.cmd.HistoryProcessInstanceDiagramCmd;
import com.zzy.pony.activiti.graph.Graph;
import com.zzy.pony.activiti.service.TraceService;
import com.zzy.pony.security.ShiroUtil;

/**
 * 我的流程 待办流程 已办未结
 */
@Controller
@RequestMapping("activiti")
public class WorkspaceController {
    private static Logger logger = LoggerFactory
            .getLogger(WorkspaceController.class);
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private TraceService traceService;
    private String baseUrl;

    /**
     * 查看历史【包含流程跟踪、任务列表（完成和未完成）、流程变量】.
     */
    @RequestMapping("workspace-viewHistory")
    public String viewHistory(
            @RequestParam("processInstanceId") String processInstanceId,
            Model model) {
        String userId = ShiroUtil.getLoginName();
        HistoryService historyService = processEngine.getHistoryService();
        HistoricProcessInstance historicProcessInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();

        if (userId.equals(historicProcessInstance.getStartUserId())) {
            // startForm
        }

        List<HistoricTaskInstance> historicTasks = historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).list();
        // List<HistoricVariableInstance> historicVariableInstances = historyService
        // .createHistoricVariableInstanceQuery()
        // .processInstanceId(processInstanceId).list();
        model.addAttribute("historicTasks", historicTasks);

        // 获取流程对应的所有人工任务（目前还没有区分历史）
//        List<HumanTaskDTO> humanTasks = humanTaskConnector
//                .findHumanTasksByProcessInstanceId(processInstanceId);
//        List<HumanTaskDTO> humanTaskDtos = new ArrayList<HumanTaskDTO>();
//
//        for (HumanTaskDTO humanTaskDto : humanTasks) {
//            if (humanTaskDto.getParentId() != null) {
//                continue;
//            }
//
//            humanTaskDtos.add(humanTaskDto);
//        }

//        model.addAttribute("humanTasks", humanTaskDtos);
        // model.addAttribute("historicVariableInstances",
        // historicVariableInstances);
        model.addAttribute("nodeDtos",
                traceService.traceProcessInstance(processInstanceId));
        model.addAttribute("historyActivities", processEngine
                .getHistoryService().createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).list());

        if (historicProcessInstance.getEndTime() == null) {
            model.addAttribute("currentActivities", processEngine
                    .getRuntimeService()
                    .getActiveActivityIds(processInstanceId));
        } else {
            model.addAttribute("currentActivities", Collections
                    .singletonList(historicProcessInstance.getEndActivityId()));
        }

        Graph graph = processEngine.getManagementService().executeCommand(
                new FindHistoryGraphCmd(processInstanceId));
        model.addAttribute("graph", graph);
        model.addAttribute("historicProcessInstance", historicProcessInstance);

        return "activiti/workspace-viewHistory";
    }

    /**
     * 流程跟踪
     * 
     * @throws Exception
     */
    @RequestMapping("workspace-graphHistoryProcessInstance")
    public void graphHistoryProcessInstance(
            @RequestParam("processInstanceId") String processInstanceId,
            HttpServletResponse response) throws Exception {
        Command<InputStream> cmd = new HistoryProcessInstanceDiagramCmd(
                processInstanceId);

        InputStream is = processEngine.getManagementService().executeCommand(
                cmd);
        response.setContentType("image/png");

        int len = 0;
        byte[] b = new byte[1024];

        while ((len = is.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }
}
