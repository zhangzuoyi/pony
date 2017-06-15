package com.zzy.pony.service;




import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.ArrangeCombineDao;
import com.zzy.pony.model.ArrangeCombine;
import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.vo.CombineAndRotationVo;




@Service
@Transactional
public class ArrangeCombineServiceImpl implements ArrangeCombineService {

	@Autowired
	private ArrangeCombineDao arrangeCombineDao;
	
	@Override
	public List<CombineAndRotationVo> findAllVo() {
		// TODO Auto-generated method stub
		List<CombineAndRotationVo> result = new ArrayList<CombineAndRotationVo>();
		List<ArrangeCombine> arrangeCombines = arrangeCombineDao.findAll();
		for (ArrangeCombine arrangeCombine : arrangeCombines) {
			for (TeacherSubject teacherSubject : arrangeCombine.getTeacherSubjects()) {
				CombineAndRotationVo vo =new CombineAndRotationVo();
				vo.setClassId(teacherSubject.getSchoolClass().getClassId());
				vo.setClassName(teacherSubject.getSchoolClass().getName());
				vo.setRotationId(arrangeCombine.getCombineId());
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
	public ArrangeCombine get(int id) {
		// TODO Auto-generated method stub
		return arrangeCombineDao.findOne(id);
	}
	
	
	
	
	 
	
	
	
	
	
	
	
	
	
	
	
	
	
}
