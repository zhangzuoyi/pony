package com.zzy.pony.oa.controller;

import com.zzy.pony.config.Constants;
import com.zzy.pony.oa.model.Task;
import com.zzy.pony.oa.service.TaskService;
import com.zzy.pony.oa.vo.TaskVo;
import com.zzy.pony.util.DateTimeUtil;
import com.zzy.pony.vo.ConditionVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
@Controller
@RequestMapping(value="/oa/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "main")
    public String main(){
        return "oa/task/main";
    }

    @RequestMapping(value = "list",method = RequestMethod.POST)
    @ResponseBody
    public Page<TaskVo> list(@RequestBody ConditionVo cv ){
        cv.setStartNum(cv.getCurrentPage()*cv.getPageSize());
        return taskService.list(cv);
    }

    @RequestMapping(value = "listMy",method = RequestMethod.POST)
    @ResponseBody
    public Page<TaskVo> listMy(@RequestBody ConditionVo cv ){
        cv.setStartNum(cv.getCurrentPage()*cv.getPageSize());
        return taskService.listMy(cv);
    }

    @RequestMapping(value = "add",method = RequestMethod.POST)
    @ResponseBody
    public long add(@RequestBody TaskVo taskVo){
        Task task = new Task();
        task.setName(taskVo.getName());
        task.setAccess(taskVo.getAccess());
        task.setAssignee(StringUtils.join(taskVo.getAssignee(),','));
        task.setCc(StringUtils.join(taskVo.getCc(),','));
        task.setDescription(taskVo.getDescription());
        task.setStartTime(taskVo.getStartTime() );
        task.setEndTime(taskVo.getEndTime());
        task.setMembers(StringUtils.join(taskVo.getMembers(),','));
        task.setTags(taskVo.getTags());
        return  taskService.add(task);
    }

    @RequestMapping(value = "addFile",method = RequestMethod.POST)
    @ResponseBody
    public void addFile(MultipartFile fileUpload, HttpServletRequest request,@RequestParam(value = "id") long id){
        MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest)request;
        MultipartFile file = multipartRequest.getFile("fileUpload");
        taskService.addFile(file,id, Constants.OA_TARGETTYPE_ONE);
    }

    @RequestMapping(value = "delete",method = RequestMethod.GET)
    @ResponseBody
    public void add(@RequestParam(value = "id") long id){
        taskService.delete(id);
    }






}
