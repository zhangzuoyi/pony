package com.zzy.pony.oa.vo;

import java.util.Date;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-02-05
 * @Description
 */
public class TaskVo {

    private int id;
    private String name;
    private String description;
    private int access;
    private String[] assignee;
    private String assigneeStr;
    private Date startTime;
    private Date endTime;
    private String startTimeStr;
    private String endTimeStr;
    private String[] members;
    private String membersStr;
    private String[] cc;
    private String ccStr;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;
    private int status;
    private String statusName;
    private String tags;
    public TaskVo(){}

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public String[] getAssignee() {
        return assignee;
    }

    public void setAssignee(String[] assignee) {
        this.assignee = assignee;
    }

    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getAssigneeStr() {
        return assigneeStr;
    }

    public void setAssigneeStr(String assigneeStr) {
        this.assigneeStr = assigneeStr;
    }

    public String getMembersStr() {
        return membersStr;
    }

    public void setMembersStr(String membersStr) {
        this.membersStr = membersStr;
    }

    public String getCcStr() {
        return ccStr;
    }

    public void setCcStr(String ccStr) {
        this.ccStr = ccStr;
    }
}
