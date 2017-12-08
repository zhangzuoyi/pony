package com.zzy.pony.ss.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzy.pony.ss.model.SubjectSelectConfig;
import com.zzy.pony.ss.service.SubjectSelectAdminService;
import com.zzy.pony.ss.service.SubjectSelectConfigService;
import com.zzy.pony.ss.vo.StudentSubjectAdminVo;

@Controller
@RequestMapping(value = "/ss/admin")
public class SubjectSelectAdminController {

	@Autowired
	private SubjectSelectAdminService subjectSelectAdminService;
	@Autowired
	private SubjectSelectConfigService subjectSelectConfigService;

	@RequestMapping(value = "main", method = RequestMethod.GET)
	public String main(Model model) {
		return "ss/admin/main";
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public List<StudentSubjectAdminVo> list(Model model, @RequestParam(value = "studentId") String studentId,@RequestParam(value = "group") String group) {

		int student = 0;
		if (!StringUtils.isEmpty(studentId)) {
			student = Integer.valueOf(studentId);
		}	
		SubjectSelectConfig config = subjectSelectConfigService.getCurrent();
		return subjectSelectAdminService.list(config.getConfigId(), student,group);
	}

	@RequestMapping(value = "edit", method = RequestMethod.POST)
	@ResponseBody
	public String edit(@RequestBody StudentSubjectAdminVo vo) {
		SubjectSelectConfig config = subjectSelectConfigService.getCurrent();
		subjectSelectAdminService.update(Arrays.asList(vo.getSelectSubjects()), vo.getStudentId(),
				config.getConfigId());
		return "success";
	}

}
