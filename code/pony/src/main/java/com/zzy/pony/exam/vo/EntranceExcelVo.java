package com.zzy.pony.exam.vo;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-05-07
 * @Description
 */
public class EntranceExcelVo implements Comparable<EntranceExcelVo> {

    private String schoolName;
    private String className;
    private String studentName;
    private String studentNo;
    /**
     * 科目成绩
     */
    private Map<String,BigDecimal> subjectResultMap;
    //科目排名
    private Map<String,Integer> subjectRankMap;
    //总成绩
    private BigDecimal totalResult;
    //总排名
    private Integer totalRank;
    //缓存某一科目成绩,排序使用
    private BigDecimal subjectResult;

    public EntranceExcelVo(){}

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Map<String, BigDecimal> getSubjectResultMap() {
        return subjectResultMap;
    }

    public void setSubjectResultMap(Map<String, BigDecimal> subjectResultMap) {
        this.subjectResultMap = subjectResultMap;
    }

    public Map<String, Integer> getSubjectRankMap() {
        return subjectRankMap;
    }

    public void setSubjectRankMap(Map<String, Integer> subjectRankMap) {
        this.subjectRankMap = subjectRankMap;
    }

    public BigDecimal getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(BigDecimal totalResult) {
        this.totalResult = totalResult;
    }

    public Integer getTotalRank() {
        return totalRank;
    }

    public void setTotalRank(Integer totalRank) {
        this.totalRank = totalRank;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public BigDecimal getSubjectResult() {
        return subjectResult;
    }

    public void setSubjectResult(BigDecimal subjectResult) {
        this.subjectResult = subjectResult;
    }

    @Override
    public int compareTo(EntranceExcelVo o) {
        return this.getSubjectResult().compareTo(o.getSubjectResult());
    }
}
