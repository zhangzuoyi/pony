package com.zzy.pony.vo;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.zzy.pony.model.LessonArrange;
import com.zzy.pony.model.TeacherSubject;

public class TeacherSubjectVo {
	private Integer tsId;

	private Integer subjectId;
	private String subjectName;

	private Integer teacherId;
	private String teacherName;

	private String gradeName;
	
	private Integer classId;
	private String className;
	private Integer classSeq;
	
	private Integer yearId;
	private String yearName;
	
	private Integer termId;
	private String termName;
	
	private List<LessonArrange> arranges;
	
	private String teacherNo;
	private String weekArrange;

	private int count;//教师上课班级数目
	private int totalWeekArrange;//教师上课总数
	
	
	public static TeacherSubjectVo fromModel(TeacherSubject ts){
		TeacherSubjectVo vo=new TeacherSubjectVo();
		vo.setTsId(ts.getTsId());
		vo.setSubjectId(ts.getSubject().getSubjectId());
		vo.setSubjectName(ts.getSubject().getName());
		vo.setTeacherId(ts.getTeacher().getTeacherId());
		vo.setTeacherName(ts.getTeacher().getName());
		vo.setClassId(ts.getSchoolClass().getClassId());
		vo.setClassName(ts.getSchoolClass().getName());
		vo.setYearId(ts.getYear().getYearId());
		vo.setYearName(ts.getYear().getName());
		vo.setTermId(ts.getTerm().getTermId());
		vo.setTermName(ts.getTerm().getName());
		vo.setTeacherNo(ts.getTeacher().getTeacherNo());
		vo.setWeekArrange(ts.getWeekArrange());
		
		return vo;
	}
	public String getArrangeShowName(){
		String showName="";
		if(arranges != null && arranges.size()>0){
			for(int i=0;i<arranges.size();i++){
				if(i == 0){
					showName +=arranges.get(i).getTimeShowName();
				}else{
					showName +=","+arranges.get(i).getTimeShowName();
				}
			}
		}
		return showName;
	}
	public Integer getTsId() {
		return tsId;
	}
	public void setTsId(Integer tsId) {
		this.tsId = tsId;
	}
	public Integer getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public Integer getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public Integer getClassId() {
		return classId;
	}
	public void setClassId(Integer classId) {
		this.classId = classId;
	}
	public String getClassName() {
		if( ! StringUtils.isBlank(className))
			return className;
		else{
			return gradeName+"("+classSeq+")班";
		}
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Integer getYearId() {
		return yearId;
	}
	public void setYearId(Integer yearId) {
		this.yearId = yearId;
	}
	public String getYearName() {
		return yearName;
	}
	public void setYearName(String yearName) {
		this.yearName = yearName;
	}
	public Integer getTermId() {
		return termId;
	}
	public void setTermId(Integer termId) {
		this.termId = termId;
	}
	public String getTermName() {
		return termName;
	}
	public void setTermName(String termName) {
		this.termName = termName;
	}
	public List<LessonArrange> getArranges() {
		return arranges;
	}
	public void setArranges(List<LessonArrange> arranges) {
		this.arranges = arranges;
	}
	public String getTeacherNo() {
		return teacherNo;
	}
	public void setTeacherNo(String teacherNo) {
		this.teacherNo = teacherNo;
	}
	public String getWeekArrange() {
		return weekArrange;
	}
	public void setWeekArrange(String weekArrange) {
		this.weekArrange = weekArrange;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public Integer getClassSeq() {
		return classSeq;
	}
	public void setClassSeq(Integer classSeq) {
		this.classSeq = classSeq;
	}
	
	
	
}
