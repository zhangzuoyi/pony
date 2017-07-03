package com.zzy.pony.dao;




import java.util.List;

import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.ArrangeCombine;
import com.zzy.pony.model.TeacherSubject;



public interface ArrangeCombineDao extends JpaRepository<ArrangeCombine,Integer>{
	
	List<ArrangeCombine> findByteacherSubjects(List<TeacherSubject> teacherSubjects);
	List<ArrangeCombine> findBySchoolYearAndTerm(SchoolYear schoolYear, Term term);

}
