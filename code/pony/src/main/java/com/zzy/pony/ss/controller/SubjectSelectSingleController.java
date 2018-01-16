package com.zzy.pony.ss.controller;


import com.zzy.pony.ss.model.SubjectSelectConfig;
import com.zzy.pony.ss.service.SubjectSelectConfigService;
import com.zzy.pony.ss.service.SubjectSelectResultService;
import com.zzy.pony.ss.service.SubjectSelectSingleService;
import com.zzy.pony.ss.vo.StudentSubjectResultVo;
import com.zzy.pony.ss.vo.StudentSubjectSingleVo;
import com.zzy.pony.util.DateTimeUtil;
import com.zzy.pony.vo.ConditionVo;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/ss/single")
public class SubjectSelectSingleController {


    @Autowired
    private SubjectSelectSingleService subjectSelectSingleService;


    @RequestMapping(value = "main", method = RequestMethod.GET)
    public String main(Model model) {
        return "ss/single/main";
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public List<StudentSubjectSingleVo> list() {

        return subjectSelectSingleService.list();

    }


}
