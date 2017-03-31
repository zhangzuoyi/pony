package com.zzy.pony.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


























import com.zzy.pony.dao.WeekdayDao;
import com.zzy.pony.model.Weekday;
import com.zzy.pony.service.WeekdayService;
import com.zzy.pony.config.Constants;

@Controller
@RequestMapping(value = "/weekLessonAdmin")
public class WeekLessonAdminController {
	@Autowired
	private WeekdayDao weekdayDao;
	@Autowired
	private WeekdayService weekdayService;
	
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){

	
		return "weekLessonAdmin/main";
	}

	@RequestMapping(value="list",method = RequestMethod.GET)
	@ResponseBody
	public List<Weekday> list( ){
		List<Weekday> resultList =weekdayDao.findAll();					
		return resultList;
	}
	
	@RequestMapping(value="listHaveClass",method = RequestMethod.GET)
	@ResponseBody
	public List<Weekday> findByhaveClass( ){
		List<Weekday> resultList = weekdayService.findByhaveClass(Constants.HAVECLASS_FLAG_TRUE);					
		return resultList;
	}
	
	@RequestMapping(value="update",method = RequestMethod.GET)
	@ResponseBody
	public void update(Integer id ){
		
		Weekday oldWeekday = weekdayDao.findOne(id);
		if (Constants.HAVECLASS_FLAG_FALSE ==oldWeekday.getHaveClass()) {
			oldWeekday.setHaveClass(Constants.HAVECLASS_FLAG_TRUE);
			weekdayDao.save(oldWeekday);
		}else if (Constants.HAVECLASS_FLAG_TRUE ==oldWeekday.getHaveClass()) {
			oldWeekday.setHaveClass(Constants.HAVECLASS_FLAG_FALSE);
			weekdayDao.save(oldWeekday);
		}		
	}
	
	
	
	
	
}
