package com.zzy.pony.service;

import java.util.List;
import java.util.Map;

import com.zzy.pony.model.ArrangeCombine;
import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.vo.CombineAndRotationVo;









public interface ArrangeCombineService {
	List<CombineAndRotationVo> findCurrentAllVo();
	ArrangeCombine get(int id);
	boolean isTeacherSubjectExist(List<TeacherSubject> teacherSubjects);
	/*
	  将combine分为两类，一种全部不出现在rotation中，将其置于原先的combineMap中
	  key : teacherId+classId+subjectId  value :  combineId
	  第二类包含两种情况，即combine全部出现在rotation之中和combine部分出现在rotation之中
	  无论哪种，构造如下specialMap
	  key : R+rotationId(teacherId+classId+subjectId) value： combineId
	   在初始化课程时，
	   走班仍然初始化R课程，合班C课程只初始化上述的第一类情况，第二类情况由specialMap维护关系
	   在排课时，之前走班单独排，合班有个C资源池排
	  现在，走班R按照之前的逻辑排，只不过考虑合班的因素，需要每次排时去specialMap查看是否存在该key，
	  若有，则要在该key对应的C资源池中找，对于普通课程也一样要去找
	  现在，C资源池不仅维护第一种情况，而且也维护第二种情况的课程安排情况
	 */
	Map<String,Integer> getCombineMap();
	Map<String,Integer> getSpecialMap();
}
