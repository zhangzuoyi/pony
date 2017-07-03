package com.zzy.pony.controller;



import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zzy.pony.model.*;
import com.zzy.pony.service.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;






































import com.zzy.pony.dao.SchoolClassDao;
import com.zzy.pony.dao.TeacherDao;
import com.zzy.pony.dao.TeacherSubjectDao;
import com.zzy.pony.util.DateTimeUtil;
import com.zzy.pony.vo.TeacherSubjectVo;
import com.zzy.pony.vo.ConditionVo;

@Controller
@RequestMapping(value = "/teacherLesson")
public class TeacherLessonController {
	@Autowired
	private TeacherSubjectService teacherSubjectService;
	@Autowired
	private TeacherDao teacherDao;
	@Autowired
	private TeacherSubjectDao teacherSubjectDao;
	@Autowired
	private SchoolClassDao schoolClassDao;
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	@Autowired
	private SchoolClassService schoolClassService;
	@Autowired
	private GradeService gradeService;
	@Autowired
    private SubjectService subjectService;
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){

	
		return "teacherLesson/main";
	}
	
	@RequestMapping(value="list",method = RequestMethod.POST)
	@ResponseBody
	public List<TeacherSubjectVo> list(@RequestBody ConditionVo cv ){
		List<TeacherSubjectVo> resultList = new ArrayList<TeacherSubjectVo>();
		
		resultList = teacherSubjectService.findCurrentVoByCondition(cv);
		/*if (teacherId != null) {
			Teacher teacher =  teacherDao.findOne(teacherId);
			 resultList =  teacherSubjectService.findCurrentVoByTeacher(teacher);
		}else {
			List<Teacher> teachers = teacherDao.findAll();
			for (Teacher teacher : teachers) {
				List<TeacherSubjectVo> list = teacherSubjectService.findCurrentVoByTeacher(teacher);
				resultList.addAll(list);				
			}
		}*/				
		return resultList;
	}
	@RequestMapping(value="listByTeacher",method = RequestMethod.GET)
	@ResponseBody
	public List<TeacherSubjectVo> listByTeacher(@RequestParam(value="teacherId") int teacherId ){
		List<TeacherSubjectVo> resultList = new ArrayList<TeacherSubjectVo>();
		
			 Teacher teacher =  teacherDao.findOne(teacherId);
			 resultList =  teacherSubjectService.findCurrentVoByTeacher(teacher);
						
		return resultList;
	}
	@RequestMapping(value="listBySchoolClass",method = RequestMethod.GET)
	@ResponseBody
	public List<TeacherSubjectVo> listBySchoolClass(@RequestParam(value="classId")Integer classId ){
		List<TeacherSubjectVo> resultList = new ArrayList<TeacherSubjectVo>();
		if (classId != null) {
			SchoolClass schoolClass =  schoolClassDao.findOne(classId);
			 resultList =  teacherSubjectService.findCurrentVoBySchoolClass(schoolClass);
		}else {
			List<SchoolClass> schoolClasses = schoolClassDao.findAll();
			for (SchoolClass schoolClass : schoolClasses) {
				List<TeacherSubjectVo> list = teacherSubjectService.findCurrentVoBySchoolClass(schoolClass);
				resultList.addAll(list);				
			}
		}				
		return resultList;
	}
	@RequestMapping(value="submit",method = RequestMethod.GET)
	@ResponseBody
	public void submit(@RequestParam(value="teacherIds[]")String[] teacherIds,@RequestParam(value="weekArrange")String weekArrange ){
		if (teacherIds!=null && teacherIds.length>0) {
			for (String teacherId : teacherIds) {
				TeacherSubject  teacherSubject = teacherSubjectDao.findOne(Integer.valueOf(teacherId));
				teacherSubject.setWeekArrange(weekArrange);
				teacherSubjectDao.save(teacherSubject);
			}
		}	
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

		List<SchoolClass> schoolClasseOne = schoolClassService.findByYearAndGradeOrderBySeq(schoolYear.getYearId(), gradeOne.getGradeId());
		List<SchoolClass> schoolClasseTwo = schoolClassService.findByYearAndGradeOrderBySeq(schoolYear.getYearId(), gradeTwo.getGradeId());
		List<SchoolClass> schoolClasseThree = schoolClassService.findByYearAndGradeOrderBySeq(schoolYear.getYearId(), gradeThree.getGradeId());
		int columnLength = schoolClasseOne.size()+schoolClasseTwo.size()+schoolClasseThree.size()+1;


		String title = "平桥中学"+schoolYear.getName()+term.getName()+"教师任课表"; 
		

	
	

				
		
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
            	for (int j = 0; j <= schoolClasseOne.size()+schoolClasseTwo.size()+schoolClasseThree.size(); j++) {
            		cell = row.createCell(j);  
                    cell.setCellStyle(columnTopStyle);  	
				}
			}
            
            // 产生表格标题行  
            HSSFRow titleRow = sheet.getRow(0); 
            HSSFCell titleCell = titleRow.getCell(0);                                                      
            
            sheet.addMergedRegion(new Region(0, (short)0,1, (short)(schoolClasseOne.size()+schoolClasseTwo.size()+schoolClasseThree.size())));    
            titleCell.setCellValue(title);               
                                     
            //产生高一，高二，高三
            HSSFRow subTitleRow = sheet.getRow(2);
            HSSFCell subTitleCell = subTitleRow.getCell(0);
            subTitleCell.setCellValue(new HSSFRichTextString("年级"));
            HSSFCell subTitleOneCell = subTitleRow.getCell(1);
            subTitleOneCell.setCellValue(new HSSFRichTextString("高一"));
            sheet.addMergedRegion(new Region(2,  (short)1, 2,(short)schoolClasseOne.size()));
            HSSFCell subTitleTwoCell = subTitleRow.getCell(schoolClasseOne.size()+1);
            subTitleTwoCell.setCellValue(new HSSFRichTextString("高二"));
            sheet.addMergedRegion(new Region(2,(short)(schoolClasseOne.size()+1),2,  (short)(schoolClasseOne.size()+schoolClasseTwo.size())));
            HSSFCell subTitleThreeCell = subTitleRow.getCell(schoolClasseOne.size()+schoolClasseTwo.size()+1);
            subTitleThreeCell.setCellValue(new HSSFRichTextString("高三"));
            sheet.addMergedRegion(new Region(2, (short)(schoolClasseOne.size()+schoolClasseTwo.size()+1),2, (short)(schoolClasseOne.size()+schoolClasseTwo.size()+schoolClasseThree.size())));
            
            HSSFRow classRow = sheet.createRow(3);
            HSSFCell classFirstCell = classRow.createCell(0);
            classFirstCell.setCellValue(new HSSFRichTextString("班级"));
            classFirstCell.setCellStyle(columnTopStyle);
            for (int i = 1; i <= schoolClasseOne.size(); i++) {
                HSSFCell classCell = classRow.createCell(i);
                classCell.setCellValue(schoolClasseOne.get(i-1).getSeq());
                classCell.setCellStyle(style);
			}
            for (int i = schoolClasseOne.size()+1; i <= schoolClasseOne.size()+schoolClasseTwo.size(); i++) {
                HSSFCell classCell = classRow.createCell(i);
                classCell.setCellValue(schoolClasseTwo.get(i-schoolClasseOne.size()-1).getSeq());
                classCell.setCellStyle(style);
			}
            for (int i = schoolClasseOne.size()+schoolClasseTwo.size()+1; i <= schoolClasseOne.size()+schoolClasseTwo.size()+schoolClasseThree.size(); i++) {
                HSSFCell classCell = classRow.createCell(i);
                classCell.setCellValue(schoolClasseOne.get(i-schoolClasseOne.size()-schoolClasseTwo.size()-1).getSeq());
                classCell.setCellStyle(style);
			}

			List<Object[]> dataList  = new ArrayList<Object[]>();
            //add 班主任
            Object[] tutorTeachers = new Object[columnLength];
            tutorTeachers[0] = "班主任";
            for (int i=1;i<=schoolClasseOne.size();i++){
                SchoolClass schoolClass = schoolClassService.findByYearAndGradeAndSeq(schoolYear.getYearId(),gradeOne.getGradeId(),i);
                tutorTeachers[i] = schoolClass.getTeacher().getName();
            }
            for (int i=1;i<=schoolClasseTwo.size();i++){
                SchoolClass schoolClass = schoolClassService.findByYearAndGradeAndSeq(schoolYear.getYearId(),gradeTwo.getGradeId(),i);
                tutorTeachers[i+schoolClasseOne.size()] = schoolClass.getTeacher().getName();
            }
            for (int i=1;i<=schoolClasseThree.size();i++){
                SchoolClass schoolClass = schoolClassService.findByYearAndGradeAndSeq(schoolYear.getYearId(),gradeThree.getGradeId(),i);
                tutorTeachers[i+schoolClasseOne.size()+schoolClasseTwo.size()] = schoolClass.getTeacher().getName();
            }
            dataList.add(tutorTeachers);
            List<Subject> subjects = subjectService.findAll();
            for (Subject subject:
                 subjects) {
                Object[] subjectObjects = new Object[columnLength];
                subjectObjects[0] = subject.getName();
                for (int i=1;i<=schoolClasseOne.size();i++){
                    SchoolClass schoolClass = schoolClassService.findByYearAndGradeAndSeq(schoolYear.getYearId(),gradeOne.getGradeId(),i);
                    TeacherSubject   teacherSubject = teacherSubjectService.findCurrentByClassAndSubject(schoolClass,subject);
                    if (teacherSubject!= null &&  teacherSubject.getTeacher()!= null){
                        subjectObjects[i] = teacherSubject.getTeacher().getName();
                    }
                }
                for (int i=1;i<=schoolClasseTwo.size();i++){
                    SchoolClass schoolClass = schoolClassService.findByYearAndGradeAndSeq(schoolYear.getYearId(),gradeTwo.getGradeId(),i);
                    TeacherSubject   teacherSubject = teacherSubjectService.findCurrentByClassAndSubject(schoolClass,subject);
                    if (teacherSubject!= null &&  teacherSubject.getTeacher()!= null) {
                        subjectObjects[i + schoolClasseOne.size()] = teacherSubject.getTeacher().getName();
                    }
                }
                for (int i=1;i<=schoolClasseThree.size();i++){
                    SchoolClass schoolClass = schoolClassService.findByYearAndGradeAndSeq(schoolYear.getYearId(),gradeThree.getGradeId(),i);
                    TeacherSubject   teacherSubject = teacherSubjectService.findCurrentByClassAndSubject(schoolClass,subject);
                    if (teacherSubject!= null &&  teacherSubject.getTeacher()!= null) {
                        subjectObjects[i + schoolClasseOne.size() + schoolClasseTwo.size()] = teacherSubject.getTeacher().getName();
                    }
                }
                dataList.add(subjectObjects);
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
                    cell.setCellStyle(style);                                   //设置单元格样式
                }
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
            }  */

          
              
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
