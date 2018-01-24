package com.zzy.pony.tiku.model;

import java.util.Date;

public class Record {
    private Long id;

    private Long zujuanId;

    private Integer totalScore;

    private Integer usedTime;

    private String createUser;

    private Date createTime;

    public Record(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getZujuanId() {
        return zujuanId;
    }

    public void setZujuanId(Long zujuanId) {
        this.zujuanId = zujuanId;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Integer usedTime) {
        this.usedTime = usedTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}