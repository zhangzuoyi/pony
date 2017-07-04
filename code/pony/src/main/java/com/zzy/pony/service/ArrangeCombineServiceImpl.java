package com.zzy.pony.service;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.zzy.pony.dao.ArrangeRotationDao;
import com.zzy.pony.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzy.pony.dao.ArrangeCombineDao;
import com.zzy.pony.vo.CombineAndRotationVo;




@Service
@Transactional
public class ArrangeCombineServiceImpl implements ArrangeCombineService {

	@Autowired
	private ArrangeCombineDao arrangeCombineDao;
	@Autowired
	private SchoolYearService schoolYearService;
	@Autowired
	private TermService termService;
	@Autowired
	private ArrangeRotationDao arrangeRotationDao;


	
	@Override
	public List<CombineAndRotationVo> findCurrentAllVo() {
		// TODO Auto-generated method stub
		List<CombineAndRotationVo> result = new ArrayList<CombineAndRotationVo>();
		SchoolYear schoolYear = schoolYearService.getCurrent();
		Term term  = termService.getCurrent();
		List<ArrangeCombine> arrangeCombines = arrangeCombineDao.findBySchoolYearAndTerm(schoolYear,term);
		for (ArrangeCombine arrangeCombine : arrangeCombines) {
			for (TeacherSubject teacherSubject : arrangeCombine.getTeacherSubjects()) {
				CombineAndRotationVo vo =new CombineAndRotationVo();
				vo.setClassId(teacherSubject.getSchoolClass().getClassId());
				vo.setClassName(teacherSubject.getSchoolClass().getName());
				vo.setCombineId(arrangeCombine.getCombineId());
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

	@Override
	public boolean isTeacherSubjectExist(List<TeacherSubject> teacherSubjects) {
		// TODO Auto-generated method stub
		List<ArrangeCombine> list =  arrangeCombineDao.findByteacherSubjects(teacherSubjects);
		if (!list.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public Map<String, Integer> getCombineMap() {
		Map<String,Integer> result = new HashMap<String, Integer>();
		SchoolYear schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		List<ArrangeCombine> arrangeCombines = arrangeCombineDao.findBySchoolYearAndTerm(schoolYear,term);
		for (ArrangeCombine ac:
				arrangeCombines) {
			for (TeacherSubject ts:
				 ac.getTeacherSubjects()) {
				int count = 0;
				List<TeacherSubject> teacherSubjects =  new ArrayList<TeacherSubject>();
				teacherSubjects.add(ts);
				List<ArrangeRotation> arrangeRotations = arrangeRotationDao.findByteacherSubjects(teacherSubjects);
				if (arrangeRotations != null && !arrangeRotations.isEmpty()){
					break;
				}
				if(count == ac.getTeacherSubjects().size()){
					for (TeacherSubject teacherSubject:
					ac.getTeacherSubjects()) {
						String teacherId = String.format("%04d", teacherSubject.getTeacher().getTeacherId())  ;
						String classId = String.format("%03d", teacherSubject.getSchoolClass().getClassId())  ;
						String subjectId = String.format("%02d", teacherSubject.getSubject().getSubjectId())  ;
						result.put(teacherId+classId+subjectId,ac.getCombineId());
					}
				}
			}

		}


		return result;
	}

	@Override
	public Map<String, Integer> getSpecialMap() {
		Map<String,Integer> result = new HashMap<String, Integer>();
		SchoolYear schoolYear = schoolYearService.getCurrent();
		Term term = termService.getCurrent();
		List<ArrangeCombine> arrangeCombines = arrangeCombineDao.findBySchoolYearAndTerm(schoolYear,term);
		for (ArrangeCombine ac:
				arrangeCombines) {
			for (TeacherSubject ts:
					ac.getTeacherSubjects()) {
				List<TeacherSubject> teacherSubjects =  new ArrayList<TeacherSubject>();
				teacherSubjects.add(ts);
				List<ArrangeRotation> arrangeRotations = arrangeRotationDao.findByteacherSubjects(teacherSubjects);
				if (arrangeRotations != null && !arrangeRotations.isEmpty()){
					for (TeacherSubject teacherSubject:
						 ac.getTeacherSubjects()) {
						List<TeacherSubject> list =  new ArrayList<TeacherSubject>();
						list.add(teacherSubject);
						List<ArrangeRotation> arrangeRotationList = arrangeRotationDao.findByteacherSubjects(list);
						if (arrangeRotationList!= null && !arrangeRotationList.isEmpty()){
							String rotationId = "R"+String.format("%05d",arrangeRotationList.get(0).getRotationId());
							result.put(rotationId,ac.getCombineId());
						}else{
							String teacherId = String.format("%04d", teacherSubject.getTeacher().getTeacherId())  ;
							String classId = String.format("%03d", teacherSubject.getSchoolClass().getClassId())  ;
							String subjectId = String.format("%02d", teacherSubject.getSubject().getSubjectId())  ;
							result.put(teacherId+classId+subjectId,ac.getCombineId());
						}
					}
					break;
				}

			}

		}



		return result;
	}
}
