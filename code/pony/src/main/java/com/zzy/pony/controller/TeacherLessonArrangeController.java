package com.zzy.pony.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;













import com.zzy.pony.dao.SchoolClassDao;
import com.zzy.pony.dao.SchoolYearDao;
import com.zzy.pony.dao.SubjectDao;
import com.zzy.pony.dao.TeacherDao;
import com.zzy.pony.dao.TeacherSubjectDao;
import com.zzy.pony.dao.TermDao;

import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.vo.TeacherSubjectVo;

@Controller
@RequestMapping(value = "/teacherLessonArrange")
public class TeacherLessonArrangeController {
	@Autowired
	private SchoolYearDao schoolYearDao;
	@Autowired
	private TermDao	 termDao;
	@Autowired
	private TeacherDao teacherDao;
	@Autowired
	private SubjectDao	subjectDao;
	@Autowired
	private SchoolClassDao schoolClassDao;
	@Autowired
	private TeacherSubjectDao teacherSubjectDao;
	
	
	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){

	
		return "teacherLessonArrange/main";
	}
	@RequestMapping(value="save",method = RequestMethod.POST)
	@ResponseBody
	public void save(@RequestBody TeacherSubjectVo tsv){
		String[] classIds =	 tsv.getClassName().split(",");
		for (String string : classIds) {
			TeacherSubject ts = new TeacherSubject();
			ts.setSchoolClass(schoolClassDao.findOne(Integer.valueOf(string)));
			ts.setSubject(subjectDao.findOne(tsv.getSubjectId()));
			ts.setTeacher(teacherDao.findOne(tsv.getTeacherId()));
			ts.setTerm(termDao.findOne(tsv.getTermId()));
			ts.setYear(schoolYearDao.findOne(tsv.getYearId()));
			if (teacherSubjectDao.findByTeacherAndYearAndTermAndSchoolClassAndSubject(ts.getTeacher(), ts.getYear(), ts.getTerm(), ts.getSchoolClass(), ts.getSubject()).isEmpty()) {
				teacherSubjectDao.save(ts);
			}
			
			
		}
		
	}
	
	
	
}
