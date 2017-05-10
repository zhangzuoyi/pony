package com.zzy.pony.controller;




import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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

import com.zzy.pony.config.Constants;
import com.zzy.pony.model.CommonDict;
import com.zzy.pony.service.DictService;




@Controller
@RequestMapping(value = "/commonDict")
public class CommonDictController {
	static Logger log=LoggerFactory.getLogger(CommonDictController.class);
	
	@Autowired
	private DictService dictService;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "commonDict/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<CommonDict> list(){		
		List<CommonDict> result = dictService.list();	
		return result;
	}
	
	@RequestMapping(value="listByDictType",method = RequestMethod.GET)
	@ResponseBody
	public List<CommonDict> listByDictType(@RequestParam(value="dictType",required=false) String dictType){		
		List<CommonDict> result = new ArrayList<CommonDict>();
		if (StringUtils.isBlank(dictType)) {
			result = dictService.list();
		}else {
			result = dictService.listByDictType(dictType);
		}
		return result;
	}
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody CommonDict commonDict){		
		String result = "0";
		if (dictService.isExist(commonDict)) {
			result  = "1";//存在code
		}else {
			dictService.add(commonDict);
		}
		
		return result;
	}
	
	@RequestMapping(value="update",method = RequestMethod.POST)
	@ResponseBody
	public String update(@RequestBody CommonDict commonDict){		
		String result = "0";
		if (dictService.isExist(commonDict)) {
			result  = "1";//存在code
		}else {
			dictService.update(commonDict);
		}
		
		return result;
		
		
	}
	
	@RequestMapping(value="delete",method = RequestMethod.GET)
	@ResponseBody
	public void delete(@RequestParam(value="dictId") int dictId){		
		dictService.delete(dictId);
	}
	
	@ResponseBody
	@RequestMapping(value="commonDictTypes",method = RequestMethod.GET)
	public Map<String, String> commonDictTypes(){
		
		return Constants.getDictTypes();
	}
	@ResponseBody
	@RequestMapping(value="propertyStatus",method = RequestMethod.GET)
	public List<CommonDict> propertyStatus(){
		
		return dictService.findPropertyStatus();
	}
	
	
	
	
	
	
}
