package com.zzy.pony.tiku.controller;

import com.zzy.pony.tiku.service.QuestionService;
import com.zzy.pony.tiku.vo.QuestionVo;
import com.zzy.pony.vo.ConditionVo;
import com.zzy.pony.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-18
 * @Description
 */
@Controller
@RequestMapping(value = "/tiku/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @RequestMapping(value = "main")
    public String main(){
        return "tiku/question/main";
    }
    @RequestMapping(value="list",method = RequestMethod.POST)
    @ResponseBody
    public Page<QuestionVo> list(@RequestBody  ConditionVo cv){
        return questionService.list(cv);

    }

}
