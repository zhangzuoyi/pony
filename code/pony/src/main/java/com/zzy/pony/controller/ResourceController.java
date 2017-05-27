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
import com.zzy.pony.config.Constants;
import com.zzy.pony.model.Resource;
import com.zzy.pony.service.ResourceService;




@Controller
@RequestMapping(value = "/resourceAdmin")
public class ResourceController {
	static Logger log=LoggerFactory.getLogger(ResourceController.class);
	
	@Autowired
	private ResourceService resourceService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "resource/main";
	}
	@RequestMapping(value="listTree",method = RequestMethod.GET)
	@ResponseBody
	public String listTree(){		
			
		StringBuilder result = new StringBuilder();
		List<Map<String, Object>> lists = new ArrayList<Map<String,Object>>();		
		List<Resource> parentResource = resourceService.findByResLevel(Constants.RESOURCE_PARENT_LEVEL);				
		for (Resource parent : parentResource) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("resId", parent.getResId());
			map.put("label", parent.getResName());
			map.put("resName", parent.getResName());
			map.put("presId", parent.getPresId());
			map.put("resKey", parent.getResKey());
			map.put("resLevel", parent.getResLevel());
			map.put("comments", parent.getComments());
			List<Map<String, Object>> list2 = new ArrayList<Map<String,Object>>();
			List<Resource> childResource = resourceService.findByPresId(parent.getResId());
			for (Resource child : childResource) {
					Map<String, Object> map2 = new HashMap<String, Object>();
					map2.put("resId", child.getResId());
					map2.put("label", child.getResName());
					map2.put("resName", child.getResName());
					map2.put("presId", child.getPresId());
					map2.put("resKey", child.getResKey());
					map2.put("resLevel", child.getResLevel());
					map2.put("comments", child.getComments());
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
	
	@ResponseBody
	@RequestMapping(value="parentResource",method = RequestMethod.GET)
	public List<Resource> parentResource(Model model){
		List<Resource> result =  resourceService.findByResLevel(Constants.RESOURCE_PARENT_LEVEL);
		for (Resource resource : result) {
			resource.setRoles(null);
		}
		return result;
		
	}
	@ResponseBody
	@RequestMapping(value="update",method = RequestMethod.POST)
	public void update(@RequestBody Resource resource){
		
		resourceService.update(resource);
		
	}
	@ResponseBody
	@RequestMapping(value="delete",method = RequestMethod.GET)
	public void delete(@RequestParam(value = "resId") int resId){
		resourceService.delete(resId);
	}
	@ResponseBody
	@RequestMapping(value="add",method = RequestMethod.POST)
	public void add(@RequestBody Resource resource){
		resourceService.add(resource);
	}
	
	
	
	
	
	
	
	
	
}
