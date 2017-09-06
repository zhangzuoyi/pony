package com.zzy.pony.vo;

import java.util.List;

import com.zzy.pony.model.PrizePunish;
import com.zzy.pony.model.Student;
import com.zzy.pony.model.StudentRemark;

public class StudentCard {
	private Student student;
	private List<ExamResultVo> results;
	private List<PrizePunish> pps;
	private List<StudentRemark> remarks;
	
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public List<ExamResultVo> getResults() {
		return results;
	}
	public void setResults(List<ExamResultVo> results) {
		this.results = results;
	}
	public List<PrizePunish> getPps() {
		return pps;
	}
	public void setPps(List<PrizePunish> pps) {
		this.pps = pps;
	}
	public List<StudentRemark> getRemarks() {
		return remarks;
	}
	public void setRemarks(List<StudentRemark> remarks) {
		this.remarks = remarks;
	}
	
	
}
