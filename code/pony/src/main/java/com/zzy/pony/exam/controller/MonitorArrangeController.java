package com.zzy.pony.exam.controller;





import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zzy.pony.exam.mapper.ExamArrangeMapper;
import com.zzy.pony.exam.mapper.ExamMonitorMapper;
import com.zzy.pony.exam.mapper.ExamRoomAllocateMapper;
import com.zzy.pony.exam.service.ExamMonitorService;
import com.zzy.pony.exam.vo.ExamMonitorVo;
import com.zzy.pony.exam.vo.ExamRoomAllocateVo;
import com.zzy.pony.util.DateTimeUtil;
import com.zzy.pony.util.ExcelUtil;
import com.zzy.pony.util.TemplateUtil;




@Controller
@RequestMapping(value="/examAdmin/monitorArrange")
public class MonitorArrangeController {
	
	@Autowired
	private ExamMonitorService service;
	@Autowired
	private ExamMonitorMapper mapper;
	@Autowired
	private ExamRoomAllocateMapper allocateMapper;
	@Autowired
	private ExamArrangeMapper arrangeMapper;
	
	@RequestMapping(value="main",method=RequestMethod.GET)
	public String main(Model model){
		return "examAdmin/monitorArrange/main";
	}
	
	@RequestMapping(value="list",method=RequestMethod.GET)
	@ResponseBody
	public List<ExamMonitorVo> list(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId, Model model){
		return mapper.find(examId,gradeId);
	}
	
	@RequestMapping(value="submit",method=RequestMethod.POST)
	@ResponseBody
	public void submit(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId,@RequestParam(value="teacherIds[]") int[] teacherIds){
		
		service.add(examId, gradeId, teacherIds);
	}
	
	@RequestMapping(value="setCount",method=RequestMethod.POST)
	@ResponseBody
	public void setCount(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId,
			@RequestParam(value="teacherIds[]") int[] teacherIds,@RequestParam(value="count") int count){
		
		service.setCount(examId,gradeId, teacherIds, count);
	}
	
