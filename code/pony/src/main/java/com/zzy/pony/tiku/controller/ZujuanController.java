package com.zzy.pony.tiku.controller;

import com.zzy.pony.tiku.service.ZujuanService;
import com.zzy.pony.tiku.vo.QuestionVo;
import com.zzy.pony.tiku.vo.ZujuanVo;
import com.zzy.pony.vo.ConditionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-18
 * @Description
 */
@Controller
@RequestMapping(value = "/tiku/zujuan")
public class ZujuanController {

    @Autowired
    private ZujuanService zujuanService;

    @RequestMapping(value = "main")
    public String main() {
        return "tiku/zujuan/main";
    }


    @RequestMapping(value="list",method = RequestMethod.POST)
    @ResponseBody
    public Page<ZujuanVo> list(@RequestBody ConditionVo cv){
        return zujuanService.list(cv);

    }


    @RequestMapping(value="add")
    @ResponseBody
    public void add(@RequestParam(value="name") String name,
                    @RequestParam(value="gradeId") String gradeId,
                    @RequestParam(value="subjectId") String subjectId,
                    @RequestParam(value="questionIds[]") List<Long> questionIds){

        //get 中文乱码 tomcat server.xml connector  URIEncoding="UTF-8"
        zujuanService.addBatch(name,gradeId,subjectId,questionIds);



    }

}
