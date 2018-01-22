package com.zzy.pony.tiku.controller;

import com.zzy.pony.tiku.model.Dict;
import com.zzy.pony.tiku.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-22
 * @Description
 */
@Controller
@RequestMapping(value="/tiku/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    @RequestMapping(value = "grades")
    @ResponseBody
    public List<Dict> getGrades(){
        return  dictService.selectByType("grade");
    }

    @RequestMapping(value = "subjects")
    @ResponseBody
    public List<Dict> getSubjects(){
        return  dictService.selectByType("subject");
    }

    @RequestMapping(value = "questionTypes")
    @ResponseBody
    public List<Dict> getQuestionTypes(){
        return  dictService.selectByType("question_type");
    }




}
