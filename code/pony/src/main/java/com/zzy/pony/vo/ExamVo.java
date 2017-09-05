package com.zzy.pony.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zzy.pony.model.ExamType;
import com.zzy.pony.model.SchoolClass;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Term;

@JsonIgnoreProperties(value={"schoolClasses","subjects"})
public class ExamVo {
	private Integer examId;

	private String name;

	private List<SchoolClass> schoolClasses;

	private Term term;

	private SchoolYear schoolYear;

	private ExamType type;
	
	private Date examDate;

	private List<ExamSubjectVo> subjects;
	
	private Integer[] classIds;
	private Integer[] subjectIds;
	
	public String getSubjectsName(){
		List<String> names=new ArrayList<String>();
		if(subjects != null)
			for(ExamSubjectVo vo: subjects){
				names.add(vo.getSubject().getName());
			}
		return StringUtils.join(names, ",");
	}
	public String getClassesName(){
		List<String> names=new ArrayList<String>();
		if(schoolClasses != null)
			for(SchoolClass vo: schoolClasses){
				names.add(vo.getName());
			}
		return StringUtils.join(names, ",");
	}

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
	public Integer[] getClassIds() {
		if(classIds == null && schoolClasses != null && schoolClasses.size()>0){
			classIds=new Integer[schoolClasses.size()];
			int i=0;
			for(SchoolClass sc: schoolClasses){
				classIds[i]=sc.getClassId();
				i++;
			}
		}
		return classIds;
	}
	public void setClassIds(Integer[] classIds) {
		this.classIds = classIds;
	}
	public Integer[] getSubjectIds() {
		if(subjectIds == null && subjects != null && subjects.size()>0){
			subjectIds=new Integer[subjects.size()];
			int i=0;
			for(ExamSubjectVo sub: subjects){
				subjectIds[i]=sub.getSubject().getSubjectId();
				i++;
			}
		}
		return subjectIds;
	}
	public void setSubjectIds(Integer[] subjectIds) {
		this.subjectIds = subjectIds;
	}

}