package com.zzy.pony.dao;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;









import com.zzy.pony.model.Grade;
import com.zzy.pony.model.GradeNoCourse;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;



public interface GradeNoCourseDao extends JpaRepository<GradeNoCourse,Integer>{
	List<GradeNoCourse> findByGradeAndSchoolYearAndTerm(Grade grade,SchoolYear schoolYear,Term term);
}
