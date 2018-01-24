package com.zzy.pony.tiku.controller;

import com.zzy.pony.tiku.model.ZujuanQuestion;
import com.zzy.pony.tiku.service.AnswerService;
import com.zzy.pony.tiku.service.RecordService;
import com.zzy.pony.tiku.vo.ZujuanQuestionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-23
 * @Description
 */
@Controller
@RequestMapping(value="/tiku/test")
public class TestController {

    @Autowired
    private AnswerService answerService;

    @RequestMapping(value="main")
    public String main(){
        return "tiku/test/main";
    }

    @RequestMapping(value="submitTest",method = RequestMethod.POST)
    @ResponseBody
    public void submitTest(@RequestBody List<ZujuanQuestionVo> list){
        answerService.addBatch(list);
    }
}