	@RequestMapping(value="delete",method=RequestMethod.POST)
	@ResponseBody
	public void delete(@RequestParam(value="ids[]") int[] ids){
		service.delete(ids);
	}
	@RequestMapping(value="uploadTeachers",method = RequestMethod.POST)
	@ResponseBody
	public String uploadTeachers(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId,
			@RequestParam(value="file") MultipartFile file, Model model){
		try {
			Workbook wb=WorkbookFactory.create(file.getInputStream());
			Sheet sheet=wb.getSheetAt(0);
			List<ExamMonitorVo> list=new ArrayList<ExamMonitorVo>();
			int i=1;
			while(true){
				Row row=sheet.getRow(i);
				if(row == null){
					break;
				}
				Cell cell=row.getCell(0);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				String teacherNo=cell.getStringCellValue();
				if(StringUtils.isBlank(teacherNo)){
					break;
				}
				int count=(int)row.getCell(2).getNumericCellValue();
				ExamMonitorVo vo=new ExamMonitorVo();
				vo.setMonitorCount(count);
				vo.setTeacherNo(teacherNo);
				list.add(vo);
				i++;
			}
			service.add(examId, gradeId, list);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "success";
	}
	@RequestMapping(value="exportTemplate",method = RequestMethod.GET)
	public ResponseEntity<byte[]> exportTemplate(Model model){
		String fileName="监考老师导入模板.xlsx";
		HttpHeaders headers = new HttpHeaders(); 
		try {
			headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("utf-8"), "ISO8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(TemplateUtil.getContent(fileName), headers, HttpStatus.CREATED);
	}
	@RequestMapping(value="monitorArrange",method=RequestMethod.POST)
	@ResponseBody
	public void monitorArrange(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId){
		service.monitorArrange(examId, gradeId);
	}
	
	@RequestMapping(value="roomList",method=RequestMethod.GET)
	@ResponseBody
	public List<String> roomList(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId, Model model){
		return allocateMapper.roomList(examId, gradeId);
	}
	
	@RequestMapping(value="resultQuery",method=RequestMethod.GET)
	public String resultQuery(Model model){
		return "examAdmin/monitorArrange/query";
	}
	
	@RequestMapping(value="arrangeListByRoom",method=RequestMethod.POST)
	@ResponseBody
	public List<ExamRoomAllocateVo> arrangeListByRoom(@RequestParam(value="examId") int examId,@RequestParam(value="gradeId") int gradeId, @RequestParam(value="room") String room){
		return allocateMapper.findByExamAndRoom(examId,gradeId, room);
	}
	
	@RequestMapping(value = "exportArrangeResult", method = RequestMethod.GET)
	public ResponseEntity<byte[]> exportArrangeResult(Integer examId, Integer gradeId, Model model) {
		String reportName = "监考安排";
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentDispositionFormData("attachment", new String(reportName.getBytes("utf-8"), "ISO8859-1")
					+ DateTimeUtil.dateToStr(new Date()) + ".xls");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(excelContent(examId,gradeId, reportName), headers, HttpStatus.CREATED);
	}
	
	private byte[] excelContent(Integer examId,Integer gradeId, String reportName) {
		List<Map<String, String>> timeList=arrangeMapper.timeList(examId, gradeId);
		List<String> roomList=allocateMapper.roomList(examId, gradeId);
		List<ExamRoomAllocateVo> arranges=allocateMapper.findByExam(examId, gradeId);
		Workbook wb = new HSSFWorkbook();
		String sheetName = reportName;
		Sheet sheet = wb.createSheet(sheetName);

		CellStyle style = ExcelUtil.getCommonStyle(wb);

		CellStyle styleTitle = ExcelUtil.getTitleStyle(wb, style);

		// 设置标题
		Row titleRow = sheet.createRow(0);
		Row titleRow2 = sheet.createRow(1);
		int colIndex=0;
		getCell(sheet,titleRow, "月日", styleTitle, 0, colIndex++,2,1);
		getCell(sheet,titleRow, "星期", styleTitle, 0, colIndex++,2,1);
		getCell(sheet,titleRow, "午别", styleTitle, 0, colIndex++,2,1);
		getCell(sheet,titleRow, "科目\n时间", styleTitle, 0, colIndex++,2,1);
		int roomIndex=1;
		for(String room : roomList){
			getCell(titleRow, String.valueOf(roomIndex++), styleTitle, colIndex);
			getCell(titleRow2, room, styleTitle, colIndex);
			colIndex++;
		}

		// 设置内容
		int voLen = timeList.size();
		for (int i = 0; i < voLen; i++) {
			Map<String, String> vo = timeList.get(i);
			Row row = sheet.createRow(2 + i);
			int cellIndex = 0;
			String examDate=vo.get("examDate");
			String startTime=vo.get("startTime");
			String endTime=vo.get("endTime");
			String subjectName=vo.get("subjectName");
			// 月日
			getCell(row, examDate.substring(5), style, cellIndex++);
			// 星期
			getCell(row, DateTimeUtil.getWeekDayName(DateTimeUtil.strToDate(examDate, "yyyy-MM-dd")), style, cellIndex++);
			// 午别
			getCell(row, getMoonType(startTime), style, cellIndex++);
			// 科目\n时间
			getCell(row, subjectName+"\n"+startTime+"-"+endTime, style, cellIndex++);
			for(String room : roomList){
				ExamRoomAllocateVo ea=getArrange(arranges,room,examDate,startTime, subjectName);
				if(ea != null){
					getCell(row, ea.getTeacherName(), style, cellIndex);
				}
				cellIndex++;
			}
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			wb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
	/**
	 * 午别
	 * @param startTime
	 * @return
	 */
	private String getMoonType(String startTime){
		String str=startTime.substring(0, 2);
		if(Integer.valueOf(str) < 12){
			return "上午";
		}else{
			return "下午";
		}
		
	}
	private ExamRoomAllocateVo getArrange(List<ExamRoomAllocateVo> arranges, String room, String examDate, String startTime, String subjectName){
		for(ExamRoomAllocateVo vo: arranges){
			if(vo.isFit(room, examDate, startTime, subjectName)){
				return vo;
			}
		}
		return null;
	}
	private Cell getCell(Sheet sheet, Row row, String value, CellStyle style, int rowIndex, int colIndex, int rowSpan,
			int colSpan) {
		Cell cell = row.createCell(colIndex);
		cell.setCellStyle(style);
		cell.setCellValue(value);
		if (rowSpan > 1 || colSpan > 1)
			sheet.addMergedRegion(
					new CellRangeAddress(rowIndex, rowIndex + rowSpan - 1, colIndex, colIndex + colSpan - 1));

		return cell;
	}

	private Cell getCell(Row row, String value, CellStyle style, int colIndex) {
		Cell cell = row.createCell(colIndex);
		cell.setCellStyle(style);
		cell.setCellValue(value);
		return cell;
	}
	
	private Cell getCell(Row row, CellStyle style, int colIndex) {
		Cell cell = row.createCell(colIndex);
		cell.setCellStyle(style);
		return cell;
	}
}
