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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzy.pony.config.Constants;
import com.zzy.pony.model.CommonDict;
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Student;
import com.zzy.pony.model.StudentStatusChange;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.DictService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.StudentService;
import com.zzy.pony.util.DateTimeUtil;

@Controller
@RequestMapping(value = "/studentAdmin")
public class StudentAdminController {
	@Autowired
	private StudentService service;
	@Autowired
	private SchoolClassService classService;
	@Autowired
	private DictService dictService;

	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		List<SchoolClass> list=classService.findCurrent();
		model.addAttribute("classes", list);
		model.addAttribute("sexes", dictService.findSexes());
		model.addAttribute("credentials", dictService.findCredentials());
		model.addAttribute("studentTypes", dictService.findStudentTypes());
		return "studentAdmin/main";
	}
	@RequestMapping(value="entrance",method = RequestMethod.GET)
	public String entrance(Model model){
		List<SchoolClass> list=classService.findCurrent();
		model.addAttribute("classes", list);
		model.addAttribute("sexes", dictService.findSexes());
		model.addAttribute("credentials", dictService.findCredentials());
		model.addAttribute("studentTypes", dictService.findStudentTypes());
		return "studentAdmin/entrance";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<Student> list(Model model){
		List<Student> list=service.findAll();
		for(Student g: list){
			g.setSchoolClasses(null);
		}
		return list;
	}
	
	//获取班级学生树结构数据
		@RequestMapping(value="listTree",method = RequestMethod.GET)
		@ResponseBody
		public String listTree(Model model){
			StringBuilder result = new StringBuilder();
			List<Map<String, Object>> lists = new ArrayList<Map<String,Object>>();
			List<SchoolClass> schoolClasses = classService.findCurrent();
			for (SchoolClass schoolClass : schoolClasses) {
				Map<String, Object> map = new HashMap<String, Object>();
				//map.put("id", grade.getGradeId());
				map.put("label", schoolClass.getName());
				List<Map<String, Object>> list2 = new ArrayList<Map<String,Object>>();
				List<Student> students = service.findBySchoolClass(schoolClass.getClassId());
					for (Student student : students) {
						Map<String, Object> map2 = new HashMap<String, Object>();
						map2.put("id", student.getStudentId());
						map2.put("label", student.getName());
						list2.add(map2);
					}
				map.put("children", list2);
				lists.add(map);
				
			}
			GsonBuilder gb = new GsonBuilder();
			Gson gson = gb.create();
			String treeDatas= gson.toJson(lists);	
			result.append("{\"treeData\"");
			result.append(":");
			result.append(treeDatas);
			result.append("}");
			return result.toString();
			
			
			
			
		}
	
	@RequestMapping(value="findByClass",method = RequestMethod.GET)
	@ResponseBody
	public List<Student> findByClass(@RequestParam(value="classId") int classId, Model model){
		List<Student> list=service.findBySchoolClass(classId);
		for(Student g: list){
			g.setSchoolClasses(null);
		}
		return list;
	}
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(Student sy, Model model){
		sy.setCreateTime(new Date());
		sy.setCreateUser(ShiroUtil.getLoginUser().getLoginName());
		sy.setUpdateTime(new Date());
		sy.setUpdateUser(ShiroUtil.getLoginUser().getLoginName());
		sy.setStatus(StudentService.STUDENT_STATUS_ZD);//默认在读
		service.add(sy);
		return "success";
	}
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	public String edit(Student sy, Model model){
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
	@RequestMapping(value="upload",method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestParam(value="classId") Integer classId, @RequestParam(value="file") MultipartFile file, Model model){
		System.out.println(classId);
		List<Student> list=new ArrayList<Student>();
		SchoolClass schoolClass=classService.get(classId);
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
				Student stu=new Student();
				Row row=sheet.getRow(i);
				if(row == null){
					break;
				}
				Cell cell=row.getCell(0);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				String studentNo=cell.getStringCellValue();
				if(StringUtils.isBlank(studentNo)){
					break;
				}
				String name=row.getCell(1).getStringCellValue();
				row.getCell(2).setCellType(HSSFCell.CELL_TYPE_STRING);
				String sex=row.getCell(2).getStringCellValue();
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
				Date entryDate=row.getCell(12).getDateCellValue();
				
				stu.setBirthday(birthday);
				stu.setEmail(email);
				stu.setEntranceDate(entryDate);
				stu.setEntranceType(StudentService.STUDENT_TYPE_TZ);//默认统招
				stu.setHomeAddr(homeAddr);
				stu.setHomeZipcode(zipcode);
				stu.setIdNo(idCard);
				stu.setIdType(Constants.ID_TYPE_DEFAULT);//默认身份证
				stu.setName(name);
				stu.setNation(nation);
				stu.setNativeAddr(nativeAddr);
				stu.setNativePlace(nativePlace);
				stu.setPhone(phone);
				stu.setSchoolClass(schoolClass);
				stu.setSex(sexMap.get(sex));
				stu.setStudentNo(studentNo);
				stu.setStatus(StudentService.STUDENT_STATUS_ZD);
				
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
	private String cellValue(Cell cell){
		if(cell != null){
			return cell.getStringCellValue();
		}
		return null;
	}
	@RequestMapping(value="get",method = RequestMethod.GET)
	@ResponseBody
	public Student get(@RequestParam(value="id") int id, Model model){
		Student g=service.get(id);
		g.setSchoolClasses(null);
		return g;
	}
	
	@RequestMapping(value="changeStatus",method = RequestMethod.POST)
	@ResponseBody
	public String changeStatus(StudentStatusChange sc, Model model){
		sc.setCreateTime(new Date());
		sc.setCreateUser(ShiroUtil.getLoginUser().getLoginName());
		service.changeStatus(sc);
		return "success";
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
