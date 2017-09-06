package com.zzy.pony.vo;

public class LessonSelectArrangeStudentVo extends LessonSelectArrangeVo {
	private int studentId;
	private boolean isChoose;
	
	public static LessonSelectArrangeStudentVo valueOf(LessonSelectArrangeVo vo, int studentId, boolean isChoose){
		LessonSelectArrangeStudentVo re=new LessonSelectArrangeStudentVo();
		re.setArrangeId(vo.getArrangeId());
		re.setChoose(isChoose);
		re.setClassroom(vo.getClassroom());
		re.setCredit(vo.getCredit());
		re.setGradeIds(vo.getGradeIds());
		re.setGrades(vo.getGrades());
		re.setLessonSelectTimes(vo.getLessonSelectTimes());
		re.setLowerLimit(vo.getLowerLimit());
		re.setPeriod(vo.getPeriod());
		re.setStudentId(studentId);
		re.setSubjectId(vo.getSubjectId());
		re.setSubjectName(vo.getSubjectName());
		re.setTeacherId(vo.getTeacherId());
		re.setTeacherName(vo.getTeacherName());
		re.setTermId(vo.getTermId());
		re.setTermName(vo.getTermName());
		re.setUpperLimit(vo.getUpperLimit());
		re.setYearId(vo.getYearId());
		re.setYearName(vo.getYearName());

		return re;
	}
	
	public int getStudentId() {
		return studentId;
	}
	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	public boolean isChoose() {
		return isChoose;
	}
	public void setChoose(boolean isChoose) {
		this.isChoose = isChoose;
	}
	
	
}
