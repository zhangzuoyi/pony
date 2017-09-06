package com.zzy.pony.exam.controller;

import java.util.List;









import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zzy.pony.exam.model.ExamRoom;



import com.zzy.pony.exam.service.ExamRoomService;

import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value="/examAdmin/examRoom")
public class ExamRoomController {
	
	@Autowired
	private ExamRoomService examRoomService;
	
	@RequestMapping(value="main",method=RequestMethod.GET)
	public String main(){	
		return "examAdmin/examRoom/main";
	}

	@RequestMapping(value="list",method=RequestMethod.GET)
	@ResponseBody
	public List<ExamRoom> list(){
       return examRoomService.list(); 
	}
	
	@RequestMapping(value="listByPage",method=RequestMethod.GET)
	@ResponseBody
	public Page<ExamRoom> listByPage(@RequestParam(value="currentPage",defaultValue="0") int currentPage,
			@RequestParam(value="pageSize",defaultValue="20") int pageSize){
       Pageable pageable =  new PageRequest(currentPage, pageSize,Direction.ASC,"id");
		return examRoomService.findAll(pageable); 
	}
	
	
	@RequestMapping(value="getExamRooms",method=RequestMethod.GET)
	@ResponseBody
	public List<ExamRoom> getExamRooms(@RequestParam(value="roomIds[]") int[] roomIds){
		return examRoomService.getExamRooms(roomIds); 
	}

	@RequestMapping(value="save",method = RequestMethod.GET)
	@ResponseBody
	public void save(@RequestParam(value="subjectIds[]") int[] subjectIds,
					 @RequestParam(value="roomIds[]") int[] roomIds,
					 @RequestParam(value="examId") int examId,
					 @RequestParam(value="gradeId") int gradeId){

		examRoomService.save(subjectIds,roomIds,examId,gradeId);
	}
	
	
	@RequestMapping(value="add",method = RequestMethod.POST)
	@ResponseBody
	public String add(@RequestBody ExamRoom examroom){		
		String result = "0";
		if (examRoomService.isExist(examroom)) {
			result  = "1";//存在name
		}else {
			examRoomService.add(examroom);
		}
		
		return result;
	}
	
	@RequestMapping(value="update",method = RequestMethod.POST)
	@ResponseBody
	public String update(@RequestBody ExamRoom examroom){		
		String result = "0";	
		examRoomService.update(examroom);			
		return result;
		
		
	}
	
	@RequestMapping(value="delete",method = RequestMethod.GET)
	@ResponseBody
	public void delete(@RequestParam(value="id") int id){		
		examRoomService.delete(id);
	}
	
	
	
}
