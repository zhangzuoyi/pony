package com.zzy.pony.controller;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

























import com.zzy.pony.dao.SchoolClassDao;
import com.zzy.pony.dao.TeacherDao;
import com.zzy.pony.dao.TeacherSubjectDao;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.service.TeacherSubjectService;
import com.zzy.pony.vo.TeacherSubjectVo;
import com.zzy.pony.vo.ConditionVo;

@Controller
@RequestMapping(value = "/teacherLesson")
public class TeacherLessonController {
	@Autowired
	private TeacherSubjectService teacherSubjectService;
	@Autowired
	private TeacherDao teacherDao;
	@Autowired
	private TeacherSubjectDao teacherSubjectDao;
	@Autowired
	private SchoolClassDao schoolClassDao;
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){

	
		return "teacherLesson/main";
	}
	
	@RequestMapping(value="list",method = RequestMethod.POST)
	@ResponseBody
	public List<TeacherSubjectVo> list(@RequestBody ConditionVo cv ){
		List<TeacherSubjectVo> resultList = new ArrayList<TeacherSubjectVo>();
		
		resultList = teacherSubjectService.findCurrentVoByCondition(cv);
		/*if (teacherId != null) {
			Teacher teacher =  teacherDao.findOne(teacherId);
			 resultList =  teacherSubjectService.findCurrentVoByTeacher(teacher);
		}else {
			List<Teacher> teachers = teacherDao.findAll();
			for (Teacher teacher : teachers) {
				List<TeacherSubjectVo> list = teacherSubjectService.findCurrentVoByTeacher(teacher);
				resultList.addAll(list);				
			}
		}*/				
		return resultList;
	}
	@RequestMapping(value="listByTeacher",method = RequestMethod.GET)
	@ResponseBody
	public List<TeacherSubjectVo> listByTeacher(@RequestParam(value="teacherId") int teacherId ){
		List<TeacherSubjectVo> resultList = new ArrayList<TeacherSubjectVo>();
		
			 Teacher teacher =  teacherDao.findOne(teacherId);
			 resultList =  teacherSubjectService.findCurrentVoByTeacher(teacher);
						
		return resultList;
	}
	@RequestMapping(value="listBySchoolClass",method = RequestMethod.GET)
	@ResponseBody
	public List<TeacherSubjectVo> listBySchoolClass(@RequestParam(value="classId")Integer classId ){
		List<TeacherSubjectVo> resultList = new ArrayList<TeacherSubjectVo>();
		if (classId != null) {
			SchoolClass schoolClass =  schoolClassDao.findOne(classId);
			 resultList =  teacherSubjectService.findCurrentVoBySchoolClass(schoolClass);
		}else {
			List<SchoolClass> schoolClasses = schoolClassDao.findAll();
			for (SchoolClass schoolClass : schoolClasses) {
				List<TeacherSubjectVo> list = teacherSubjectService.findCurrentVoBySchoolClass(schoolClass);
				resultList.addAll(list);				
			}
		}				
		return resultList;
	}
	@RequestMapping(value="submit",method = RequestMethod.GET)
	@ResponseBody
	public void submit(@RequestParam(value="teacherIds[]")String[] teacherIds,@RequestParam(value="weekArrange")String weekArrange ){
		if (teacherIds!=null && teacherIds.length>0) {
			for (String teacherId : teacherIds) {
				TeacherSubject  teacherSubject = teacherSubjectDao.findOne(Integer.valueOf(teacherId));
				teacherSubject.setWeekArrange(weekArrange);
				teacherSubjectDao.save(teacherSubject);
			}
		}	
	}
	
	
	
	
}
