package com.zzy.pony.exam.controller;



import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.zzy.pony.config.Constants;
import com.zzy.pony.exam.model.ExamRoom;
import com.zzy.pony.exam.service.ExamRoomService;
import com.zzy.pony.exam.service.ExamineeRoomArrangeService;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.service.ExamService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.util.DateTimeUtil;
import com.zzy.pony.vo.GsonVo;



@Controller
@RequestMapping(value="/examAdmin/examineeRoomArrange")
public class ExamineeRoomArrangeController {
	
	@Autowired
	private ExamineeRoomArrangeService examineeRoomArrangeService;
	@Autowired
	private SchoolClassService schoolClassService;
	@Autowired
	private ExamService examService;
	@Autowired
	private ExamRoomService examRoomService;
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private SubjectService subjectService;
	
	

	
	@RequestMapping(value="main",method=RequestMethod.GET)
	public String main(Model model){
		return "examAdmin/examineeRoomArrange/main";
	}
	
	@RequestMapping(value="autoExamineeRoomArrange",method=RequestMethod.GET)
	@ResponseBody
	public void autoExamineeRoomArrange(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId){		
		examineeRoomArrangeService.autoExamineeRoomArrange(examId, gradeId);
	}
	
	@RequestMapping(value="findExamineeRoomArrangeByClassId",method=RequestMethod.GET)
	@ResponseBody
	public String findExamineeRoomArrangeByClassId(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId,
			@RequestParam(value="classId") int classId
			){					
		return examineeRoomArrangeService.findExamineeRoomArrangeByClassId(classId,gradeId, examId,Constants.SELECT);
	}
	
	@RequestMapping(value="findExamineeRoomArrangeByRoomId",method=RequestMethod.GET)
	@ResponseBody
	public String findExamineeRoomArrangeByRoomId(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId,
			@RequestParam(value="roomId") String roomId
			){					
		return examineeRoomArrangeService.findExamineeRoomArrangeByRoomId(roomId,gradeId, examId,Constants.SELECT);
	}
	
