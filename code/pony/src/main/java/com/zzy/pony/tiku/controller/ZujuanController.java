package com.zzy.pony.tiku.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-18
 * @Description
 */
@Controller
@RequestMapping(value = "/tiku/zujuan")
public class ZujuanController {

    @RequestMapping(value = "main")
    public String main() {
        return "tiku/zujuan/main";
    }

}
