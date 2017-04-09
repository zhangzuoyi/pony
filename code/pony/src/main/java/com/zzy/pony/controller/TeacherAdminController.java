package com.zzy.pony.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
		List<String> list=service.findAllTeacherNo();
		for (String teacherNo : list) {
			TeacherVo vo = new TeacherVo();
			List<Teacher> teachers=   service.findTeachersByTeacherNo(teacherNo);	
				for (Teacher teacher2 : teachers) {											
					if (vo.getSubjectName()==null ||"".equalsIgnoreCase(vo.getSubjectName())) {
						vo.setTeacherId(teacher2.getTeacherId());
						vo.setName(teacher2.getName());
						vo.setTeacherNo(teacher2.getTeacherNo());
						vo.setSubjectName(teacher2.getSubject().getName());
					}else {
						vo.setSubjectName(vo.getSubjectName()+","+teacher2.getSubject().getName());
					}									
				}
			 	
			
			result.add(vo);
			
			
		}

		return result;
	}
	
	
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(Teacher sy, Model model){
		sy.setCreateTime(new Date());
		sy.setCreateUser("test");
		sy.setUpdateTime(new Date());
		sy.setUpdateUser("test");
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
				String name=row.getCell(1).getStringCellValue();
				row.getCell(2).setCellType(HSSFCell.CELL_TYPE_STRING);
				String sexValue=row.getCell(2).getStringCellValue();
				Date birthdayDate=row.getCell(3).getDateCellValue();
				String birthday=null;
				if(birthdayDate != null){
					birthday=DateTimeUtil.dateToStr(birthdayDate);
				}
				String nativePlace=row.getCell(4).getStringCellValue();
				String nation=row.getCell(5).getStringCellValue();
				row.getCell(6).setCellType(HSSFCell.CELL_TYPE_STRING);
				String idCard=cellValue(row.getCell(6));
				String nativeAddr=cellValue(row.getCell(7));
				String homeAddr=cellValue(row.getCell(8));
				String zipcode=cellValue(row.getCell(9));
				String phone=cellValue(row.getCell(10));
				String email=cellValue(row.getCell(11));
				Date graduateDate=row.getCell(12).getDateCellValue();
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
