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
    @RequestMapping(value="export",method = RequestMethod.GET)
    public void export(HttpServletRequest request,HttpServletResponse response) throws Exception{

        try {
            String title = "单科统计";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(title);


            List<StudentSubjectSingleVo> list = subjectSelectSingleService.list();
            HSSFRow  headRow = sheet.createRow(0);
            headRow.createCell(0).setCellValue("科目");
            headRow.createCell(1).setCellValue("人数");
            HSSFRow row = null;
            for(int  i = 1; i<=list.size();i++) {
                row = sheet.createRow(i);
                row.createCell(0).setCellValue(list.get(i-1).getSubjectName());
                row.createCell(1).setCellValue(list.get(i-1).getCountNum());
            }



            if(workbook !=null){
                try
                {
                    String fileName = new String(title.getBytes("utf-8"), "ISO8859-1")+DateTimeUtil.dateToStr(new Date()) + ".xls" ;
                    String headStr = "attachment; filename=\"" + fileName + "\"";
                    response.setContentType("APPLICATION/OCTET-STREAM");
                    response.setHeader("Content-Disposition", headStr);
                    OutputStream out = response.getOutputStream();
                    workbook.write(out);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
