package com.zzy.pony.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.zzy.pony.exam.service.ExamineeService;
import com.zzy.pony.exam.vo.ExamineeVo;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.Student;
import com.zzy.pony.model.Subject;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.ExamResultService;
import com.zzy.pony.service.ExamService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.StudentService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.util.DateTimeUtil;
import com.zzy.pony.util.ExcelUtil;
import com.zzy.pony.vo.ExamResultVo;
import com.zzy.pony.vo.ExamVo;

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
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private TermService termService;
	@Autowired
	private ExamineeService examineeService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
//		model.addAttribute("subjects", subjectService.findAll());
		model.addAttribute("year",yearService.getCurrent());
		model.addAttribute("term",termService.getCurrent());
		return "examResult/main";
	}
	@RequestMapping(value="admin",method = RequestMethod.GET)
	public String admin(Integer examId, Model model){
		ExamVo vo=examService.getVo(examId);
		model.addAttribute("vo",vo);
		return "examResult/admin";
	}
	@RequestMapping(value="findByClass",method = RequestMethod.GET)
	@ResponseBody
	public List<ExamResultVo> findByClass(Integer examId, Integer classId, Integer subjectId, Model model){
		return service.findByClass(examId, classId, subjectId);
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
	public String upload(@RequestParam(value="examId") Integer examId, @RequestParam(value="classId") Integer classId, 
			@RequestParam(value="subjectId") Integer subjectId, @RequestParam(value="file") MultipartFile file, Model model){
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
			service.batchSave(studentScores, examId,subjectId, ShiroUtil.getLoginUser().getLoginName());
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "success";
	}
	/**
	 * 导入列分别为考生号，班级，姓名，各科目成绩，总分
	 * @param examId
	 * @param file
	 * @param model
	 * @return
	 */
	@RequestMapping(value="uploadAll",method = RequestMethod.POST)
	@ResponseBody
	public String uploadAll(@RequestParam(value="examId") final Integer examId, @RequestParam(value="file") MultipartFile file, Model model){
		try {
			Workbook wb=WorkbookFactory.create(file.getInputStream());
			Sheet sheet=wb.getSheetAt(0);
			//从第一行取得科目
			List<Integer> subjectIndexes=new ArrayList<Integer>();
			List<Subject> subjects=new ArrayList<Subject>();
			Row titleRow=sheet.getRow(0);
			int k=3;
			while(true) {
				Cell cell=titleRow.getCell(k);
				if(cell == null || StringUtils.isBlank(cell.getStringCellValue())) {
					break;
				}
				Subject subject=subjectService.findByName(cell.getStringCellValue());
				if(subject != null) {
					subjectIndexes.add(k);
					subjects.add(subject);
				}
				k++;
			}
			int subjectSize=subjects.size();
			//取得考试所有考生
			List<ExamineeVo> examinees=examineeService.findByExam(examId);
			//遍历内容，生成ExamResultVo列表
			final List<ExamResultVo> resultList=new ArrayList<ExamResultVo>();
			int i=1;
			while(true){
				Row row=sheet.getRow(i);
				if(row == null){
					break;
				}
				Cell cell=row.getCell(0);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				String regNo=cell.getStringCellValue();
				System.out.println(regNo);
				if(StringUtils.isBlank(regNo)){
					break;
				}
				ExamineeVo examinee=getExamineeVo(examinees, regNo);
				if(examinee != null) {
					for(int j=0;j<subjectSize;j++) {
						ExamResultVo vo=getExamResultVo(examinee,row.getCell(subjectIndexes.get(j)),subjects.get(j),examId);
						if(vo != null) {
							resultList.add(vo);
						}
					}
				}
				i++;
			}
			//保存ExamResultVo列表
			Thread t=new Thread(new Runnable() {//用线程防止前台超时重复提交

				@Override
				public void run() {
					service.uploadAll(examId, resultList, ShiroUtil.getLoginName());
				}
				
			});
			t.start();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "success";
	}
	private ExamResultVo getExamResultVo(ExamineeVo examinee, Cell cell, Subject subject, Integer examId) {
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		String score=cell.getStringCellValue();
		if(StringUtils.isNotBlank(score)) {
			ExamResultVo vo=new ExamResultVo();
			vo.setExamId(examId);
			vo.setScore(Float.valueOf(score));
			vo.setStudentId(examinee.getStudentId());
			vo.setSubjectId(subject.getSubjectId());
			return vo;
		}
		return null;
	}
	private ExamineeVo getExamineeVo(List<ExamineeVo> examinees, String regNo) {
		for(ExamineeVo vo: examinees) {
			if(regNo.equals(vo.getRegNo())) {
				return vo;
			}
		}
		return null;
	}
	
	@RequestMapping(value = "export", method = RequestMethod.GET)
	public ResponseEntity<byte[]> export(Integer examId, Integer classId,Integer subjectId,Model model) {
		List<ExamResultVo> vos=service.findByClass(examId, classId, subjectId);
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
	
	@RequestMapping(value = "exportTemplate", method = RequestMethod.GET)
	public ResponseEntity<byte[]> exportTemplate(Integer examId, Integer classId,Integer subjectId,Model model) {
		List<Student> students=studentService.findBySchoolClass(classId);
		Subject subject=subjectService.get(subjectId);
		SchoolClass sc=classService.get(classId);
		String reportName = sc.getName()+"成绩导入模板";
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentDispositionFormData("attachment", new String(reportName.getBytes("utf-8"), "ISO8859-1")
					+ DateTimeUtil.dateToStr(new Date()) + ".xls");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(templateExcelContent(students, reportName, subject.getName()), headers, HttpStatus.CREATED);
	}
	
	private byte[] templateExcelContent(List<Student> students, String reportName, String subjectName) {
		Workbook wb = new HSSFWorkbook();
		String sheetName = reportName;
		Sheet sheet = wb.createSheet(sheetName);

		CellStyle style = ExcelUtil.getCommonStyle(wb);

		CellStyle styleTitle = ExcelUtil.getTitleStyle(wb, style);

		// 设置标题
		Row titleRow = sheet.createRow(0);
		getCell(titleRow, "学号", styleTitle, 0);
		getCell(titleRow, "姓名", styleTitle, 1);
		getCell(titleRow, subjectName+"成绩", styleTitle, 2);

		// 设置内容
		int voLen = students.size();
		for (int i = 0; i < voLen; i++) {
			Student vo = students.get(i);
			Row row = sheet.createRow(1 + i);
			int cellIndex = 0;
			// 学号
			getCell(row, vo.getStudentNo(), style, cellIndex++);

			// 姓名
			getCell(row, vo.getName(), style, cellIndex++);

			// 成绩，默认为空
			Cell cell=getCell(row, style, cellIndex++);

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
