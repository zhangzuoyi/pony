package com.zzy.pony.exam.controller;





import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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

import com.zzy.pony.exam.mapper.ExamArrangeMapper;
import com.zzy.pony.exam.mapper.ExamMonitorMapper;
import com.zzy.pony.exam.mapper.ExamRoomAllocateMapper;
import com.zzy.pony.exam.service.ExamMonitorService;
import com.zzy.pony.exam.vo.ExamMonitorVo;
import com.zzy.pony.exam.vo.ExamRoomAllocateVo;
import com.zzy.pony.util.DateTimeUtil;
import com.zzy.pony.util.ExcelUtil;
import com.zzy.pony.vo.ExamResultVo;




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
	public List<ExamMonitorVo> list(@RequestParam(value="examId") int examId, Model model){
		return mapper.find(examId);
	}
	
	@RequestMapping(value="submit",method=RequestMethod.POST)
	@ResponseBody
	public void submit(@RequestParam(value="examId") int examId,@RequestParam(value="teacherIds[]") int[] teacherIds){
		
		service.add(examId, teacherIds);
	}
	
	@RequestMapping(value="setCount",method=RequestMethod.POST)
	@ResponseBody
	public void setCount(@RequestParam(value="examId") int examId,@RequestParam(value="teacherIds[]") int[] teacherIds,@RequestParam(value="count") int count){
		
		service.setCount(examId, teacherIds, count);
	}
	
	@RequestMapping(value="delete",method=RequestMethod.POST)
	@ResponseBody
	public void delete(@RequestParam(value="ids[]") int[] ids){
		service.delete(ids);
	}
	
	@RequestMapping(value="monitorArrange",method=RequestMethod.POST)
	@ResponseBody
	public void monitorArrange(@RequestParam(value="examId") int examId){
		service.monitorArrange(examId);
	}
	
	@RequestMapping(value="roomList",method=RequestMethod.GET)
	@ResponseBody
	public List<String> roomList(@RequestParam(value="examId") int examId, Model model){
		return allocateMapper.roomList(examId);
	}
	
	@RequestMapping(value="resultQuery",method=RequestMethod.GET)
	public String resultQuery(Model model){
		return "examAdmin/monitorArrange/query";
	}
	
	@RequestMapping(value="arrangeListByRoom",method=RequestMethod.POST)
	@ResponseBody
	public List<ExamRoomAllocateVo> arrangeListByRoom(@RequestParam(value="examId") int examId, @RequestParam(value="room") String room){
		return allocateMapper.findByExamAndRoom(examId, room);
	}
	
	@RequestMapping(value = "exportArrangeResult", method = RequestMethod.GET)
	public ResponseEntity<byte[]> exportArrangeResult(Integer examId, Model model) {
		String reportName = "监考安排";
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentDispositionFormData("attachment", new String(reportName.getBytes("utf-8"), "ISO8859-1")
					+ DateTimeUtil.dateToStr(new Date()) + ".xls");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(excelContent(examId, reportName), headers, HttpStatus.CREATED);
	}
	
	private byte[] excelContent(Integer examId, String reportName) {
		List<Map<String, String>> timeList=arrangeMapper.timeList(examId);
		List<String> roomList=allocateMapper.roomList(examId);
		List<ExamRoomAllocateVo> arranges=allocateMapper.findByExam(examId);
		Workbook wb = new HSSFWorkbook();
		String sheetName = reportName;
		Sheet sheet = wb.createSheet(sheetName);

		CellStyle style = ExcelUtil.getCommonStyle(wb);

		CellStyle styleTitle = ExcelUtil.getTitleStyle(wb, style);

		// 设置标题
		Row titleRow = sheet.createRow(0);
		Row titleRow2 = sheet.createRow(1);
		int colIndex=0;
		getCell(sheet,titleRow, "考试日期", styleTitle, 0, colIndex++,2,1);
		getCell(sheet,titleRow, "开始时间", styleTitle, 0, colIndex++,2,1);
		getCell(sheet,titleRow, "结束时间", styleTitle, 0, colIndex++,2,1);
		for(String room : roomList){
			getCell(sheet,titleRow, room, styleTitle, 0, colIndex,1,3);
			getCell(titleRow2, "年级", styleTitle, colIndex);
			getCell(titleRow2, "考试科目", styleTitle, colIndex+1);
			getCell(titleRow2, "监考老师", styleTitle, colIndex+2);
			colIndex +=3;
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
			// 日期
			getCell(row, examDate, style, cellIndex++);
			// 开始时间
			getCell(row, startTime, style, cellIndex++);
			// 结束时间
			getCell(row, endTime, style, cellIndex++);
			for(String room : roomList){
				ExamRoomAllocateVo ea=getArrange(arranges,room,examDate,startTime);
				if(ea != null){
					getCell(row, ea.getGradeName(), style, cellIndex);
					getCell(row, ea.getSubjectName(), style, cellIndex+1);
					getCell(row, ea.getTeacherName(), style, cellIndex+2);
				}
				cellIndex +=3;
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
	private ExamRoomAllocateVo getArrange(List<ExamRoomAllocateVo> arranges, String room, String examDate, String startTime){
		for(ExamRoomAllocateVo vo: arranges){
			if(vo.isFit(room, examDate, startTime)){
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
