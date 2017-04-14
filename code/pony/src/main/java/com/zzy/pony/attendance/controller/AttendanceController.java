package com.zzy.pony.attendance.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import com.zzy.pony.attendance.model.Attendance;
import com.zzy.pony.attendance.service.AttendanceService;
import com.zzy.pony.security.ShiroUtil;

@Controller
@RequestMapping(value = "/attendance/attendance")
public class AttendanceController {
	@Autowired
	private AttendanceService service;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		return "attendance/attendance/main";
	}
	@RequestMapping(value="my",method = RequestMethod.GET)
	public String my(Model model){
		return "attendance/attendance/my";
	}
	@RequestMapping(value="mylist",method = RequestMethod.GET)
	@ResponseBody
	public List<Attendance> mylist(Model model){
		return service.findByUser(ShiroUtil.getLoginUser().getId());
	}

	@RequestMapping(value="checkin",method = RequestMethod.POST)
	@ResponseBody
	public String checkIn(Model model){
		service.checkIn(ShiroUtil.getLoginUser().getId());
		return "success";
	}

	@RequestMapping(value="checkout",method = RequestMethod.POST)
	@ResponseBody
	public String checkOut(Model model){
		service.checkOut(ShiroUtil.getLoginUser().getId());
		return "success";
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
}
