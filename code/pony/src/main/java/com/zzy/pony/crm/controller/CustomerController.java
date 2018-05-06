package com.zzy.pony.crm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.crm.model.Customer;
import com.zzy.pony.crm.service.CustomerService;
import com.zzy.pony.oa.vo.TaskVo;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.vo.ConditionVo;

@Controller
@RequestMapping(value = "/crm/customer")
public class CustomerController {
	@Autowired
	private CustomerService service;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		
		return "crm/customer/main";
	}
	/*@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<Customer> list(Model model) {
		return service.findAll();
	}*/
	@RequestMapping(value = "list",method = RequestMethod.POST)
    @ResponseBody
    public Page<Customer> list(@RequestBody ConditionVo cv ){
        return service.findPage(cv.getCurrentPage(), cv.getPageSize());
    }
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody Customer customer){		
		service.add(customer, ShiroUtil.getLoginName());
		
		return "success";
	}
	@RequestMapping(value="update",method = RequestMethod.POST)
	@ResponseBody
	public String update(@RequestBody Customer customer){		
		service.update(customer, ShiroUtil.getLoginName());
		
		return "success";
	}
	@RequestMapping(value="delete/{id}",method = RequestMethod.POST)
	@ResponseBody
	public String delete(@PathVariable("id") long id, Model model){
		service.delete(id);
		return "success";
	}
}
