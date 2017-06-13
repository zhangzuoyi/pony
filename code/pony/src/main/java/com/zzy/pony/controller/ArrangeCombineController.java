package com.zzy.pony.controller;




import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;




























import com.zzy.pony.dao.ArrangeCombineDao;
import com.zzy.pony.model.ArrangeCombine;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.model.Term;
import com.zzy.pony.service.SchoolYearService;
import com.zzy.pony.service.TeacherSubjectService;
import com.zzy.pony.service.TermService;
import com.zzy.pony.vo.CombineAndRotationVo;



@Controller
@RequestMapping(value = "/arrangeCombine")
public class ArrangeCombineController {
	
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	@Autowired
	private ArrangeCombineDao arrangeCombineDao;
	@Autowired
	private TeacherSubjectService teacherSubjectService;
	

	@RequestMapping(value="main",method = RequestMethod.GET)
	public String main(Model model){	
		return "arrangeCombine/main";
	}
	
	
	
	@RequestMapping(value="list",method = RequestMethod.POST)
	@ResponseBody
	public List<CombineAndRotationVo> list() {
			List<CombineAndRotationVo> result = new ArrayList<CombineAndRotationVo>();
			SchoolYear year = schoolYearService.getCurrent();
			Term term = termService.getCurrent();
			List<ArrangeCombine> arrangeCombines = arrangeCombineDao.findAll();
			for (ArrangeCombine arrangeCombine : arrangeCombines) {
				CombineAndRotationVo vo = new CombineAndRotationVo();
				vo.setCombineId(arrangeCombine.getCombineId());
				vo.setTermId(term.getTermId());
				vo.setTermName(term.getName());
				vo.setYearId(year.getYearId());
				vo.setYearName(year.getName());
				if (arrangeCombine.getTeacherSubjects() != null && arrangeCombine.getTeacherSubjects().size()>0) {
					List<Integer> tsIds = new ArrayList<Integer>();
					List<String> tsNames = new ArrayList<String>();
					for (TeacherSubject	 teacherSubject : arrangeCombine.getTeacherSubjects()) {
						tsIds.add(teacherSubject.getTsId());
						StringBuilder sb = new StringBuilder();
						sb.append(teacherSubject.getSchoolClass().getName()+":"+teacherSubject.getTeacher().getName()+"("+teacherSubject.getSubject().getName()+");");
						tsNames.add(sb.toString());						
					}
					vo.setTsIds(tsIds);
					vo.setTsNames(tsNames);
				}
				result.add(vo);				
			}			
            return result;			

	}
	
	@RequestMapping(value="delete",method=RequestMethod.GET)
	@ResponseBody
	public void delete(@RequestParam(value="combineId") int combineId){
		arrangeCombineDao.delete(combineId);		
	}
	
	@RequestMapping(value="add",method=RequestMethod.GET)
	@ResponseBody	
	public void add(@RequestParam(value="tsIds[]") Integer[] tsIds){
		List<TeacherSubject> teacherSubjects = new ArrayList<TeacherSubject>();
		for (Integer tsId : tsIds) {
			TeacherSubject ts = teacherSubjectService.get(tsId);
			teacherSubjects.add(ts);
		}
		ArrangeCombine ac = new ArrangeCombine();
		ac.setSchoolYear(schoolYearService.getCurrent());
		ac.setTerm(termService.getCurrent());
		ac.setTeacherSubjects(teacherSubjects);
		arrangeCombineDao.save(ac);
		
		
	}
	
	
	
	
	
	
	
	
}
