package com.zzy.pony.tiku.model;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-22
 * @Description
 */
public class ZujuanQuestion {

    private long id;
    private long questionId;
    private long zujuanId;
    private int seq;
    private int score;
    public ZujuanQuestion(){}

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public long getZujuanId() {
        return zujuanId;
    }

    public void setZujuanId(long zujuanId) {
        this.zujuanId = zujuanId;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


}
