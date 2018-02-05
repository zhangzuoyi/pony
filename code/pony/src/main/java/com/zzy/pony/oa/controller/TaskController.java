package com.zzy.pony.oa.controller;

import com.zzy.pony.oa.service.TaskService;
import com.zzy.pony.oa.vo.TaskVo;
import com.zzy.pony.vo.ConditionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        cv.setStartNum((cv.getCurrentPage()-1)*cv.getPageSize());
        return taskService.list(cv);
    }




}
