package com.zzy.pony.tiku.vo;

import java.util.List;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-22
 * @Description
 */
public class ZujuanVo {

    private int id;
    private String name;
    private int gradeCode;
    private String gradeName;
    private int subjectCode;
    private String subjectName;
    private List<ZujuanQuestionVo> questions;
    public ZujuanVo(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(int gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(int subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List<ZujuanQuestionVo> getQuestions() {
        return questions;
    }

    public void setQuestions(List<ZujuanQuestionVo> questions) {
        this.questions = questions;
    }
}
