package com.zzy.pony.service;




import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.zzy.pony.dao.ArrangeCombineDao;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.ArrangeRotationDao;
import com.zzy.pony.model.ArrangeRotation;
import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.vo.CombineAndRotationVo;




@Service
@Transactional
public class ArrangeRotationServiceImpl implements ArrangeRotationService {

	@Autowired
	private ArrangeRotationDao arrangeRotationDao;
	@Autowired
	private ArrangeCombineDao arrangeCombineDao;
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;



	
	@Override
	public List<CombineAndRotationVo> findCurrentAllVo() {
		// TODO Auto-generated method stub
		List<CombineAndRotationVo> result = new ArrayList<CombineAndRotationVo>();
		SchoolYear schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		List<ArrangeRotation> arrangeRotations = arrangeRotationDao.findBySchoolYearAndTerm(schoolYear,term);
		for (ArrangeRotation arrangeRotation : arrangeRotations) {
			for (TeacherSubject teacherSubject : arrangeRotation.getTeacherSubjects()) {
				CombineAndRotationVo vo =new CombineAndRotationVo();
				vo.setClassId(teacherSubject.getSchoolClass().getClassId());
				vo.setClassName(teacherSubject.getSchoolClass().getName());
				vo.setRotationId(arrangeRotation.getRotationId());
				vo.setSubjectId(teacherSubject.getSubject().getSubjectId());
				vo.setSubjectName(teacherSubject.getSubject().getName());
				vo.setTeacherId(teacherSubject.getTeacher().getTeacherId());
				vo.setTeacherName(teacherSubject.getTeacher().getName());
				result.add(vo);
			}
		}
		
		return result;
	}



	@Override
	public ArrangeRotation get(int id) {
		// TODO Auto-generated method stub
		return arrangeRotationDao.findOne(id);
	}
	
	
	
	
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
}
