package com.zzy.pony.property.controller;




import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.property.model.Department;
import com.zzy.pony.property.service.DepartmentService;




@Controller
@RequestMapping(value = "/property/department")
public class DepartmentController {
	static Logger log=LoggerFactory.getLogger(DepartmentController.class);
	
	@Autowired
	private DepartmentService departmentService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "property/department/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<Department> list(){		
		List<Department> result = departmentService.list();	
		return result;
	}
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody Department department){		
		String result = "0";
		if (departmentService.isExist(department)) {
			result  = "1";//存在category
		}else {
			departmentService.add(department);
		}
		
		return result;
	}
	
	@RequestMapping(value="update",method = RequestMethod.POST)
	@ResponseBody
	public String update(@RequestBody Department department){		
		String result = "0";
		if (departmentService.isExist(department)) {
			result  = "1";//存在category
		}else {
			departmentService.update(department);
		}
		
		return result;
		
		
	}
	
	@RequestMapping(value="delete",method = RequestMethod.GET)
	@ResponseBody
	public void delete(@RequestParam(value="deptId") int deptId){		
		departmentService.delete(deptId);
	}
	
	
	
	
}
