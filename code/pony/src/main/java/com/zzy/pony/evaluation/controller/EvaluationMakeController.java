package com.zzy.pony.evaluation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.evaluation.dao.EvaluationSubjectDao;
import com.zzy.pony.evaluation.model.EvaluationSubject;
import com.zzy.pony.evaluation.service.EvaluationService;
import com.zzy.pony.evaluation.vo.EvaluationRecordVo;
import com.zzy.pony.evaluation.vo.EvaluationRowVo;
import com.zzy.pony.security.ShiroUtil;

@Controller
@RequestMapping(value = "/evaluation/make")
public class EvaluationMakeController {
	@Autowired
	private EvaluationSubjectDao subjectDao;
	@Autowired
	private EvaluationService service;
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String userMain(Long subjectId, Model model){
		EvaluationSubject subject=subjectDao.findOne(subjectId);
		model.addAttribute("subject", subject);
		return "evaluation/make/main";
	}
	@RequestMapping(value="itemTableData",method = RequestMethod.GET)
	@ResponseBody
	public List<EvaluationRowVo> itemTableData(Long subjectId){
		return service.itemTableData(subjectId);
	}
	@RequestMapping(value="recordData",method = RequestMethod.GET)
	@ResponseBody
	public EvaluationRecordVo recordData(Long subjectId) {
		return service.findBySubjectAndTeacher(subjectId, ShiroUtil.getLoginUser().getTeacherId());
	}
	@ResponseBody
	@RequestMapping(value="submitRecord",method = RequestMethod.POST)
	public void submitRecord(@RequestBody EvaluationRecordVo record){
		service.addRecord(record, ShiroUtil.getLoginUser().getTeacherId(), ShiroUtil.getLoginName());
	}
	@ResponseBody
	@RequestMapping(value="updateRecord",method = RequestMethod.POST)
	public void updateRecord(@RequestBody EvaluationRecordVo record){
		service.updateRecord(record, ShiroUtil.getLoginName());
	}
	@RequestMapping(value="checkMain",method = RequestMethod.GET)
	public String checkMain(Long subjectId, Model model){
		EvaluationSubject subject=subjectDao.findOne(subjectId);
		model.addAttribute("subject", subject);
		return "evaluation/make/checkMain";
	}
	@RequestMapping(value="records",method = RequestMethod.GET)
	@ResponseBody
	public List<EvaluationRecordVo> records(Long subjectId) {
		return service.findRecords(subjectId);
	}
	@RequestMapping(value="getRecord",method = RequestMethod.GET)
	@ResponseBody
	public EvaluationRecordVo getRecord(Long recordId) {
		return service.findRecordById(recordId);
	}
	@ResponseBody
	@RequestMapping(value="checkRecord",method = RequestMethod.POST)
	public void checkRecord(@RequestBody EvaluationRecordVo record){
		service.checkRecord(record, ShiroUtil.getLoginName());
	}
}
