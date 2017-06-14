package com.zzy.pony.service;




import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

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
	
	@Override
	public List<CombineAndRotationVo> findAllVo() {
		// TODO Auto-generated method stub
		List<CombineAndRotationVo> result = new ArrayList<CombineAndRotationVo>();
		List<ArrangeRotation> arrangeRotations = arrangeRotationDao.findAll();
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
