package com.zzy.pony.controller;




import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzy.pony.config.Constants;
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.model.Term;
import com.zzy.pony.model.Weekday;
import com.zzy.pony.service.AutoLessonArrangeService;
import com.zzy.pony.service.GradeService;
import com.zzy.pony.service.LessonArrangeService;
import com.zzy.pony.service.LessonPeriodService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.TeacherSubjectService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.service.WeekdayService;
import com.zzy.pony.util.DateTimeUtil;
import com.zzy.pony.vo.ConditionVo;



@Controller
@RequestMapping(value = "/autoLessonArrange")
public class AutoLessonArrangeController {
	
	@Autowired
	private AutoLessonArrangeService autoLessonArrangeService;
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	@Autowired
	private LessonArrangeService lessonArrangeService;
	@Autowired
	private LessonPeriodService lessonPeriodService;
	@Autowired
	private WeekdayService weekdayService;
	@Autowired
	private TeacherSubjectService teacherSubjectService;
	@Autowired
	private SchoolClassService schoolClassService;
	@Autowired
	private GradeService gradeService;

	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){	
		return "autoLessonArrange/main";
	}
	
	@RequestMapping(value="autoLessonArrange",method = RequestMethod.GET)
	@ResponseBody
	public void autoLessonArrange(@RequestParam(value="gradeId") int gradeId ){
		//删除当前学年，当前学期所有自动排课和调课类型的数据
		final int grade = gradeId;
		//增加熔断 5min								
		ExecutorService executor = Executors.newSingleThreadExecutor();
		FutureTask<Boolean> future = new FutureTask<Boolean>(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub	
				SchoolYear year = schoolYearService.getCurrent();
				Term term = termService.getCurrent();
				List<LessonArrange> autoList = lessonArrangeService.findBySchooleYearAndTermAndGradeIdAndSourceType(year, term,grade, Constants.SOURCE_TYPE_AUTO);
				List<LessonArrange> changeList = lessonArrangeService.findBySchooleYearAndTermAndGradeIdAndSourceType(year, term,grade, Constants.SOURCE_TYPE_CHANGE);
				autoList.addAll(changeList);
				lessonArrangeService.deleteList(autoList);	
				return autoLessonArrangeService.autoLessonArrange(grade);
			}
			
		});
		executor.execute(future);
		try {
				boolean result = future.get(5, TimeUnit.MINUTES);
			}catch (InterruptedException e) {
				// TODO: handle exception
				e.printStackTrace();
			}catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				future.cancel(true);
				executor.shutdown();
			}
		 								
	}
	
	@RequestMapping(value="listTableData",method = RequestMethod.POST)
	@ResponseBody
	public String listTableData(@RequestBody ConditionVo cv) {
			//新增默认全选功能
			SchoolYear year = schoolYearService.getCurrent();
			Term term = termService.getCurrent();
			List<Weekday> weekdays = weekdayService.findByhaveClass(Constants.HAVECLASS_FLAG_TRUE);
			List<LessonPeriod> lessonPeriods= lessonPeriodService.findBySchoolYearAndTerm(year, term);
			SchoolClass schoolClass = schoolClassService.get(cv.getClassId());
			List<LessonArrange> allLessonArranges=lessonArrangeService.findByClassIdAndSchoolYearAndTerm(cv.getClassId(), year, term);
			List<TeacherSubject> tsList=teacherSubjectService.findCurrentByClass(schoolClass);
			List<Map<String, Object>> dataList =  new ArrayList<Map<String,Object>>();
			for (LessonPeriod lessonPeriod : lessonPeriods) {
				Map<String, Object> map = new HashMap<String, Object>();
				//map.put("period",lessonPeriod.getStartTime()+"--"+lessonPeriod.getEndTime());
				map.put("period", lessonPeriod.getSeq()+"");
				for (Weekday weekday : weekdays) {
					List<LessonArrange> lessonArranges =  getLessonArranges(allLessonArranges,weekday.getSeq()+"", lessonPeriod);//lessonArrangeService.findByClassIdAndSchoolYearAndTermAndWeekDayAndLessonPeriod(cv.getClassId(), year, term, weekday.getSeq()+"", lessonPeriod);
					//新增老师名字
					
					if (lessonArranges != null && lessonArranges.size()>0 ) {					
						StringBuilder sb  = new StringBuilder();
						for (LessonArrange la : lessonArranges) {
							if (la.getSubject() !=null ) {
								TeacherSubject  teacherSubject = getTeacherSubject(tsList,la.getSubject());//teacherSubjectService.findCurrentByClassAndSubject(schoolClass,la.getSubject());						
								sb.append(la.getSubject().getName()+"("+teacherSubject.getTeacher().getName()+");");														
							}
						}																		
						map.put( Constants.WEEKDAYMAP.get(weekday.getSeq()+"") ,sb);
					}					
				}
				dataList.add(map);
			}
			StringBuilder result = new StringBuilder();	
			GsonBuilder gb = new GsonBuilder();
			Gson gson = gb.create();
			String data = gson.toJson(dataList);
		
			result.append("{\"total\"");
			result.append(":");
			result.append(dataList.size());
			result.append(",\"rows\":");
			result.append(data);						
			result.append("}");
		
            return result.toString();
			

	}
	
	private List<LessonArrange> getLessonArranges(List<LessonArrange> allLessonArranges,String weekday,LessonPeriod lessonPeriod){
		List<LessonArrange> list=new ArrayList<LessonArrange>();
		for(LessonArrange la: allLessonArranges){
			if(la.getWeekDay().equals(weekday) && la.getLessonPeriod().getPeriodId().equals(lessonPeriod.getPeriodId())){
				list.add(la);
			}
		}
		return list;
	}

	/* 
     * 导出数据 
     * 
     * 
     */ 
	@SuppressWarnings("deprecation")
	@RequestMapping(value="exportData",method = RequestMethod.GET)	 
    public void export(HttpServletRequest request,HttpServletResponse response) throws Exception{  
        
		SchoolYear schoolYear = schoolYearService.getCurrent(); 
		Term term = termService.getCurrent();
		Grade gradeOne = gradeService.findBySeq(1);
		Grade gradeTwo = gradeService.findBySeq(2);
		Grade gradeThree = gradeService.findBySeq(3);
		List<LessonPeriod> lessonPeriods = lessonPeriodService.findBySchoolYearAndTerm(schoolYear, term);
		List<Weekday> weekdays = weekdayService.findByhaveClassOrderBySeq(Constants.HAVECLASS_FLAG_TRUE);
		
		List<SchoolClass> schoolClasseOne = schoolClassService.findByYearAndGradeOrderBySeq(schoolYear.getYearId(), gradeOne.getGradeId());
		List<SchoolClass> schoolClasseTwo = schoolClassService.findByYearAndGradeOrderBySeq(schoolYear.getYearId(), gradeTwo.getGradeId());
		List<SchoolClass> schoolClasseThree = schoolClassService.findByYearAndGradeOrderBySeq(schoolYear.getYearId(), gradeThree.getGradeId());
		int columnLength = 1+1+schoolClasseOne.size()+schoolClasseTwo.size()+schoolClasseThree.size();


		String title = Constants.SCHOOL_NAME+schoolYear.getName()+term.getName()+"总课程表"; 
					
		
		try{  
            HSSFWorkbook workbook = new HSSFWorkbook();                     // 创建工作簿对象  
            HSSFSheet sheet = workbook.createSheet(title);                  // 创建工作表  
            
            //sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面  - 可扩展】  
            HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);//获取列头样式对象  
            HSSFCellStyle style = this.getStyle(workbook);                  //单元格样式对象  
            HSSFRow row = null;  
            HSSFCell cell = null; 
            for (int i = 0; i < 3; i++) {
            	row = sheet.createRow(i);
            	for (int j = 0; j < columnLength; j++) {
            		cell = row.createCell(j);  
                    cell.setCellStyle(columnTopStyle);  	
				}
			}
            
            // 产生表格标题行  
            HSSFRow titleRow = sheet.getRow(0); 
            HSSFCell titleCell = titleRow.getCell(0);                                                      
            
            sheet.addMergedRegion(new Region(0, (short)0,1, (short)(columnLength)));    
            titleCell.setCellValue(title);               
                                     
            //产生高一，高二，高三
            HSSFRow subTitleRow = sheet.getRow(2);
           /* HSSFCell subTitleCell = subTitleRow.getCell(0);
            subTitleCell.setCellValue(new HSSFRichTextString("年级"));*/
            HSSFCell subTitleOneCell = subTitleRow.getCell(2);
            subTitleOneCell.setCellValue(new HSSFRichTextString("高一"));
            sheet.addMergedRegion(new Region(2,  (short)2, 2,(short)(schoolClasseOne.size()+1)));
            HSSFCell subTitleTwoCell = subTitleRow.getCell(schoolClasseOne.size()+2);
            subTitleTwoCell.setCellValue(new HSSFRichTextString("高二"));
            sheet.addMergedRegion(new Region(2,(short)(schoolClasseOne.size()+2),2,  (short)(schoolClasseOne.size()+schoolClasseTwo.size()+1)));
            HSSFCell subTitleThreeCell = subTitleRow.getCell(schoolClasseOne.size()+schoolClasseTwo.size()+2);
            subTitleThreeCell.setCellValue(new HSSFRichTextString("高三"));
            sheet.addMergedRegion(new Region(2, (short)(schoolClasseOne.size()+schoolClasseTwo.size()+2),2, (short)(schoolClasseOne.size()+schoolClasseTwo.size()+schoolClasseThree.size()+1)));
            
            HSSFRow classRow = sheet.createRow(3);
            HSSFCell classFirstCell = subTitleRow.getCell(0);
            classFirstCell.setCellValue(new HSSFRichTextString("星期"));
            sheet.addMergedRegion(new CellRangeAddress(2, 3, 0, 0));
            HSSFCell classSecondCell = subTitleRow.getCell(1);
            classSecondCell.setCellValue(new HSSFRichTextString("节次"));
            sheet.addMergedRegion(new CellRangeAddress(2, 3, 1, 1));
            
            classFirstCell.setCellStyle(columnTopStyle);
            for (int i = 2; i < schoolClasseOne.size()+2; i++) {
                HSSFCell classCell = classRow.createCell(i);
                classCell.setCellValue(schoolClasseOne.get(i-2).getSeq());
                classCell.setCellStyle(style);
			}
            for (int i = schoolClasseOne.size()+2; i < schoolClasseOne.size()+schoolClasseTwo.size()+2; i++) {
                HSSFCell classCell = classRow.createCell(i);
                classCell.setCellValue(schoolClasseTwo.get(i-schoolClasseOne.size()-2).getSeq());
                classCell.setCellStyle(style);
			}
            for (int i = schoolClasseOne.size()+schoolClasseTwo.size()+2; i < schoolClasseOne.size()+schoolClasseTwo.size()+schoolClasseThree.size()+2; i++) {
                HSSFCell classCell = classRow.createCell(i);
                classCell.setCellValue(schoolClasseOne.get(i-schoolClasseOne.size()-schoolClasseTwo.size()-2).getSeq());
                classCell.setCellStyle(style);
			}

			List<Object[]> dataList  = new ArrayList<Object[]>();
            List<LessonArrange> laList=lessonArrangeService.findBySchoolYearAndTerm(schoolYear, term);
            for (Weekday weekday:
                 weekdays) {                	
            	for (LessonPeriod lessonPeriod : lessonPeriods) {					
            		Object[] subjectObjects = new Object[columnLength];
                    subjectObjects[0] = weekday.getName();
                    subjectObjects[1] = lessonPeriod.getSeq();
                    for (int i=2;i<schoolClasseOne.size()+2;i++){
                        SchoolClass schoolClass = schoolClasseOne.get(i-2);//schoolClassService.findByYearAndGradeAndSeq(schoolYear.getYearId(),gradeOne.getGradeId(),i);
                        subjectObjects[i]  = getLessonArrange(laList,schoolClass,weekday,lessonPeriod);//teacherSubjectService.findCurrentByClassAndSubject(schoolClass,subject);                       
                    }
                    for (int i=2;i<schoolClasseTwo.size()+2;i++){
                        SchoolClass schoolClass = schoolClasseTwo.get(i-2);//schoolClassService.findByYearAndGradeAndSeq(schoolYear.getYearId(),gradeTwo.getGradeId(),i);
                        subjectObjects[i + schoolClasseOne.size()] = getLessonArrange(laList,schoolClass,weekday,lessonPeriod);;
                        
                    }
                    for (int i=2;i<schoolClasseThree.size()+2;i++){
                        SchoolClass schoolClass = schoolClasseThree.get(i-2);//schoolClassService.findByYearAndGradeAndSeq(schoolYear.getYearId(),gradeThree.getGradeId(),i);
                        subjectObjects[i + schoolClasseOne.size() + schoolClasseTwo.size()] =getLessonArrange(laList,schoolClass,weekday,lessonPeriod);
                        
                    }
                    dataList.add(subjectObjects);
            		
				}           	           	            	             
            }

            //将查询出的数据设置到sheet对应的单元格中
            for(int i=0;i<dataList.size();i++){

                Object[] obj = dataList.get(i);//遍历每个对象
                HSSFRow dataRow = sheet.createRow(i+4);//创建所需的行数

                for(int j=0; j<obj.length; j++){
                    HSSFCell  dataCell = null;   //设置单元格的数据类型
                    dataCell = dataRow.createCell(j,HSSFCell.CELL_TYPE_STRING);
                    if(!"".equals(obj[j]) && obj[j] != null){
                            dataCell.setCellValue(obj[j].toString());                       //设置单元格的值
                    }
                    else {
                    	dataCell.setCellValue("");
                    }
                    if (j==2) {
						style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
					}
                    dataCell.setCellStyle(style);                                   //设置单元格样式
                }
            }
            //合并星期
            for (int i = 0; i < weekdays.size(); i++) {
				sheet.addMergedRegion(new CellRangeAddress(4+(i*lessonPeriods.size()), 4+((i+1)*lessonPeriods.size())-1, 0, 0));
			}
            
            


            //让列宽随着导出的列长自动适应  
            /*for (int colNum = 0; colNum < columnLength; colNum++) {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = 4; rowNum < sheet.getLastRowNum(); rowNum++) {
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
                if(colNum == 0){  
                    sheet.setColumnWidth(colNum, (columnWidth-2) * 256);
                }else{  
                    sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
                }  
            } */ 

          
              
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
	private String getLessonArrange(List<LessonArrange> laList,SchoolClass schoolClass,Weekday weekday,LessonPeriod lessonPeriod){
		StringBuffer stringBuffer = new StringBuffer();
		for(LessonArrange la: laList){
			if(la.getClassId()==schoolClass.getClassId()&& la.getWeekDay().equalsIgnoreCase(weekday.getSeq()+"")&&la.getLessonPeriod().getPeriodId().equals(lessonPeriod.getPeriodId())&&la.getSubject()!=null ){
				stringBuffer.append(la.getSubject().getName()+" ");
			}
		}
			return stringBuffer.toString();
		
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
	
	private TeacherSubject getTeacherSubject(List<TeacherSubject> tsList, Subject subject ){
		for(TeacherSubject ts: tsList){
			if(ts.getSubject().getSubjectId().equals(subject.getSubjectId())){
				return ts;
			}
		}
		return null;
	}
}

