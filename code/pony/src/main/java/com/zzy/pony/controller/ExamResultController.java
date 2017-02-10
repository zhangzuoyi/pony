package com.zzy.pony.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
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

import com.zzy.pony.model.Student;
import com.zzy.pony.service.ExamResultService;
import com.zzy.pony.service.StudentService;
import com.zzy.pony.service.SubjectService;
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
