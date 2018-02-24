package com.zzy.pony.oa.controller;

import com.zzy.pony.config.Constants;
import com.zzy.pony.oa.model.Task;
import com.zzy.pony.oa.model.TaskProgress;
import com.zzy.pony.oa.service.TaskProgressService;
import com.zzy.pony.oa.service.TaskService;
import com.zzy.pony.oa.vo.TaskVo;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.vo.ConditionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
@Controller
@RequestMapping(value="/oa/taskProgress")
public class TaskProgressController {

    @Autowired
    private TaskProgressService taskProgressService;
    @Autowired
    private TaskService taskService;


    @RequestMapping(value = "add",method = RequestMethod.GET)
    @ResponseBody
    public long add(@RequestParam(value="taskId") long taskId,@RequestParam(value="content") String content){
        TaskProgress tp = new TaskProgress();
        tp.setContent(content);
        tp.setTaskId(taskId);
        tp.setCreateTime(new Date());
        tp.setCreateUser(ShiroUtil.getLoginName());
        return  taskProgressService.add(tp);
    }

    @RequestMapping(value = "addFile",method = RequestMethod.POST)
    @ResponseBody
    public void addFile(MultipartFile fileUpload, HttpServletRequest request,@RequestParam(value = "id") long id){
        MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest)request;
        MultipartFile file = multipartRequest.getFile("fileUpload");
        taskService.addFile(file,id, Constants.OA_TARGETTYPE_TWO);
    }

    @RequestMapping(value = "delete",method = RequestMethod.GET)
    @ResponseBody
    public void add(@RequestParam(value = "id") long id){
        taskProgressService.delete(id);
    }






}
