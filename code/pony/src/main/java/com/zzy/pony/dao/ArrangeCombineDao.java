package com.zzy.pony.dao;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.ArrangeCombine;
import com.zzy.pony.model.TeacherSubject;



public interface ArrangeCombineDao extends JpaRepository<ArrangeCombine,Integer>{
	
	List<ArrangeCombine> findByteacherSubjects(List<TeacherSubject> teacherSubjects);

}
