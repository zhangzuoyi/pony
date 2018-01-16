package com.zzy.pony.ss.controller;


import com.zzy.pony.ss.model.SubjectSelectConfig;
import com.zzy.pony.ss.service.SubjectSelectConfigService;
import com.zzy.pony.ss.service.SubjectSelectResultService;
import com.zzy.pony.ss.service.SubjectSelectStatisticsService;
import com.zzy.pony.ss.vo.StudentSubjectResultVo;
import com.zzy.pony.ss.vo.StudentSubjectStatisticsVo;
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
@RequestMapping(value = "/ss/result")
public class SubjectSelectResultController {
	
	@Autowired
	private SubjectSelectResultService subjectSelectResultService;
	@Autowired
    private SubjectSelectConfigService subjectSelectConfigService;

	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "ss/result/main";
	}

	@RequestMapping(value="listByCondition",method = RequestMethod.POST)
	@ResponseBody
	public List<StudentSubjectResultVo> listByCondition(@RequestBody ConditionVo cv){
        if (cv.isStatus()){
            return subjectSelectResultService.findBySelected(cv);
        }else{
            return subjectSelectResultService.findByUnselected(cv);
        }

	}
	

	
	@RequestMapping(value="export",method = RequestMethod.GET)
    public void export(HttpServletRequest request,HttpServletResponse response) throws Exception{  
        
		try {
			String title = "选课结果";
            HSSFWorkbook workbook = new HSSFWorkbook();                       
            HSSFSheet sheet1 = workbook.createSheet("已选名单");
            HSSFSheet sheet2 = workbook.createSheet("未选名单");


            ConditionVo cv = new ConditionVo();
            SubjectSelectConfig config = subjectSelectConfigService.getCurrent();
            cv.setConfigId(config.getConfigId());
            List<StudentSubjectResultVo> selectedList = subjectSelectResultService.findBySelected(cv);
            List<StudentSubjectResultVo> unselectedList = subjectSelectResultService.findByUnselected(cv);
            Collections.sort(selectedList);
            Collections.sort(unselectedList);
            HSSFRow  headRow = sheet1.createRow(0);
            headRow.createCell(0).setCellValue("姓名");
            headRow.createCell(1).setCellValue("班级");
            headRow.createCell(2).setCellValue("选课结果");
            HSSFRow row = null;
            for(int  i = 1; i<=selectedList.size();i++) {
            	row = sheet1.createRow(i);
            	row.createCell(0).setCellValue(selectedList.get(i-1).getStudentName());
            	row.createCell(1).setCellValue(selectedList.get(i-1).getClassName());
            	row.createCell(2).setCellValue(selectedList.get(i-1).getGroupName());
            }

            HSSFRow  headRow2 = sheet2.createRow(0);
            headRow2.createCell(0).setCellValue("姓名");
            headRow2.createCell(1).setCellValue("班级");
            HSSFRow row2= null;
            for(int  i = 1; i<=unselectedList.size();i++) {
                row2 = sheet2.createRow(i);
                row2.createCell(0).setCellValue(unselectedList.get(i-1).getStudentName());
                row2.createCell(1).setCellValue(unselectedList.get(i-1).getClassName());
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
