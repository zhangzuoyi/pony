package com.zzy.pony.attendance.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.zzy.pony.attendance.model.Leave;
import com.zzy.pony.attendance.service.LeaveService;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.UserService;
import com.zzy.pony.util.activiti.Variable;

@Controller
@RequestMapping(value = "/attendance/leave")
public class LeaveController {
	@Autowired
	private LeaveService service;
	@Autowired
	private UserService userService;
	@Autowired
	private TaskService taskService;
	
	@RequestMapping(value="applyMain",method = RequestMethod.GET)
	public String applyMain(Model model){
		model.addAttribute("personName", ShiroUtil.getLoginUser().getShowName());
		return "attendance/leave/apply";
	}

	@RequestMapping(value="apply",method = RequestMethod.POST)
	@ResponseBody
	public String apply(@RequestBody Leave leave, Model model){
		Date now=new Date();
		leave.setCreateTime(now);
		leave.setCreateUser(ShiroUtil.getLoginUser().getLoginName());
		leave.setUpdateTime(now);
		leave.setUpdateUser(ShiroUtil.getLoginUser().getLoginName());
		leave.setUser(userService.findById(ShiroUtil.getLoginUser().getId()));
		service.add(leave);
		return "success";
	}
	
	@RequestMapping(value="tasksMain",method = RequestMethod.GET)
	public String tasksMain(Model model){
		return "attendance/leave/tasks";
	}
	
	@RequestMapping(value="tasks",method = RequestMethod.GET)
	@ResponseBody
	public List<Leave> tasks(Model model){
		return service.findTodoTasks(ShiroUtil.getLoginName());
	}
	
	/**
     * 签收任务
     */
    @RequestMapping(value = "task/claim/{id}",method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> claim(@PathVariable("id") String taskId) {
    	Map<String, String> map=new HashMap<String, String>();
    	map.put("result", "success");
        String userId = ShiroUtil.getLoginName();
        taskService.claim(taskId, userId);
        
        return map;
    }
    @RequestMapping(value = "task/complete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String complete(@PathVariable("id") String taskId, @RequestBody Variable[] vars) {
        try {
            Map<String, Object> variables = new HashMap<String, Object>();
            for(Variable var: vars){
            	var.addVariableMap(variables);
            }
            taskService.complete(taskId, variables);
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}
