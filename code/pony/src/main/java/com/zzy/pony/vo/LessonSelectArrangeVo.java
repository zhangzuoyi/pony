package com.zzy.pony.vo;

import java.util.ArrayList;
import java.util.List;

import com.zzy.pony.config.Constants;
import com.zzy.pony.model.Grade;
import com.zzy.pony.model.LessonPeriod;
import com.zzy.pony.model.LessonSelectArrange;
import com.zzy.pony.model.LessonSelectTime;
import com.zzy.pony.model.SchoolYear;
import com.zzy.pony.model.Subject;
import com.zzy.pony.model.Teacher;
import com.zzy.pony.model.Term;

public class LessonSelectArrangeVo {
	private Integer arrangeId;

	private String classroom;

	private int credit;

	private int lowerLimit;

	private int period;

	private int upperLimit;

	private Integer teacherId;
	private String teacherName;

	private Integer yearId;
	private String yearName;

	private Integer termId;
	private String termName;

	private Integer subjectId;
	private String subjectName;

	private List<Grade> grades;
	private List<Integer> gradeIds;
	private String gradeIdsStr;

	private List<LessonSelectTimeVo> lessonSelectTimes;
	
	public String getGradesStr(){
		if(grades == null){
			return null;
		}
		StringBuilder sb=new StringBuilder();
		for(Grade g: grades){
			sb.append(g.getName()+",");
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	public String getTimesStr(){
		StringBuilder sb=new StringBuilder();
		for(LessonSelectTimeVo g: lessonSelectTimes){
			sb.append(Constants.WEEKDAYS.get(g.getWeekday())+"第"+g.getPeriodSeq()+"节,");
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	public void setGradeIdsFromStr(){
		String[] strs=getGradeIdsStr().split(",");
		List<Integer> gIds=new ArrayList<Integer>();
		for(String str:strs){
			gIds.add(Integer.valueOf(str));
		}
		setGradeIds(gIds);
	}

	public Integer getArrangeId() {
		return arrangeId;
	}

	public void setArrangeId(Integer arrangeId) {
		this.arrangeId = arrangeId;
	}

	public String getClassroom() {
		return classroom;
	}

	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getLowerLimit() {
		return lowerLimit;
	}

	public void setLowerLimit(int lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getUpperLimit() {
		return upperLimit;
	}

	public void setUpperLimit(int upperLimit) {
		this.upperLimit = upperLimit;
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

	public List<Integer> getGradeIds() {
		return gradeIds;
	}

	public void setGradeIds(List<Integer> gradeIds) {
		this.gradeIds = gradeIds;
	}

	public List<LessonSelectTimeVo> getLessonSelectTimes() {
		return lessonSelectTimes;
	}

	public void setLessonSelectTimes(List<LessonSelectTimeVo> lessonSelectTimes) {
		this.lessonSelectTimes = lessonSelectTimes;
	}
	
	public List<Grade> getGrades() {
		return grades;
	}

	public void setGrades(List<Grade> grades) {
		this.grades = grades;
	}

	public String getGradeIdsStr() {
		return gradeIdsStr;
	}

	public void setGradeIdsStr(String gradeIdsStr) {
		this.gradeIdsStr = gradeIdsStr;
	}

//	public LessonSelectArrange toModel(){
//		LessonSelectArrange arr=new LessonSelectArrange();
//		arr.setArrangeId(arrangeId);
//		arr.setClassroom(getClassroom());
//		arr.setCredit(getCredit());
//		List<Grade> grades=new ArrayList<Grade>();
//		for(Integer gradeId: getGradeIds()){
//			Grade grade=new Grade();
//			grade.setGradeId(gradeId);
//			grades.add(grade);
//		}
//		arr.setGrades(grades);
//		List<LessonSelectTime> times=new ArrayList<LessonSelectTime>();
//		for(LessonSelectTimeVo st: getLessonSelectTimes()){
//			LessonSelectTime time=new LessonSelectTime();
//			LessonPeriod lessonPeriod=new LessonPeriod();
//			lessonPeriod.setPeriodId(st.getPeriodId());
//			time.setLessonPeriod(lessonPeriod);
//			time.setWeekDay(st.getWeekday());
//			time.setLessonSelectArrange(arr);
//			times.add(time);
//		}
//		arr.setLessonSelectTimes(times);
//		arr.setLowerLimit(getLowerLimit());
//		arr.setPeriod(period);
//		SchoolYear schoolYear=new SchoolYear();
//		schoolYear.setYearId(getYearId());
//		arr.setSchoolYear(schoolYear);
//		Subject subject=new Subject();
//		subject.setSubjectId(getSubjectId());
//		arr.setSubject(subject);
//		Teacher teacher=new Teacher();
//		teacher.setTeacherId(getTeacherId());
//		arr.setTeacher(teacher);
//		Term term=new Term();
//		term.setTermId(getTermId());
//		arr.setTerm(term);
//		arr.setUpperLimit(getUpperLimit());
//		
//		return arr;
//	}
}