	@RequestMapping(value="findExamineeRoomArrangeBySubjectId",method=RequestMethod.GET)
	@ResponseBody
	public String findExamineeRoomArrangeBySubjectId(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId,
			@RequestParam(value="subjectId") int subjectId
			){					
		return examineeRoomArrangeService.findExamineeRoomArrangeBySubjectId(subjectId,gradeId, examId,Constants.SELECT);
	}
	@SuppressWarnings("deprecation")
	@RequestMapping(value="exportByClassId",method = RequestMethod.GET)	 
    public void exportByClassId(HttpServletRequest request,HttpServletResponse response) throws Exception{  
        
		int examId = Integer.valueOf(request.getParameter("examId"));
        int gradeId = Integer.valueOf(request.getParameter("gradeId"));
        SchoolYear year = schoolYearService.getCurrent();
        List<SchoolClass> schoolClasses = schoolClassService.findByYearAndGradeOrderBySeq(year.getYearId(), gradeId);		             
		Exam exam = examService.get(examId);
		String title = exam.getName()+"(班级)"+"考场安排表";
		
		
							
		try{  
            HSSFWorkbook workbook = new HSSFWorkbook();                     // 创建工作簿对象  
    		HSSFSheet sheet = workbook.createSheet(title);                  // 创建工作表   
    		List<Map<String, Object>> headList = new ArrayList<Map<String,Object>>();
    		List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>();
    		
    		

            
            
            for (SchoolClass schoolClass : schoolClasses) {				
            	String gsonString = examineeRoomArrangeService.findExamineeRoomArrangeByClassId(schoolClass.getClassId(),gradeId, examId,Constants.EXPORT);
        		Gson gson = new Gson();
        		GsonVo gsonVo = gson.fromJson(gsonString, GsonVo.class);
        		datas.addAll(gsonVo.getRows()) ;  
        		if (headList ==null || headList.size()<=0) {
        			headList.addAll(gsonVo.getTitle());
				}
        		
            }
        		
        		int columnLength = headList.size();
        		//HSSFSheet sheet = workbook.createSheet(schoolClass.getName());                  // 创建工作表               
                //sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】  
                HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);//获取列头样式对象  
                HSSFCellStyle style = this.getStyle(workbook);                  //单元格样式对象  
                HSSFRow row = null;  
                HSSFCell cell = null; 
                for (int i = 0; i < 2; i++) {
                	row = sheet.createRow(i);
                	for (int j = 0; j < columnLength; j++) {
                		cell = row.createCell(j);  
                        cell.setCellStyle(columnTopStyle);  	
    				}
    			}    

                // 产生表格标题行  
                HSSFRow titleRow = sheet.getRow(0); 
                HSSFCell titleCell = titleRow.getCell(0);                                                                     
                sheet.addMergedRegion(new Region(0, (short)0,1, (short)(columnLength-1)));    
                titleCell.setCellValue(title);                                                                                   
                HSSFRow headRow = sheet.createRow(2);          
                int m = 0;
                String[] keys = new String[headList.size()];
                for (Map<String, Object> head : headList) {
    				HSSFCell headCell = headRow.createCell(m);
    				headCell.setCellValue(head.get("label").toString());
    				headCell.setCellStyle(columnTopStyle);
    				keys[m] = head.get("prop").toString();
    				m++;
    			}
                List<Object[]> dataList  = new ArrayList<Object[]>();  
                for (Map<String, Object> data : datas) {
    				Object[] objects = new Object[columnLength];				
    				for (int n = 0; n < objects.length; n++) {
    					 if(data.get(keys[n]) instanceof Double){
    						 objects[n] = ((Double)(data.get(keys[n]))).intValue(); 
    					 } else {
    						objects[n] = data.get(keys[n]);
    					} //处理Gson反序列化时将int默认转换为double
    				}
    				dataList.add(objects);			
    			}                          			       
                //将查询出的数据设置到sheet对应的单元格中
                for(int i=0;i<dataList.size();i++){
                    Object[] obj = dataList.get(i);//遍历每个对象
                    HSSFRow dataRow = sheet.createRow(i+3);//创建所需的行数
                    for(int j=0; j<obj.length; j++){
                        HSSFCell  dataCell = null;   //设置单元格的数据类型
                        dataCell = dataRow.createCell(j,HSSFCell.CELL_TYPE_STRING);
                        if(!"".equals(obj[j]) && obj[j] != null){
                                dataCell.setCellValue(obj[j].toString());                       //设置单元格的值
                        }
                        else {
                        	dataCell.setCellValue("");
                        }
                        /*if (j==2) {
    						style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
    					}*/
                        dataCell.setCellStyle(style);                                   //设置单元格样式
                    }
                }                                    
          	
			
            
          //让列宽随着导出的列长自动适应  
            for (int colNum = 0; colNum < columnLength; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 3; rowNum < sheet.getLastRowNum(); rowNum++) {
                    HSSFRow currentRow;  
                    //当前行未被使用过  
                    if (sheet.getRow(rowNum) == null) {
                        currentRow = sheet.createRow(rowNum);
                    } else {  
                        currentRow = sheet.getRow(rowNum);
                    }  
                    if (currentRow.getCell(colNum) != null&&!"".equals(currentRow.getCell(colNum))) {  
                        HSSFCell currentCell = currentRow.getCell(colNum);  
                        if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {  
                            int length = currentCell.getStringCellValue().getBytes().length;  
                            if (columnWidth < length) {  
                                columnWidth = length;  
                            }  
                        }  
                    }  
                }  
                /*if(colNum == 0){  
                    sheet.setColumnWidth(colNum, (columnWidth-2) * 256);
                }else{  
                    sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
                } */ 
                sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
            }
            
                                  
            if(workbook !=null){  
                try  
                {
                	String fileName = new String(title.getBytes("utf-8"), "ISO8859-1")+DateTimeUtil.dateToStr(new Date()) + ".xls" ;
                  //  String fileName = "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls";  
                    String headStr = "attachment; filename=\"" + fileName + "\"";  
                  //  response = getResponse();  
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
  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
          
    }
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value="exportByRoomId",method = RequestMethod.GET)	 
    public void exportByRoomId(HttpServletRequest request,HttpServletResponse response) throws Exception{  
        
		int examId = Integer.valueOf(request.getParameter("examId"));
        int gradeId = Integer.valueOf(request.getParameter("gradeId"));
		Exam exam = examService.get(examId);
    	String title = exam.getName()+"(考场)"+"考场安排表";
		List<ExamRoom> examRooms = examRoomService.list();																	
		try{  
            HSSFWorkbook workbook = new HSSFWorkbook();                     // 创建工作簿对象
    		HSSFSheet sheet = workbook.createSheet(title);                  // 创建工作表
    		List<Map<String, Object>> headList = new ArrayList<Map<String,Object>>();
    		List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>();
            
            for (ExamRoom  examRoom : examRooms) {
            	String gsonString = examineeRoomArrangeService.findExamineeRoomArrangeByRoomId(examRoom.getName(),gradeId, examId,Constants.EXPORT);
        		Gson gson = new Gson();
        		GsonVo gsonVo = gson.fromJson(gsonString, GsonVo.class);
        		datas.addAll(gsonVo.getRows()) ;  
        		if (headList ==null || headList.size()<=0) {
        			headList.addAll(gsonVo.getTitle());
				}
            }	
            
        		int columnLength = headList.size();
        		//HSSFSheet sheet = workbook.createSheet(examRoom.getName());                  // 创建工作表
        		//sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】  
                HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);//获取列头样式对象  
                HSSFCellStyle style = this.getStyle(workbook);                  //单元格样式对象  
                HSSFRow row = null;  
                HSSFCell cell = null; 
                            
                for (int i = 0; i < 2; i++) {
                	row = sheet.createRow(i);
                	for (int j = 0; j < columnLength; j++) {
                		cell = row.createCell(j);  
                        cell.setCellStyle(columnTopStyle);  	
    				}
    			}          
            	

                // 产生表格标题行  
                HSSFRow titleRow = sheet.getRow(0); 
                HSSFCell titleCell = titleRow.getCell(0);                                                      
                
                sheet.addMergedRegion(new Region(0, (short)0,1, (short)(columnLength-1)));    
                titleCell.setCellValue(title);               
                                                                      
                HSSFRow headRow = sheet.createRow(2);          
                int m = 0;
                String[] keys = new String[headList.size()];
                for (Map<String, Object> head : headList) {
    				HSSFCell headCell = headRow.createCell(m);
    				headCell.setCellValue(head.get("label").toString());
    				headCell.setCellStyle(columnTopStyle);
    				keys[m] = head.get("prop").toString();
    				m++;
    			}
                List<Object[]> dataList  = new ArrayList<Object[]>();  
                for (Map<String, Object> data : datas) {
    				Object[] objects = new Object[columnLength];				
    				for (int n = 0; n < objects.length; n++) {
    					 if(data.get(keys[n]) instanceof Double){
    						 objects[n] = ((Double)(data.get(keys[n]))).intValue(); 
    					 } else {
    						objects[n] = data.get(keys[n]);
    					} //处理Gson反序列化时将int默认转换为double
    				}
    				dataList.add(objects);			
    			}
                            			       
                //将查询出的数据设置到sheet对应的单元格中
                for(int i=0;i<dataList.size();i++){

                    Object[] obj = dataList.get(i);//遍历每个对象
                    HSSFRow dataRow = sheet.createRow(i+3);//创建所需的行数

                    for(int j=0; j<obj.length; j++){
                        HSSFCell  dataCell = null;   //设置单元格的数据类型
                        dataCell = dataRow.createCell(j,HSSFCell.CELL_TYPE_STRING);
                        if(!"".equals(obj[j]) && obj[j] != null){
                                dataCell.setCellValue(obj[j].toString());                       //设置单元格的值
                        }
                        else {
                        	dataCell.setCellValue("");
                        }
                        /*if (j==2) {
    						style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
    					}*/
                        dataCell.setCellStyle(style);                                   //设置单元格样式
                    }
                }     
 		 
            //让列宽随着导出的列长自动适应  
            for (int colNum = 0; colNum < columnLength; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 3; rowNum < sheet.getLastRowNum(); rowNum++) {
                    HSSFRow currentRow;  
                    //当前行未被使用过  
                    if (sheet.getRow(rowNum) == null) {
                        currentRow = sheet.createRow(rowNum);
                    } else {  
                        currentRow = sheet.getRow(rowNum);
                    }  
                    if (currentRow.getCell(colNum) != null&&!"".equals(currentRow.getCell(colNum))) {  
                        HSSFCell currentCell = currentRow.getCell(colNum);  
                        if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {  
                            int length = currentCell.getStringCellValue().getBytes().length;  
                            if (columnWidth < length) {  
                                columnWidth = length;  
                            }  
                        }  
                    }  
                }  
                /*if(colNum == 0){  
                    sheet.setColumnWidth(colNum, (columnWidth-2) * 256);
                }else{  
                    sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
                } */ 
                sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
            }
            
