package com.zzy.pony.dao;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;






import com.zzy.pony.model.Grade;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.SubjectNoCourse;
import com.zzy.pony.model.Term;



public interface SubjectNoCourseDao extends JpaRepository<SubjectNoCourse,Integer>{
	List<SubjectNoCourse> findByGradeAndSubjectAndSchoolYearAndTerm(Grade grade,Subject subject,SchoolYear schoolYear,Term term);
	List<SubjectNoCourse> findBySchoolYearAndTerm(SchoolYear schoolYear,Term term);
}
