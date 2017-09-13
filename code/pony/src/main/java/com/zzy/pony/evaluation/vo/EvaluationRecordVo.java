package com.zzy.pony.evaluation.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class EvaluationRecordVo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long recordId;

	private Date checkTime;

	private String checkUser;

	private String comments;

	private Date createTime;

	private String createUser;

	private String evlResult;

	private String evlTime;

	private int rank;

	private float totalScore;
	
	private Integer teacherId;
	private String teacherName;
	private String teacherNo;
	
	private Long subjectId;
	private String subjectName;

	private List<EvaluationItemDataVo> itemData;

	public EvaluationRecordVo() {
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public String getCheckUser() {
		return checkUser;
	}

	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getEvlResult() {
		return evlResult;
	}

	public void setEvlResult(String evlResult) {
		this.evlResult = evlResult;
	}

	public String getEvlTime() {
		return evlTime;
	}

	public void setEvlTime(String evlTime) {
		this.evlTime = evlTime;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public float getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(float totalScore) {
		this.totalScore = totalScore;
	}

	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTeacherNo() {
		return teacherNo;
	}

	public void setTeacherNo(String teacherNo) {
		this.teacherNo = teacherNo;
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public List<EvaluationItemDataVo> getItemData() {
		return itemData;
	}

	public void setItemData(List<EvaluationItemDataVo> itemData) {
		this.itemData = itemData;
	}


}