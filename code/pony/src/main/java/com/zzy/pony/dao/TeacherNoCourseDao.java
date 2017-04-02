package com.zzy.pony.dao;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;







import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.TeacherNoCourse;
import com.zzy.pony.model.Term;



public interface TeacherNoCourseDao extends JpaRepository<TeacherNoCourse,Integer>{
	List<TeacherNoCourse> findByTeacherAndSchoolYearAndTerm(Teacher teacher,SchoolYear schoolYear,Term term);
}
