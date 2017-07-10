package com.zzy.pony.exam.controller;

import com.zzy.pony.exam.model.ExamArrange;
import com.zzy.pony.exam.model.ExamRoomAllocate;
import com.zzy.pony.exam.model.Examinee;
import com.zzy.pony.exam.service.ExamArrangeService;
import com.zzy.pony.exam.vo.ExamArrangeVo;
import com.zzy.pony.service.ExamService;
import com.zzy.pony.service.GradeService;
import com.zzy.pony.service.SubjectService;
import com.zzy.pony.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping(value="/examAdmin/examRoomArrange")
public class ExamRoomArrangeController {
	


	
	@RequestMapping(value="main",method=RequestMethod.GET)
	public String main(Model model){
		return "examAdmin/examRoomArrange/main";
	}

	
	
}
