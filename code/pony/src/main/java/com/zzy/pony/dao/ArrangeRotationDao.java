package com.zzy.pony.dao;




import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.TeacherSubject;
import com.zzy.pony.model.Term;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zzy.pony.model.ArrangeRotation;

import java.util.List;


public interface ArrangeRotationDao extends JpaRepository<ArrangeRotation,Integer>{

	List<ArrangeRotation> findByteacherSubjects(List<TeacherSubject> teacherSubjects);
	List<ArrangeRotation> findBySchoolYearAndTerm(SchoolYear schoolYear, Term term);

}