            if(workbook !=null){  
                try  
                {
                	String fileName = new String(title.getBytes("utf-8"), "ISO8859-1")+DateTimeUtil.dateToStr(new Date()) + ".xls" ;
                  //  String fileName = "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls";  
                    String headStr = "attachment; filename=\"" + fileName + "\"";  
                  //  response = getResponse();  
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
  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
          
    }
	
	@SuppressWarnings("deprecation")
	@RequestMapping(value="exportBySubjectId",method = RequestMethod.GET)	 
    public void exportBySubjectId(HttpServletRequest request,HttpServletResponse response) throws Exception{  
        
		int examId = Integer.valueOf(request.getParameter("examId"));
        int gradeId = Integer.valueOf(request.getParameter("gradeId"));
        int subjectId = Integer.valueOf(request.getParameter("subjectId"));

		Exam exam = examService.get(examId);
		Subject subject = subjectService.get(subjectId);
    	String title = exam.getName()+"("+subject.getName()+")"+"考场安排表";
		try{  
            HSSFWorkbook workbook = new HSSFWorkbook();                     // 创建工作簿对象
    		HSSFSheet sheet = workbook.createSheet(title);                  // 创建工作表
    		List<Map<String, Object>> headList = new ArrayList<Map<String,Object>>();
    		List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>();
          
            	String gsonString = examineeRoomArrangeService.findExamineeRoomArrangeBySubjectId(subjectId,gradeId, examId,Constants.EXPORT);
        		Gson gson = new Gson();
        		GsonVo gsonVo = gson.fromJson(gsonString, GsonVo.class);
        		datas.addAll(gsonVo.getRows()) ;  
        		if (headList ==null || headList.size()<=0) {
        			headList.addAll(gsonVo.getTitle());
				}
            	
            
        		int columnLength = headList.size();
        		//HSSFSheet sheet = workbook.createSheet(examRoom.getName());                  // 创建工作表
        		//sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】  
                HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);//获取列头样式对象  
                HSSFCellStyle style = this.getStyle(workbook);                  //单元格样式对象  
                HSSFRow row = null;  
                HSSFCell cell = null; 
                            
                for (int i = 0; i < 2; i++) {
                	row = sheet.createRow(i);
                	for (int j = 0; j < columnLength; j++) {
                		cell = row.createCell(j);  
                        cell.setCellStyle(columnTopStyle);  	
    				}
    			}          
            	

                // 产生表格标题行  
                HSSFRow titleRow = sheet.getRow(0); 
                HSSFCell titleCell = titleRow.getCell(0);                                                      
                
                sheet.addMergedRegion(new Region(0, (short)0,1, (short)(columnLength-1)));    
                titleCell.setCellValue(title);               
                                                                      
                HSSFRow headRow = sheet.createRow(2);          
                int m = 0;
                String[] keys = new String[headList.size()];
                for (Map<String, Object> head : headList) {
    				HSSFCell headCell = headRow.createCell(m);
    				headCell.setCellValue(head.get("label").toString());
    				headCell.setCellStyle(columnTopStyle);
    				keys[m] = head.get("prop").toString();
    				m++;
    			}
                List<Object[]> dataList  = new ArrayList<Object[]>();  
                for (Map<String, Object> data : datas) {
    				Object[] objects = new Object[columnLength];				
    				for (int n = 0; n < objects.length; n++) {
    					 if(data.get(keys[n]) instanceof Double){
    						 objects[n] = ((Double)(data.get(keys[n]))).intValue(); 
    					 } else {
    						objects[n] = data.get(keys[n]);
    					} //处理Gson反序列化时将int默认转换为double
    				}
    				dataList.add(objects);			
    			}
                            			       
                //将查询出的数据设置到sheet对应的单元格中
                for(int i=0;i<dataList.size();i++){

                    Object[] obj = dataList.get(i);//遍历每个对象
                    HSSFRow dataRow = sheet.createRow(i+3);//创建所需的行数

                    for(int j=0; j<obj.length; j++){
                        HSSFCell  dataCell = null;   //设置单元格的数据类型
                        dataCell = dataRow.createCell(j,HSSFCell.CELL_TYPE_STRING);
                        if(!"".equals(obj[j]) && obj[j] != null){
                                dataCell.setCellValue(obj[j].toString());                       //设置单元格的值
                        }
                        else {
                        	dataCell.setCellValue("");
                        }
                        /*if (j==2) {
    						style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
    					}*/
                        dataCell.setCellStyle(style);                                   //设置单元格样式
                    }
                }     
 		 
            //让列宽随着导出的列长自动适应  
            for (int colNum = 0; colNum < columnLength; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 3; rowNum < sheet.getLastRowNum(); rowNum++) {
                    HSSFRow currentRow;  
                    //当前行未被使用过  
                    if (sheet.getRow(rowNum) == null) {
                        currentRow = sheet.createRow(rowNum);
                    } else {  
                        currentRow = sheet.getRow(rowNum);
                    }  
                    if (currentRow.getCell(colNum) != null&&!"".equals(currentRow.getCell(colNum))) {  
                        HSSFCell currentCell = currentRow.getCell(colNum);  
                        if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {  
                            int length = currentCell.getStringCellValue().getBytes().length;  
                            if (columnWidth < length) {  
                                columnWidth = length;  
                            }  
                        }  
                    }  
                }  
                /*if(colNum == 0){  
                    sheet.setColumnWidth(colNum, (columnWidth-2) * 256);
                }else{  
                    sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
                } */ 
                sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
            }
            
            if(workbook !=null){  
                try  
                {
                	String fileName = new String(title.getBytes("utf-8"), "ISO8859-1")+DateTimeUtil.dateToStr(new Date()) + ".xls" ;
                  //  String fileName = "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls";  
                    String headStr = "attachment; filename=\"" + fileName + "\"";  
                  //  response = getResponse();  
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
  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
          
    }
	
	   /*  
     * 列头单元格样式 
     */      
    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {  
          
          // 设置字体  
          HSSFFont font = workbook.createFont();  
          //设置字体大小  
          font.setFontHeightInPoints((short)11);  
          //字体加粗  
          font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
          //设置字体名字   
          font.setFontName("Courier New");  
          //设置样式;   
          HSSFCellStyle style = workbook.createCellStyle();  
          //设置底边框;   
          style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
          //设置底边框颜色;    
          style.setBottomBorderColor(HSSFColor.BLACK.index);  
          //设置左边框;     
          style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
          //设置左边框颜色;   
          style.setLeftBorderColor(HSSFColor.BLACK.index);  
          //设置右边框;   
          style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
          //设置右边框颜色;   
          style.setRightBorderColor(HSSFColor.BLACK.index);  
          //设置顶边框;   
          style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
          //设置顶边框颜色;    
          style.setTopBorderColor(HSSFColor.BLACK.index);  
          //在样式用应用设置的字体;    
          style.setFont(font);  
          //设置自动换行;   
          style.setWrapText(false);  
          //设置水平对齐的样式为居中对齐;    
          style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
          //设置垂直对齐的样式为居中对齐;   
          style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
            
          return style;  
            
    }  
      
    /*   
     * 列数据信息单元格样式 
     */    
    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {  
          // 设置字体  
          HSSFFont font = workbook.createFont();  
          //设置字体大小  
          //font.setFontHeightInPoints((short)10);  
          //字体加粗  
          //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
          //设置字体名字   
          font.setFontName("Courier New");  
          //设置样式;   
          HSSFCellStyle style = workbook.createCellStyle();  
          //设置底边框;   
          style.setBorderBottom(HSSFCellStyle.BORDER_THIN);  
          //设置底边框颜色;    
          style.setBottomBorderColor(HSSFColor.BLACK.index);  
          //设置左边框;     
          style.setBorderLeft(HSSFCellStyle.BORDER_THIN);  
          //设置左边框颜色;   
          style.setLeftBorderColor(HSSFColor.BLACK.index);  
          //设置右边框;   
          style.setBorderRight(HSSFCellStyle.BORDER_THIN);  
          //设置右边框颜色;   
          style.setRightBorderColor(HSSFColor.BLACK.index);  
          //设置顶边框;   
          style.setBorderTop(HSSFCellStyle.BORDER_THIN);  
          //设置顶边框颜色;    
          style.setTopBorderColor(HSSFColor.BLACK.index);  
          //在样式用应用设置的字体;    
          style.setFont(font);  
          //设置自动换行;   
          style.setWrapText(false);  
          //设置水平对齐的样式为居中对齐;    
          style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
          //设置垂直对齐的样式为居中对齐;   
          style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
           
          return style;  
      
    }
	
	
	
}
