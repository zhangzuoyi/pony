package com.zzy.pony.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

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
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.GradeService;
import com.zzy.pony.service.SchoolClassService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.TeacherService;
import com.zzy.pony.vo.SchoolClassVo;

@Controller
@RequestMapping(value = "/schoolClass")
public class SchoolClassController {
	@Autowired
	private SchoolClassService service;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private SchoolYearService yearService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		model.addAttribute("grades", gradeService.findAll());
		model.addAttribute("teachers",teacherService.findAll());
		model.addAttribute("schoolYear",yearService.getCurrent());
		return "schoolClass/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<SchoolClass> list(Model model){
		List<SchoolClass> list=service.findCurrent();
		for(SchoolClass sc: list){
//			sc.getGrade().setSchoolClasses(null);
		}
		return list;
	}
	
	@RequestMapping(value="listVo",method = RequestMethod.GET)
	@ResponseBody
	public List<SchoolClassVo> listVo(Model model){
		List<SchoolClassVo> result = new ArrayList<SchoolClassVo>();					
		List<SchoolClass> list=service.findCurrent();
		for(SchoolClass sc: list){
			SchoolClassVo vo = new SchoolClassVo();
			vo.setClassId(sc.getClassId());
			vo.setName(sc.getName());
			result.add(vo);		
		}
		return result;
	}
	
	//获取年级班级树结构数据
	@RequestMapping(value="listTree",method = RequestMethod.GET)
	@ResponseBody
	public String listTree(Model model){
		StringBuilder result = new StringBuilder();
		SchoolYear currentYear=   yearService.getCurrent();
		List<Map<String, Object>> lists = new ArrayList<Map<String,Object>>();
		List<Grade> grades =   gradeService.findAll();
		
		for (Grade grade : grades) {
			Map<String, Object> map = new HashMap<String, Object>();
			//map.put("id", grade.getGradeId());
			map.put("label", grade.getName());
			List<Map<String, Object>> list2 = new ArrayList<Map<String,Object>>();
			List<SchoolClass> schoolClasses = service.findByYearAndGrade(currentYear.getYearId(), grade.getGradeId());
				for (SchoolClass schoolClass : schoolClasses) {
					Map<String, Object> map2 = new HashMap<String, Object>();
					map2.put("id", schoolClass.getClassId());
					map2.put("label", schoolClass.getName());
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
	@RequestMapping(value="findByGrade",method = RequestMethod.GET)
	@ResponseBody
	public List<SchoolClassVo> findByGrade(@RequestParam(value="gradeId") int gradeId,Model model){
//		List<SchoolClass> schoolClasses =service.findByGrade(gradeId);
		SchoolYear year = yearService.getCurrent();
		List<SchoolClass> schoolClasses =service.findByYearAndGrade(year.getYearId(), gradeId);
		return toSimpleVo(schoolClasses);
	}
	
	
	@RequestMapping(value="findCurrentByGrade",method = RequestMethod.GET)
	@ResponseBody
	public List<SchoolClassVo> findCurrentByGrade(@RequestParam(value="gradeId") int gradeId,Model model){
		SchoolYear year = yearService.getCurrent();
		List<SchoolClass> schoolClasses =service.findByYearAndGrade(year.getYearId(), gradeId);
		return toSimpleVo(schoolClasses);
	}
	@RequestMapping(value="findByYearAndGrade",method = RequestMethod.GET)
	@ResponseBody
	public List<SchoolClassVo> findByYearAndGrade(@RequestParam(value="yearId") int yearId,@RequestParam(value="gradeId") int gradeId,Model model){
		List<SchoolClass> schoolClasses =service.findByYearAndGrade(yearId, gradeId);
		return toSimpleVo(schoolClasses);
	}
	private List<SchoolClassVo> toSimpleVo(List<SchoolClass> schoolClasses){
		List<SchoolClassVo> resultList = new ArrayList<SchoolClassVo>();
		for (SchoolClass schoolClass : schoolClasses) {
			SchoolClassVo vo = new SchoolClassVo();
			vo.setClassId(schoolClass.getClassId());
			vo.setName(schoolClass.getName());
			vo.setGradeId(schoolClass.getGrade().getGradeId());
			vo.setGradeName(schoolClass.getGrade().getName());
			resultList.add(vo);;
		}
		return resultList;
	}
	@RequestMapping(value="findByExam",method = RequestMethod.GET)
	@ResponseBody
	public List<SchoolClassVo> findByExam(@RequestParam(value="examId") int examId,Model model){
		List<SchoolClassVo> resultList = new ArrayList<SchoolClassVo>();
		List<SchoolClass> schoolClasses = service.findByExam(examId);
		
		for (SchoolClass schoolClass : schoolClasses) {
			SchoolClassVo vo = new SchoolClassVo();
			vo.setClassId(schoolClass.getClassId());
			vo.setName(schoolClass.getName());
			resultList.add(vo);
		}
		return resultList;
	}
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(SchoolClass sy, Model model){
		Date now=new Date();
		sy.setCreateTime(now);
		sy.setCreateUser(ShiroUtil.getLoginUser().getLoginName());
		sy.setUpdateTime(now);
		sy.setUpdateUser(ShiroUtil.getLoginUser().getLoginName());
		sy.setYearId(yearService.getCurrent().getYearId());
		service.add(sy);
		return "success";
	}
	@RequestMapping(value="edit",method = RequestMethod.POST)
	@ResponseBody
	public String edit(SchoolClass sy, Model model){
		Date now=new Date();
		sy.setUpdateTime(now);
		sy.setUpdateUser(ShiroUtil.getLoginUser().getLoginName());
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
	public SchoolClass get(@RequestParam(value="id") int id, Model model){
		SchoolClass sc=service.get(id);
//		sc.getGrade().setSchoolClasses(null);
		return sc;
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}
