package com.zzy.pony.exam.vo;




public class ExamineeVo {
	private int examineeId;
	private int examId;
	private int studentId;
	private String regNo;
	private String name;
	private String sex;
	private String studentNo;
	private String examSubjects;
	private int classId;
	private String className;
	private float totalScore;
	private int classRank;
	private int gradeRank;

	public ExamineeVo(){}

	public int getExamineeId() {
		return examineeId;
	}

	public void setExamineeId(int examineeId) {
		this.examineeId = examineeId;
	}

	public int getExamId() {
		return examId;
	}

	public void setExamId(int examId) {
		this.examId = examId;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public String getExamSubjects() {
		return examSubjects;
	}

	public void setExamSubjects(String examSubjects) {
		this.examSubjects = examSubjects;
	}

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(float totalScore) {
        this.totalScore = totalScore;
    }

    public int getClassRank() {
        return classRank;
    }

    public void setClassRank(int classRank) {
        this.classRank = classRank;
    }

    public int getGradeRank() {
        return gradeRank;
    }

    public void setGradeRank(int gradeRank) {
        this.gradeRank = gradeRank;
    }

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
    
}