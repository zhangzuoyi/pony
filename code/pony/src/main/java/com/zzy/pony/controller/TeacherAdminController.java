package com.zzy.pony.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

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

import com.zzy.pony.config.Constants;
import com.zzy.pony.model.CommonDict;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.DictService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.service.TeacherService;
import com.zzy.pony.util.DateTimeUtil;
import com.zzy.pony.util.ExcelUtil;
import com.zzy.pony.util.TemplateUtil;
import com.zzy.pony.vo.TeacherVo;

@Controller
@RequestMapping(value = "/teacherAdmin")
public class TeacherAdminController {
	@Autowired
	private TeacherService service;
	@Autowired
	private DictService dictService;
	@Autowired
	private SubjectService subjectService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		model.addAttribute("degrees", dictService.findEducationDegrees());
		model.addAttribute("sexes", dictService.findSexes());
		model.addAttribute("credentials", dictService.findCredentials());
		model.addAttribute("subjects", subjectService.findAll());
		return "teacherAdmin/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<Teacher> list(Model model){
		List<Teacher> list=service.findAll();

		return list;
	}
	
	//将同一教师按照科目进行合并
	@RequestMapping(value="listAllVo",method = RequestMethod.GET)
	@ResponseBody
	public List<TeacherVo> listAllVo(Model model){
		List<TeacherVo> result = new ArrayList<TeacherVo>();
		List<Teacher> teachers = service.findAll();
		Map<String, List<Teacher>> map = new LinkedHashMap<String, List<Teacher>>();
		for (Teacher teacher : teachers) {			
			if (map.containsKey(teacher.getTeacherNo())) {
				map.get(teacher.getTeacherNo()).add(teacher);								
			}else{
				List<Teacher> innerList = new ArrayList<Teacher>();
				innerList.add(teacher);
				map.put(teacher.getTeacherNo(), innerList);
			}							
		}
		for (String teacherNo : map.keySet()) {
			TeacherVo vo = new TeacherVo();
			List<Teacher> teacherList = map.get(teacherNo);
			vo.setTeacherId(teacherList.get(0).getTeacherId());
			vo.setTeacherNo(teacherList.get(0).getTeacherNo());
			vo.setName(teacherList.get(0).getName());
			vo.setSubjectName(teacherList.get(0).getSubject().getName());
			if (teacherList.size()>1){
				for (Teacher teacher:
					 teacherList) {
					if (!teacher.getSubject().getName().equalsIgnoreCase(vo.getSubjectName())){
						vo.setSubjectName(vo.getSubjectName()+","+teacher.getSubject().getName());
					}
				}
			}
			
			result.add(vo);
			
		}
		
		
		
		
		

		return result;
	}
	@RequestMapping(value="findSubjectByTeacher",method = RequestMethod.GET)
	@ResponseBody
	public Subject findSubjectByTeacher(@RequestParam(value="teacherId") int teacherId, Model model){
		Teacher g=service.get(teacherId);
		return g.getSubject();
	}
	
	
	
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(Teacher sy, Model model){
		sy.setCreateTime(new Date());
		sy.setCreateUser(ShiroUtil.getLoginUser().getLoginName());
		sy.setUpdateTime(new Date());
		sy.setUpdateUser(ShiroUtil.getLoginUser().getLoginName());
		service.add(sy);
		return "success";
	}
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	public String edit(Teacher sy, Model model){
		sy.setUpdateTime(new Date());
		sy.setUpdateUser("test");
		service.update(sy);
		return "success";
	}
	@RequestMapping(value="delete",method = RequestMethod.POST)
	@ResponseBody
	public String delete(@RequestParam(value="id") int id, Model model){
		service.delete(id);
		return "success";
	}
	@RequestMapping(value="get",method = RequestMethod.GET)
	@ResponseBody
	public Teacher get(@RequestParam(value="id") int id, Model model){
		Teacher g=service.get(id);
		return g;
	}
	@RequestMapping(value="upload",method = RequestMethod.POST)
	@ResponseBody
	public String upload(@RequestParam(value="file") MultipartFile file, Model model){
		List<Teacher> list=new ArrayList<Teacher>();
		List<Subject> subjects=subjectService.findAll();
		List<CommonDict> degrees=dictService.findEducationDegrees();
		List<CommonDict> sexList=dictService.findSexes();
		Map<String,String> sexMap=new HashMap<String,String>();
		for(CommonDict cd: sexList){
			sexMap.put(cd.getValue(), cd.getCode());//从名称到编码
		}
		try {
			Workbook wb=WorkbookFactory.create(file.getInputStream());
			Sheet sheet=wb.getSheetAt(0);
			int i=1;
			while(true){
				Teacher stu=new Teacher();
				Row row=sheet.getRow(i);
				if(row == null || row.getCell(0) == null){
					break;
				}
				Cell cell=row.getCell(0);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				String teacherNo=cell.getStringCellValue();
				if(StringUtils.isBlank(teacherNo)){
					break;
				}
				String name=cellValue(row.getCell(1));
				String sexValue=cellValue(row.getCell(2));
				String birthday=null;
				if(row.getCell(3) != null && row.getCell(3).getDateCellValue() != null){
					birthday=DateTimeUtil.dateToStr(row.getCell(3).getDateCellValue());
				}
				String nativePlace=cellValue(row.getCell(4));
				String nation=cellValue(row.getCell(5));
				String idCard=cellValue(row.getCell(6));
				String nativeAddr=cellValue(row.getCell(7));
				String homeAddr=cellValue(row.getCell(8));
				String zipcode=cellValue(row.getCell(9));
				String phone=cellValue(row.getCell(10));
				String email=cellValue(row.getCell(11));
				Date graduateDate=row.getCell(12)==null ? null : row.getCell(12).getDateCellValue();
				String graduateSchool=cellValue(row.getCell(13));
				String degree=cellValue(row.getCell(14));
				String major=cellValue(row.getCell(15));
				String title=cellValue(row.getCell(16));
				String subjectName=cellValue(row.getCell(17));
				
				stu.setBirthday(birthday);
				stu.setEmail(email);
				stu.setGraduateDate(graduateDate);
				stu.setGraduateSchool(graduateSchool);
				stu.setHomeAddr(homeAddr);
				stu.setHomeZipcode(zipcode);
				stu.setIdNo(idCard);
				stu.setIdType(Constants.ID_TYPE_DEFAULT);
				stu.setName(name);
				stu.setNation(nation);
				stu.setNativeAddr(nativeAddr);
				stu.setNativePlace(nativePlace);
				stu.setPhone(phone);
				stu.setSex(sexMap.get(sexValue));
				stu.setTeacherNo(teacherNo);
				stu.setMajor(major);
				stu.setTitle(title);
				stu.setSubject(getSubject(subjectName, subjects));
				stu.setDegree(getDegreeCode(degree, degrees));
				
				list.add(stu);
				i++;
			}
			service.upload(list, ShiroUtil.getLoginUser().getLoginName());
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "success";
	}
	@RequestMapping(value="exportTemplate",method = RequestMethod.GET)
	public ResponseEntity<byte[]> exportTemplate(Model model){
		String fileName="老师导入模板.xlsx";
		HttpHeaders headers = new HttpHeaders(); 
		try {
			headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("utf-8"), "ISO8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(TemplateUtil.getContent(fileName), headers, HttpStatus.CREATED);
	}
	@RequestMapping(value = "exportTeachers", method = RequestMethod.GET)
	public ResponseEntity<byte[]> exportTeachers(Model model) {
		String reportName = "老师列表";
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentDispositionFormData("attachment", new String(reportName.getBytes("utf-8"), "ISO8859-1")
					+ DateTimeUtil.dateToStr(new Date()) + ".xls");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(excelContent(reportName), headers, HttpStatus.CREATED);
	}
	private byte[] excelContent(String reportName) {
		List<Teacher> list=service.findAll();
		List<CommonDict> dicts=dictService.findSexes();
		Map<String, String> sexMap=new HashMap<String, String>();
		for(CommonDict cd : dicts) {
			sexMap.put(cd.getCode(), cd.getValue());
		}
		Workbook wb = new HSSFWorkbook();
		String sheetName = reportName;
		Sheet sheet = wb.createSheet(sheetName);

		CellStyle style = ExcelUtil.getCommonStyle(wb);

		CellStyle styleTitle = ExcelUtil.getTitleStyle(wb, style);

		// 设置标题
		Row titleRow = sheet.createRow(0);
		String[] titles=new String[] {"编号","姓名","性别","生日","联系电话","毕业日期","专业","职称","任教科目"};
		for(int i=0;i<titles.length;i++) {
			getCell(titleRow,titles[i],styleTitle,i);
		}
		int rowIndex=1;
		for(Teacher teacher: list) {
			int colIndex=0;
			Row row = sheet.createRow(rowIndex++);
			getCell(row,teacher.getTeacherNo(),style,colIndex++);
			getCell(row,teacher.getName(),style,colIndex++);
			getCell(row,sexMap.get(teacher.getSex()),style,colIndex++);
			getCell(row,teacher.getBirthday(),style,colIndex++);
			getCell(row,teacher.getPhone(),style,colIndex++);
			getCell(row,teacher.getGraduateDate() == null ? "" : DateTimeUtil.dateToStr(teacher.getGraduateDate()),style,colIndex++);
			getCell(row,teacher.getMajor(),style,colIndex++);
			getCell(row,teacher.getTitle(),style,colIndex++);
			getCell(row,teacher.getSubject().getName(),style,colIndex++);
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			wb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
	private Cell getCell(Row row, String value, CellStyle style, int colIndex) {
		Cell cell = row.createCell(colIndex);
		cell.setCellStyle(style);
		cell.setCellValue(value);
		return cell;
	}
	private Subject getSubject(String subjectName, List<Subject> subjects){
		if(StringUtils.isBlank(subjectName)){
			return null;
		}
		for(Subject subject: subjects){
			if(subjectName.equals(subject.getName())){
				return subject;
			}
		}
		return null;
	}
	private String getDegreeCode(String degree, List<CommonDict> degrees){
		for(CommonDict cd: degrees){
			if(cd.getValue().equals(degree)){
				return cd.getCode();
			}
		}
		return null;
	}
	private String cellValue(Cell cell){
		if(cell != null){
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			return cell.getStringCellValue();
		}
		return null;
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
