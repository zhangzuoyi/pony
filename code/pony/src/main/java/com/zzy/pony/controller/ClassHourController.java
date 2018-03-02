package com.zzy.pony.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

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
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.zzy.pony.model.ClassHourActual;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.Term;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.ClassHourService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.TeacherService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.util.DateTimeUtil;
import com.zzy.pony.util.ExcelUtil;
import com.zzy.pony.vo.ClassHourActualVo;
import com.zzy.pony.vo.ClassHourPlanVo;
import com.zzy.pony.vo.ExamResultVo;
/**
 * 课时管理
 * @author ZHANGZUOYI499
 *
 */
@Controller
@RequestMapping(value = "/classHour")
public class ClassHourController {
	@Autowired
	private ClassHourService service;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private TermService termService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){

		return "classHour/main";
	}
	@RequestMapping(value="businessDateList",method = RequestMethod.GET)
	@ResponseBody
	public List<String> businessDateList( Model model){
		return service.businessDateList();
	}
	@RequestMapping(value="find",method = RequestMethod.GET)
	@ResponseBody
	public List<ClassHourActualVo> find(@RequestParam(value="businessDate") String businessDate, Model model){
		Date bd = DateTimeUtil.strToDate(businessDate, DateTimeUtil.FORMAL_FORMAT);
		return service.findActual(bd);
	}
	@RequestMapping(value="upload",method = RequestMethod.POST)
	@ResponseBody
	public String upload(@RequestParam(value="businessDate") String businessDate, 
			@RequestParam(value="file") MultipartFile file, Model model){
		List<ClassHourActual> list=new ArrayList<ClassHourActual>();
		try {
			Workbook wb=WorkbookFactory.create(file.getInputStream());
			Sheet sheet=wb.getSheetAt(0);
			int i=1;
			Date now=new Date();
			SchoolYear year=yearService.getCurrent();
			Term term=termService.getCurrent();
			Date bd = DateTimeUtil.strToDate(businessDate, DateTimeUtil.FORMAL_FORMAT);
			while(true){
				Row row=sheet.getRow(i);
				i++;
				if(row == null){
					break;
				}
				Cell cell=row.getCell(0);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				String teacherNo=cell.getStringCellValue();
				if(StringUtils.isBlank(teacherNo)){
					break;
				}
				Teacher teacher=teacherService.findByTeacherNo(teacherNo);
				if(teacher == null) {
					continue;
				}
				row.getCell(2).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				double planHour=row.getCell(2).getNumericCellValue();
				row.getCell(3).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				double actualHour=row.getCell(3).getNumericCellValue();
				ClassHourActual ha=new ClassHourActual();
				ha.setActualHours( (int)actualHour);
				ha.setBusinessDate(bd);
				ha.setCreateTime(now);
				ha.setCreateUser(ShiroUtil.getLoginName());
				ha.setPlanHours( (int)planHour);
				ha.setTeacherId(teacher.getTeacherId());
				ha.setTermId(term.getTermId());
				ha.setUpdateTime(now);
				ha.setUpdateUser(ShiroUtil.getLoginName());
				ha.setYearId(year.getYearId());
				list.add(ha);
			}
			service.save(list);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	@RequestMapping(value = "exportTemplate", method = RequestMethod.GET)
	public ResponseEntity<byte[]> export(Integer examId, Integer classId,Integer subjectId,Model model) {
		List<ClassHourPlanVo> vos=service.findCurrentPlan();
		String reportName = "课时导入模板";
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentDispositionFormData("attachment", new String(reportName.getBytes("utf-8"), "ISO8859-1")
					+ DateTimeUtil.dateToStr(new Date()) + ".xls");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(templateExcelContent(vos, reportName), headers, HttpStatus.CREATED);
	}
	
	private byte[] templateExcelContent(List<ClassHourPlanVo> vos, String reportName) {
		Workbook wb = new HSSFWorkbook();
		String sheetName = reportName;
		Sheet sheet = wb.createSheet(sheetName);

		CellStyle style = ExcelUtil.getCommonStyle(wb);

		CellStyle styleTitle = ExcelUtil.getTitleStyle(wb, style);

		// 设置标题
		Row titleRow = sheet.createRow(0);
		getCell(titleRow, "老师编号", styleTitle, 0);
		getCell(titleRow, "老师姓名", styleTitle, 1);
		getCell(titleRow, "计划课时", styleTitle, 2);
		getCell(titleRow, "实际课时", styleTitle, 3);

		// 设置内容
		int voLen = vos.size();
		for (int i = 0; i < voLen; i++) {
			ClassHourPlanVo vo = vos.get(i);
			Row row = sheet.createRow(1 + i);
			int cellIndex = 0;
			// 老师编号
			getCell(row, vo.getTeacherNo(), style, cellIndex++);

			// 老师姓名
			getCell(row, vo.getTeacherName(), style, cellIndex++);
			// 计划课时
			Cell cell=getCell(row, style, cellIndex++);
			cell.setCellValue(vo.getHours());

			// 实际课时，默认为空
			getCell(row, style, cellIndex++);

		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			wb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	private byte[] excelContent(List<ExamResultVo> vos, String reportName) {
		Workbook wb = new HSSFWorkbook();
		String sheetName = reportName;
		Sheet sheet = wb.createSheet(sheetName);

		CellStyle style = ExcelUtil.getCommonStyle(wb);

		CellStyle styleTitle = ExcelUtil.getTitleStyle(wb, style);

		// 设置标题
		Row titleRow = sheet.createRow(0);
		getCell(titleRow, "学号", styleTitle, 0);
		getCell(titleRow, "姓名", styleTitle, 1);
		getCell(titleRow, "成绩", styleTitle, 2);

		// 设置内容
		int voLen = vos.size();
		for (int i = 0; i < voLen; i++) {
			ExamResultVo vo = vos.get(i);
			Row row = sheet.createRow(1 + i);
			int cellIndex = 0;
			// 学号
			getCell(row, vo.getStudentNo(), style, cellIndex++);

			// 姓名
			getCell(row, vo.getStudentName(), style, cellIndex++);

			// 成绩
			Cell cell=getCell(row, style, cellIndex++);
			if(vo.getScore() != null){
				cell.setCellValue(vo.getScore().doubleValue());
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

	/**
	 * 返回单元格，只带样式
	 * 
	 * @param row
	 * @param style
	 * @param colIndex
	 * @return
	 */
	private Cell getCell(Row row, CellStyle style, int colIndex) {
		Cell cell = row.createCell(colIndex);
		cell.setCellStyle(style);
		return cell;
	}
//	@RequestMapping(value="edit",method = RequestMethod.POST)
//	@ResponseBody
//	public String edit(Grade sy, Model model){
//		service.update(sy);
//		return "success";
//	}
//	@RequestMapping(value="delete",method = RequestMethod.POST)
//	@ResponseBody
//	public String delete(@RequestParam(value="id") int id, Model model){
//		service.delete(id);
//		return "success";
//	}
//	@RequestMapping(value="get",method = RequestMethod.GET)
//	@ResponseBody
//	public Grade get(@RequestParam(value="id") int id, Model model){
//		Grade g=service.get(id);
////		g.setSchoolClasses(null);
//		return g;
//	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}
