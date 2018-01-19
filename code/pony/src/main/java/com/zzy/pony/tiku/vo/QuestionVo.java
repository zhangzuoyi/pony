package com.zzy.pony.tiku.vo;

import java.util.Date;

/**
 * @Author: WANGCHAO262
 * @Date: 2018-01-18
 * @Description
 */
public class QuestionVo {

    private int id ;
    private String question;
    private String items;
    private String answer;
    private String analysis;
    private String type;
    private String checkPoints;
    private String subject;
    private String grade;
    private String property;
    private String difficulty;
    private String province;
    private String city;
    private String school;
    private String textbook;
    private String source;
    private String collectSource;
    private String creatUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;
    private int seq;
    private String collectUrl;
    private int paperId;
    private int isHandle;
    private Object[] itemArr;//答案备选项
    private boolean showPoint;
    private String paperName;
    private String paperCollectUrl;


    public QuestionVo(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCheckPoints() {
        return checkPoints;
    }

    public void setCheckPoints(String checkPoints) {
        this.checkPoints = checkPoints;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getTextbook() {
        return textbook;
    }

    public void setTextbook(String textbook) {
        this.textbook = textbook;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCollectSource() {
        return collectSource;
    }

    public void setCollectSource(String collectSource) {
        this.collectSource = collectSource;
    }

    public String getCreatUser() {
        return creatUser;
    }

    public void setCreatUser(String creatUser) {
        this.creatUser = creatUser;
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

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getCollectUrl() {
        return collectUrl;
    }

    public void setCollectUrl(String collectUrl) {
        this.collectUrl = collectUrl;
    }

    public int getPaperId() {
        return paperId;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public int getIsHandle() {
        return isHandle;
    }

    public void setIsHandle(int isHandle) {
        this.isHandle = isHandle;
    }

    public Object[] getItemArr() {
        return itemArr;
    }

    public void setItemArr(Object[] itemArr) {
        this.itemArr = itemArr;
    }

    public boolean isShowPoint() {
        return showPoint;
    }

    public void setShowPoint(boolean showPoint) {
        this.showPoint = showPoint;
    }

    public String getPaperName() {
        return paperName;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }

    public String getPaperCollectUrl() {
        return paperCollectUrl;
    }

    public void setPaperCollectUrl(String paperCollectUrl) {
        this.paperCollectUrl = paperCollectUrl;
    }
}
