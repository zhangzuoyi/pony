package com.zzy.pony.exam.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zzy.pony.exam.model.ExamArrange;
import com.zzy.pony.model.Exam;
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.Subject;

public interface ExamArrangeDao extends JpaRepository<ExamArrange, Integer> {
	Page<ExamArrange> findByExamAndGrade(Pageable pageable,Exam exam,Grade grade);
	Page<ExamArrange> findByExam(Pageable pageable,Exam exam);
	Page<ExamArrange> findByGrade(Pageable pageable,Grade grade);
	List<ExamArrange> findByExamAndGradeAndSubject(Exam exam, Grade grade, Subject subject);
	List<ExamArrange> findByExam(Exam exam);
	List<ExamArrange> findByExamAndGroupIsNull(Exam exam);
	List<ExamArrange> findByExamAndGrade(Exam exam,Grade grade);

	@Modifying
	@Query("update ExamArrange set examDate = :examDate where arrangeId =:arrangeId")
	void setExamDate(@Param("arrangeId")int arrangeId,@Param("examDate")Date examDate);
	@Modifying
	@Query("update ExamArrange set startTime = :startTime, endTime = :endTime where arrangeId =:arrangeId")
	void setExamTime(@Param("arrangeId")int arrangeId,@Param("startTime")Date startTime,@Param("endTime")Date endTime);
}
