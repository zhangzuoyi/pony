package com.zzy.pony.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.zzy.pony.model.Exam;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.Student;
import com.zzy.pony.service.ExamResultService;
import com.zzy.pony.service.ExamService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.StudentService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.util.DateTimeUtil;
import com.zzy.pony.vo.ExamResultVo;

@Controller
@RequestMapping(value = "/examResult")
public class ExamResultController {
	@Autowired
	private ExamResultService service;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private ExamService examService;
	@Autowired
	private SchoolClassService classService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		model.addAttribute("subjects", subjectService.findAll());
		return "examResult/main";
	}
	@RequestMapping(value="findByClass",method = RequestMethod.GET)
	@ResponseBody
	public List<ExamResultVo> findByClass(Integer examId, Integer classId, Model model){
		return service.findByClass(examId, classId);
	}
	@RequestMapping(value="save",method = RequestMethod.POST)
	@ResponseBody
	public String save(@RequestBody List<ExamResultVo> vos, Model model){
		for(ExamResultVo vo: vos){
			System.out.println(vo.getExamName()+":"+vo.getStudentName());
		}
		service.save(vos);
		return "success";
	}
	@RequestMapping(value="upload",method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestParam(value="examId") Integer examId, @RequestParam(value="classId") Integer classId, 
			@RequestParam(value="file") MultipartFile file, Model model){
		System.out.println(examId);
		System.out.println(classId);
		List<Student> students=studentService.findBySchoolClass(classId);
		Map<Student, Float> studentScores=new HashMap<Student, Float>();
		try {
			Workbook wb=WorkbookFactory.create(file.getInputStream());
			Sheet sheet=wb.getSheetAt(0);
			int i=1;
			while(true){
				Row row=sheet.getRow(i);
				if(row == null){
					break;
				}
				Cell cell=row.getCell(0);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				String studentNo=cell.getStringCellValue();
				System.out.println(studentNo);
				if(StringUtils.isBlank(studentNo)){
					break;
				}
				double score=row.getCell(2).getNumericCellValue();
				for(Student student:students){
					if(student.getStudentNo().equals(studentNo)){
						studentScores.put(student, (float)score);
						break;
					}
				}
				i++;
			}
			service.batchSave(studentScores, examId, "test");
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	@RequestMapping(value = "export", method = RequestMethod.GET)
	public ResponseEntity<byte[]> export(Integer examId, Integer classId,Model model) {
		List<ExamResultVo> vos=service.findByClass(examId, classId);
		Exam exam=examService.get(examId);
		SchoolClass sc=classService.get(classId);
		String reportName = sc.getName()+exam.getName()+"成绩";
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentDispositionFormData("attachment", new String(reportName.getBytes("utf-8"), "ISO8859-1")
					+ DateTimeUtil.dateToStr(new Date()) + ".xls");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(excelContent(vos, reportName), headers, HttpStatus.CREATED);
	}

	private byte[] excelContent(List<ExamResultVo> vos, String reportName) {
		Workbook wb = new HSSFWorkbook();
		String sheetName = reportName;
		Sheet sheet = wb.createSheet(sheetName);

		CellStyle style = wb.createCellStyle();
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());

		CellStyle styleTitle = wb.createCellStyle();
		styleTitle.cloneStyleFrom(style);
		styleTitle.setAlignment(CellStyle.ALIGN_CENTER);
		styleTitle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		styleTitle.setFillPattern(CellStyle.SOLID_FOREGROUND);

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
