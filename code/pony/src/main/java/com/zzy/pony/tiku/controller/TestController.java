package com.zzy.pony.tiku.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-23
 * @Description
 */
@Controller
@RequestMapping(value="/tiku/test")
public class TestController {

    @RequestMapping(value="main")
    public String main(){
        return "tiku/test/main";
    }

    @RequestMapping(value="submitTest",method = RequestMethod.POST)
    @ResponseBody
    public void submitTest(){

    }
}
