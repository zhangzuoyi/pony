package com.zzy.pony.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zzy.pony.model.LessonSelectArrange;
import com.zzy.pony.model.LessonSelectStudent;
import com.zzy.pony.model.Student;


public interface LessonSelectStudentDao extends JpaRepository<LessonSelectStudent,Integer>{
	@Query("select lessonSelectArrange.arrangeId from LessonSelectStudent where student=?1")
	List<Integer> findArrangeIdsByStudent(Student student);
	LessonSelectStudent findByStudentAndLessonSelectArrange(Student student,LessonSelectArrange arrange);
}
