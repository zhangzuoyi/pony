package com.zzy.pony.ss.vo;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-16
 * @Description
 */
public class StudentSubjectResultVo implements Comparable<StudentSubjectResultVo> {

    private int  studentId;
    private String studentName;
    private int classId;
    private String className;
    private String groupName;
    public StudentSubjectResultVo(){}

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public int compareTo(StudentSubjectResultVo o) {
        return o.getClassId()-this.getClassId(); //使用classId排序
    }
}
