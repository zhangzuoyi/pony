package com.zzy.pony.vo;

import java.util.Date;
import java.util.List;

import com.zzy.pony.model.ExamType;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;

public class ExamVo {
	private Integer examId;

	private String name;

	private List<SchoolClass> schoolClasses;

	private Term term;

	private SchoolYear schoolYear;

	private ExamType type;
	
	private Date examDate;

	private List<ExamSubjectVo> subjects;

	public Integer getExamId() {
		return examId;
	}

	public void setExamId(Integer examId) {
		this.examId = examId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SchoolClass> getSchoolClasses() {
		return schoolClasses;
	}

	public void setSchoolClasses(List<SchoolClass> schoolClasses) {
		this.schoolClasses = schoolClasses;
	}

	public Term getTerm() {
		return term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}

	public SchoolYear getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(SchoolYear schoolYear) {
		this.schoolYear = schoolYear;
	}

	public ExamType getType() {
		return type;
	}

	public void setType(ExamType type) {
		this.type = type;
	}

	public List<ExamSubjectVo> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<ExamSubjectVo> subjects) {
		this.subjects = subjects;
	}

	public Date getExamDate() {
		return examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

}