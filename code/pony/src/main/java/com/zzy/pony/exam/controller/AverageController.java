package com.zzy.pony.exam.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;
import com.zzy.pony.config.Constants;
import com.zzy.pony.exam.mapper.AverageIndexMapper;
import com.zzy.pony.exam.model.AverageIndex;
import com.zzy.pony.exam.service.AverageService;
import com.zzy.pony.exam.vo.AverageExcelVo;
import com.zzy.pony.exam.vo.AverageIndexRowVo;
import com.zzy.pony.exam.vo.AverageIndexVo;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.service.ExamService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.util.DateTimeUtil;
import com.zzy.pony.util.ReadExcelUtils;
import com.zzy.pony.util.TemplateUtil;
import com.zzy.pony.vo.GsonVo;

@Controller
@RequestMapping(value="/examAdmin/average")
public class AverageController {
	@Autowired
	private AverageService service;
	@Autowired
	private ExamService examService;
	@Autowired
	private SchoolClassService schoolClassService;
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private AverageIndexMapper averageIndexMapper;
	@Autowired
	private SubjectService subjectService;
	
	@RequestMapping(value="main",method=RequestMethod.GET)
	public String main(Model model){
		return "examAdmin/averageByFile/main";
	}
	@RequestMapping(value="getIndexRows",method=RequestMethod.GET)
	@ResponseBody
	public List<AverageIndexRowVo> getIndexRows(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId){
		return service.findIndexRowVo(examId, gradeId);
	}
	@RequestMapping(value="submitIndexList",method=RequestMethod.POST)
	@ResponseBody
	public void submitIndexList(@RequestBody List<AverageIndexRowVo> indexList){
		List<AverageIndex> list=new ArrayList<AverageIndex>();
		for(AverageIndexRowVo vo: indexList) {
			list.addAll(vo.getIndexList());
		}
		service.saveIndexList(indexList.get(0).getExamId(), indexList.get(0).getGradeId(), list);
	}
	@RequestMapping(value="uploadIndexList",method = RequestMethod.POST)
	@ResponseBody
	public String uploadIndexList(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId,
			@RequestParam(value="file") MultipartFile file, Model model){
		try {
			Workbook wb=WorkbookFactory.create(file.getInputStream());
			Sheet sheet=wb.getSheetAt(0);
			Row titleRow=sheet.getRow(0);
			List<String> subjectList=new ArrayList<String>();
			int ti=1;
			while(true) {//取得科目名
				Cell cell=titleRow.getCell(ti);
				if(cell != null && StringUtils.isNoneBlank(cell.getStringCellValue())) {
					subjectList.add(cell.getStringCellValue().trim());
					ti++;
				}else {
					break;
				}
			}
			List<List<Float>> list=new ArrayList<List<Float>>();//按分段存放指标
			int subjectsLen=subjectList.size();
			int i=1;
			while(true){
				Row row=sheet.getRow(i);
				if(row == null){
					break;
				}
				Cell cell=row.getCell(0);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				String section=cell.getStringCellValue();
				if(StringUtils.isBlank(section)){
					break;
				}
				List<Float> values=new ArrayList<Float>();
				for(int j=0;j<subjectsLen;j++) {
					float value = 0f;
					if (row.getCell(j+1).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						 value=(float)row.getCell(j+1).getNumericCellValue();

					}else {
						 value=Float.valueOf(row.getCell(j+1).getStringCellValue());
					}
					//row.getCell(j+1).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					values.add(value);
				}
				list.add(values);
				i++;
				if(i>AverageService.SECTION_COUNT) {
					break;
				}
			}
			service.uploadIndexList(examId, gradeId, subjectList, list);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "success";
	}
	@RequestMapping(value="exportTemplate",method = RequestMethod.GET)
	public ResponseEntity<byte[]> exportTemplate(Model model){
		String fileName="均量值导入模板.xlsx";
		HttpHeaders headers = new HttpHeaders(); 
		try {
			headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("utf-8"), "ISO8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(TemplateUtil.getContent(fileName), headers, HttpStatus.CREATED);
	}
	@SuppressWarnings("deprecation")
	@RequestMapping(value="exportAverage",method = RequestMethod.GET)	 
    public void exportAverage(HttpServletRequest request,HttpServletResponse response) throws Exception{  
        
		int examId = Integer.valueOf(request.getParameter("examId"));
        int gradeId = Integer.valueOf(request.getParameter("gradeId"));
        Exam exam = examService.get(examId);
        String title = exam.getName()+ "均量值统计";
        SchoolYear year = yearService.getCurrent();
        List<SchoolClass> schoolClasses = schoolClassService.findByYearAndGradeOrderBySeq(year.getYearId(), gradeId);
        Map<Integer,Map<String, Map<String, BigDecimal>>> dataMap = service.calculateAverage(examId, gradeId);
				
		try{  
            HSSFWorkbook workbook = new HSSFWorkbook();                     // 创建工作簿对象  
    		HSSFSheet sheet = workbook.createSheet(title);                  // 创建工作表   
    		List<Map<String, Object>> headList = new ArrayList<Map<String,Object>>();
    		List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>();

    		int range = 0 ;
    		for (Integer	 subjectId : dataMap.keySet()) {
    			Map<String, Map<String, BigDecimal>> innerMap = dataMap.get(subjectId);
        		Subject subject = subjectService.get(subjectId);
    			List<AverageIndexVo> averageIndexVos = averageIndexMapper.findByExamAndGradeAndSubject(examId, gradeId,subject.getSubjectId());
    			// 产生表格标题行  
                HSSFRow titleRow = sheet.createRow(range); 
                HSSFCell titleCell = titleRow.createCell(0);                                                                     
                sheet.addMergedRegion(new Region(range, (short)0,range, (short)(1)));    
                titleCell.setCellValue(subject.getName()); 
                HSSFRow headRow = sheet.createRow(range+1);
                headRow.createCell(0).setCellValue("段名");
                headRow.createCell(1).setCellValue("各档指标");
                int colNums = 2;
            	int classSize = 1; 
                for (SchoolClass schoolClass : schoolClasses) {
                	HSSFCell classSeqCell = titleRow.createCell(classSize*2);
                	classSeqCell.setCellValue(schoolClass.getSeq());                	
                	headRow.createCell(classSize*2).setCellValue("档数");
                	headRow.createCell(classSize*2+1).setCellValue("累数");
                	classSize++;
                	colNums +=2;
				}
                titleRow.createCell(colNums).setCellValue("全部");
                headRow.createCell(colNums).setCellValue("档数");
                headRow.createCell(colNums+1).setCellValue("累数");                                
				int index = 0;
                for (String section : innerMap.keySet()) {
                	HSSFRow dataRow = sheet.createRow(range+index+2);
					dataRow.createCell(0).setCellValue(section);
					dataRow.createCell(1).setCellValue(String.valueOf(averageIndexVos.get(index).getIndexValue()));
					for (int j=1;j<=schoolClasses.size();j++) {
						dataRow.createCell(j*2).setCellValue(innerMap.get(section).get("level"+j).toString());
						dataRow.createCell(j*2+1).setCellValue(innerMap.get(section).get("levelSum"+j).toString());						
					}
					dataRow.createCell(schoolClasses.size()*2+2).setCellValue(innerMap.get(section).get("allLevel").toString());
					dataRow.createCell(schoolClasses.size()*2+3).setCellValue(innerMap.get(section).get("allLevelSum").toString());
					index++;
				}
                range += 25;
    		}
          //让列宽随着导出的列长自动适应  
            for (int colNum = 0; colNum < 30; colNum++) {
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
	@RequestMapping(value="exportResult",method = RequestMethod.POST)	 
    public void exportResult(MultipartFile fileUpload,HttpServletRequest request,HttpServletResponse response) throws Exception{  
		MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest)request;
		MultipartFile file = multipartRequest.getFile("upoadfile");		
        if (file == null) {
			return ;
		}
		String title = "均量值统计";
				
		try{  
            HSSFWorkbook workbook = new HSSFWorkbook();                     // 创建工作簿对象  
    		HSSFSheet sheet = workbook.createSheet(title);                  // 创建工作表   
    		List<Map<String, Object>> headList = new ArrayList<Map<String,Object>>();
    		List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>();
   		
    		//Map<String,Map<String, Map<String, BigDecimal>>> result = new LinkedHashMap<String, Map<String,Map<String,BigDecimal>>>();
    		Map<String,Map<String, Map<String, BigDecimal>>> dataMap = new LinkedHashMap<String, Map<String,Map<String,BigDecimal>>>();

			Workbook wb =  ReadExcelUtils.ReadExcelByFile(file);							
			String[] titles = ReadExcelUtils.readExcelTitle(wb);
    		int range = 0 ;

			for (int i=3;i<titles.length;i++) {
					List<AverageExcelVo> averageExcelVos = service.getAverageExcelVo(wb,i);
					List<String> classCodes = service.getClassCode(averageExcelVos,Constants.SCHOOL_NAME);
					service.sortAverageExcelVo(averageExcelVos);
					Map<Integer,List<AverageExcelVo>> levelMap = service.getLevelMap(averageExcelVos);
					Map<Integer,BigDecimal> levelMapDecimal = service.getLevelMapDecimal(averageExcelVos);
					Map<Integer,List<AverageExcelVo>> schoolLevelMap = service.getLevelMapBySchoolName(levelMap,Constants.SCHOOL_NAME);
					Map<Integer,BigDecimal> schoolLevelMapDecimal = service.getLevelMapDecimalBySchoolName(levelMap,levelMapDecimal,Constants.SCHOOL_NAME);
					Map<String, Map<String, BigDecimal>> innerMap = service.calculate(schoolLevelMap, schoolLevelMapDecimal, classCodes);
					dataMap.put(titles[i], innerMap);
					
					
	    			// 产生表格标题行  
	                HSSFRow titleRow = sheet.createRow(range); 
	                HSSFCell titleCell = titleRow.createCell(0);                                                                     
	                sheet.addMergedRegion(new Region(range, (short)0,range, (short)(1)));    
	                titleCell.setCellValue(titles[i]); 
	                HSSFRow headRow = sheet.createRow(range+1);
	                headRow.createCell(0).setCellValue("段名");
	                headRow.createCell(1).setCellValue("各档指标");
	                int colNums = 2;
	            	int classSize = 1; 
	                for (String classCode : classCodes) {
	                	HSSFCell classSeqCell = titleRow.createCell(classSize*2);
	                	classSeqCell.setCellValue(classCode);                	
	                	headRow.createCell(classSize*2).setCellValue("档数");
	                	headRow.createCell(classSize*2+1).setCellValue("累数");
	                	classSize++;
	                	colNums +=2;
					}
	                titleRow.createCell(colNums).setCellValue("全部");
	                headRow.createCell(colNums).setCellValue("档数");
	                headRow.createCell(colNums+1).setCellValue("累数");                                
					int index = 0;
	                for (int section=1;i<=22;i++) {
	                	HSSFRow dataRow = sheet.createRow(range+index+2);
						dataRow.createCell(0).setCellValue("A"+section);
						dataRow.createCell(1).setCellValue(schoolLevelMapDecimal.get(index+1).floatValue());
						int j = 1;
						for (String classCode : classCodes) {
							dataRow.createCell(j*2).setCellValue(innerMap.get(classCode).get("A"+section).toString());
						//	dataRow.createCell(j*2+1).setCellValue(innerMap.get(section).get("levelSum"+j).toString());						
							j++;
						}
						//dataRow.createCell(schoolClasses.size()*2+2).setCellValue(innerMap.get(section).get("allLevel").toString());
						//dataRow.createCell(schoolClasses.size()*2+3).setCellValue(innerMap.get(section).get("allLevelSum").toString());
						index++;
					}
	                range += 25;
					
					
			}										
			   		    		    		
          //让列宽随着导出的列长自动适应  
            for (int colNum = 0; colNum < 30; colNum++) {
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
                sheet.setColumnWidth(colNum, (columnWidth+4) * 256);
            }
                                             
            if(workbook !=null){  
              
                	String fileName = new String(title.getBytes("utf-8"), "ISO8859-1")+DateTimeUtil.dateToStr(new Date()) + ".xls" ;
                  //  String fileName = "Excel-" + String.valueOf(System.currentTimeMillis()).substring(4, 13) + ".xls";  
                    String headStr = "attachment; filename=\"" + fileName + "\"";  
                  //  response = getResponse();  
                    response.setContentType("APPLICATION/OCTET-STREAM");  
                    response.setHeader("Content-Disposition", headStr);  
                    //OutputStream out = response.getOutputStream();  
                   // workbook.write(out); 
                   
                    File localFile = new File(Constants.AVERAGE_PATH);
					FileOutputStream outputStream = new FileOutputStream(localFile);
                    workbook.write(outputStream);
                    
                    
                    
                    
                 
            }  
  
        } catch (IOException e)  
        {  
            e.printStackTrace();  
        } 
		 catch(Exception e){  
            e.printStackTrace();  
        }  
          
    }
	
	
	
}
