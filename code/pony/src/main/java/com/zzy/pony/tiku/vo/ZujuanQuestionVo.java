package com.zzy.pony.tiku.vo;

import java.math.BigDecimal;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-22
 * @Description
 */
public class ZujuanQuestionVo {

    private int id;
    private int questionId;
    private int zujuanId;
    private int seq;
    private int  score;
    private String question;
    private String items;
    private Object[] itemArr;//答案备选项



    public ZujuanQuestionVo(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getZujuanId() {
        return zujuanId;
    }

    public void setZujuanId(int zujuanId) {
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public Object[] getItemArr() {
        return itemArr;
    }

    public void setItemArr(Object[] itemArr) {
        this.itemArr = itemArr;
    }
}
