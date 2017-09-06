package com.zzy.pony.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.model.Grade;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.security.ShiroUtil;
import com.zzy.pony.service.GradeService;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.UpgradeService;
import com.zzy.pony.vo.UpgradeVo;

@Controller
@RequestMapping(value = "/upgrade")
public class UpgradeController {
	@Autowired
	private SchoolYearService yearService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private UpgradeService service;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){
		SchoolYear currentYear=yearService.getCurrent();
		SchoolYear lastYear=yearService.findByStartYear(currentYear.getStartYear()-1);
		List<Grade> grades=gradeService.findAll();
		model.addAttribute("currentYear", currentYear);
		model.addAttribute("lastYear", lastYear);
		model.addAttribute("grades", grades);
		
		return "upgrade/main";
	}
	@ResponseBody
	@RequestMapping(value="preview",method = RequestMethod.GET)
	public List<UpgradeVo> preview(Integer[] gradeIds, Model model){
		return service.preview(gradeIds);
	}
	@ResponseBody
	@RequestMapping(value="upgrade",method = RequestMethod.POST)
	public String upgrade(@RequestBody UpgradeVo[] vos, Model model){
		for(UpgradeVo vo: vos){
			for(SchoolClass sc: vo.getTargetClasses()){
				System.out.println(sc.getSeq());
			}
		}
		service.upgrade(vos, ShiroUtil.getLoginUser().getLoginName());
		return "success";
	}
}
