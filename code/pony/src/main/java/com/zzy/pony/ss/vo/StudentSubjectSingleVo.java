package com.zzy.pony.ss.vo;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-16
 * @Description
 */
public class StudentSubjectSingleVo  {

    private int subjectId;
    private String subjectName;
    private int countNum;
    public StudentSubjectSingleVo(){}

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getCountNum() {
        return countNum;
    }

    public void setCountNum(int countNum) {
        this.countNum = countNum;
    }
}
