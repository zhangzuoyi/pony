package com.zzy.pony.ss.controller;




import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.config.Constants;
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import com.zzy.pony.model.Weekday;
import com.zzy.pony.ss.model.SubjectSelectConfig;
import com.zzy.pony.ss.service.SubjectSelectAdminService;
import com.zzy.pony.ss.service.SubjectSelectConfigService;
import com.zzy.pony.ss.service.SubjectSelectStatisticsService;
import com.zzy.pony.ss.vo.StudentSubjectAdminVo;
import com.zzy.pony.ss.vo.StudentSubjectStatisticsVo;
import com.zzy.pony.util.DateTimeUtil;
import com.zzy.pony.vo.ArrangeVo;
import com.zzy.pony.vo.ConditionVo;

@Controller
@RequestMapping(value = "/ss/statistics")
public class SubjectSelectStatisticsController {
	
	@Autowired
	private SubjectSelectStatisticsService subjectSelectStatisticsService;
	@Autowired
	private SubjectSelectConfigService subjectSelectConfigService;
	@Autowired
	private SubjectSelectAdminService subjectSelectAdminService;
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "ss/statistics/main";
	}

	@RequestMapping(value="totalSelect",method = RequestMethod.GET)
	@ResponseBody
	public int totalSelect(Model model){
		SubjectSelectConfig config = subjectSelectConfigService.getCurrent();
		return subjectSelectStatisticsService.findTotalSelectByConfig(config.getConfigId());
	}
	
	@RequestMapping(value="group",method = RequestMethod.GET)
	@ResponseBody
	public List<StudentSubjectStatisticsVo> group(Model model){
		SubjectSelectConfig config = subjectSelectConfigService.getCurrent();
		return subjectSelectStatisticsService.group(config.getConfigId());					
	}
	
	@RequestMapping(value="exportStatistics",method = RequestMethod.GET)	 
    public void export(HttpServletRequest request,HttpServletResponse response) throws Exception{  
        
		try {
			String title = "选课统计";
            HSSFWorkbook workbook = new HSSFWorkbook();                       
            HSSFSheet sheet = workbook.createSheet(title);    
            SubjectSelectConfig config = subjectSelectConfigService.getCurrent();
            List<StudentSubjectAdminVo> vos = subjectSelectAdminService.list(config.getConfigId(), 0, "");
            Collections.sort(vos);
            HSSFRow  headRow = sheet.createRow(0);
            headRow.createCell(0).setCellValue("姓名");
            headRow.createCell(1).setCellValue("选课结果");
            HSSFRow row = null;
            for(int  i = 1; i<=vos.size();i++) {
            	row = sheet.createRow(i);
            	row.createCell(0).setCellValue(vos.get(i-1).getStudentName());
            	row.createCell(1).setCellValue(vos.get(i-1).getGroup());
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
			// TODO: handle exception
		}
        
    } 
	
	

	

	
}
