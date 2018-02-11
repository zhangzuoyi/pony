package com.zzy.pony.crm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.crm.model.Customer;
import com.zzy.pony.crm.service.CustomerService;

@Controller
@RequestMapping(value = "/crm/customer")
public class CustomerController {
	@Autowired
	private CustomerService service;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		
		return "crm/customer/main";
	}
	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<Customer> list(Model model) {
		return service.findAll();
	}
}
