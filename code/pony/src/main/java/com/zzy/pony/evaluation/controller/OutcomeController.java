package com.zzy.pony.evaluation.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.zzy.pony.evaluation.dao.OutcomeAttachDao;
import com.zzy.pony.evaluation.dao.OutcomeValueDao;
import com.zzy.pony.evaluation.model.Outcome;
import com.zzy.pony.evaluation.model.OutcomeAttach;
import com.zzy.pony.evaluation.service.OutcomeService;
import com.zzy.pony.evaluation.vo.OutcomeVo;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.security.ShiroUtil;

@Controller
@RequestMapping(value = "/evaluation/outcome")
public class OutcomeController {
	@Autowired
	private OutcomeService service;
	@Autowired
	private OutcomeAttachDao attachDao;
	
	@RequestMapping(value="userMain",method = RequestMethod.GET)
	public String userMain(Model model){
		return "evaluation/outcome/userMain";
	}
	@RequestMapping(value="mylist",method = RequestMethod.GET)
	@ResponseBody
	public List<OutcomeVo> mylist(){
		Integer teacherId=ShiroUtil.getLoginUser().getTeacherId();
		return service.findByTeacher(teacherId);
	}
	@RequestMapping(value="addOutcome",method = RequestMethod.POST)
	@ResponseBody
	public String addOutcome(@RequestBody Outcome outcome) {
		outcome.setCreateTime(new Date());
		outcome.setCreateUser(ShiroUtil.getLoginName());
		outcome.setStatus(Outcome.STATUS_UNCHECK);
		Teacher teacher=new Teacher();
		teacher.setTeacherId(ShiroUtil.getLoginUser().getTeacherId());
		outcome.setTeacher(teacher);
		service.add(outcome);
		return outcome.getOutcomeId().toString();
	}
	@RequestMapping(value="updateOutcome",method = RequestMethod.POST)
	@ResponseBody
	public String updateOutcome(@RequestBody Outcome outcome) {
		service.update(outcome);
		return outcome.getOutcomeId().toString();
	}
	@ResponseBody
	@RequestMapping(value="fileUpload",method = RequestMethod.POST)
	public void fileUpload(MultipartFile fileUpload,HttpServletRequest request,@RequestParam(value="outcomeId") Long outcomeId){
		MultipartHttpServletRequest multipartRequest=(MultipartHttpServletRequest)request;
		MultipartFile file = multipartRequest.getFile("fileUpload");
		service.saveAttach(file, outcomeId);
	
	}
	@RequestMapping(value="findAttach",method = RequestMethod.GET)
	@ResponseBody
	public List<OutcomeAttach> findAttach(@RequestParam(value="outcomeId") Long outcomeId){
		return service.findAttach(outcomeId);
	}
	@RequestMapping(value="deleteAttach",method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteAttach(@RequestParam(value="attachId") Long attachId){
		service.deleteAttach(attachId);
	}
	@RequestMapping(value = "downloadAttach", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadAttach(Model model,@RequestParam(value = "attachId") Long attachId) {
		
		OutcomeAttach attach = attachDao.findOne(attachId);
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentDispositionFormData("attachment", new String(
					attach.getOldFileName().getBytes("utf-8"), "ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		
		return new ResponseEntity<byte[]>(service.getAttachContent(attach), headers,HttpStatus.CREATED);
	}
	@RequestMapping(value="checkMain",method = RequestMethod.GET)
	public String checkMain(Model model){
		return "evaluation/outcome/checkMain";
	}
	@RequestMapping(value="findPage",method = RequestMethod.GET)
	@ResponseBody
	public List<OutcomeVo> findPage(){
		return service.findAll();
	}
	@RequestMapping(value="check",method = RequestMethod.POST)
	@ResponseBody
	public String check(@RequestParam(value="outcomeId") Long outcomeId){
		service.check(outcomeId, ShiroUtil.getLoginName());
		return "success";
	}
}
