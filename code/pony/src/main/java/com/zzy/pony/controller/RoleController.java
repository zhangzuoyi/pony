package com.zzy.pony.controller;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzy.pony.model.Resource;
import com.zzy.pony.model.Role;
import com.zzy.pony.service.ResourceService;
import com.zzy.pony.service.RoleService;
import com.zzy.pony.vo.RoleVo;




@Controller
@RequestMapping(value = "/roleAdmin")
public class RoleController {
	static Logger log=LoggerFactory.getLogger(RoleController.class);
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private ResourceService resourceService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "role/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<RoleVo> list(){		
		
		return   roleService.list();
		
	}
	@RequestMapping(value="listTree",method = RequestMethod.GET)
	@ResponseBody
	public String listTree(){		
			
		StringBuilder result = new StringBuilder();
		List<Map<String, Object>> lists = new ArrayList<Map<String,Object>>();		
		List<Role> roles = roleService.findAll();
		for (Role role : roles) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("roleCode", role.getRoleCode());
			map.put("label", role.getRoleName());						
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
	
	
	
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody RoleVo roleVo){		
		String result = "0";
		if (roleService.isExist(roleVo.getRoleCode())) {
			result = "1";			
		}else {
			Role role = new Role();
			role.setRoleName(roleVo.getRoleName());
			role.setRoleCode(roleVo.getRoleCode());
			List<Resource> resources = new ArrayList<Resource>();
			if (roleVo.getResources()!=null && roleVo.getResources().length>0) {
				for (Integer resId : roleVo.getResources()) {
					Resource resource = resourceService.get(resId); 
					resources.add(resource);
				} 
				role.setResources(resources);	
			}
			roleService.add(role);
		}
		
		return result;
		
	}
	
	@RequestMapping(value="update",method = RequestMethod.POST)
	@ResponseBody
	public void update(@RequestBody RoleVo roleVo){		
			
		Role role = roleService.get(roleVo.getRoleCode());
		role.setRoleName(roleVo.getRoleName());
		List<Resource> resources = new ArrayList<Resource>();
		if (roleVo.getResources()!=null && roleVo.getResources().length>0) {
			for (Integer resId : roleVo.getResources()) {
				Resource resource = resourceService.get(resId); 
				resources.add(resource);
			}  	
		}
		role.setResources(resources);			
		roleService.update(role);
		
	}
	
	@RequestMapping(value="delete",method = RequestMethod.GET)
	@ResponseBody
	public void delete(@RequestParam(value="roleCode") String roleCode){		
		roleService.delete(roleCode);
	}
	
	
	
	
	
	
	
	
	
}
