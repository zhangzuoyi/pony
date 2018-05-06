package com.zzy.pony.oa.vo;

import java.util.Date;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
public class WorkReportVo {

    private int id;
    private String name;
    private int access;
    private int period;
    private String[] reporter;
    private String reporterStr;
    private Date startDate;
    private Date endDate;
    private String startDateStr;
    private String endDateStr;
    private String content;
    private String[] auditor;
    private String auditorStr;
    private int status;
    private String statusName;
    private Date createTime;
    private Date updateTime;

    public WorkReportVo() {
    }

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

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String[] getReporter() {
        return reporter;
    }

    public void setReporter(String[] reporter) {
        this.reporter = reporter;
    }

    public String getReporterStr() {
        return reporterStr;
    }

    public void setReporterStr(String reporterStr) {
        this.reporterStr = reporterStr;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getAuditor() {
        return auditor;
    }

    public void setAuditor(String[] auditor) {
        this.auditor = auditor;
    }

    public String getAuditorStr() {
        return auditorStr;
    }

    public void setAuditorStr(String auditorStr) {
        this.auditorStr = auditorStr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
