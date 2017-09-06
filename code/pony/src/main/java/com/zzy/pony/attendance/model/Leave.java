package com.zzy.pony.attendance.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.activiti.engine.task.Task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zzy.pony.model.User;


/**
 * The persistent class for the t_leave database table.
 * 
 */
@Entity
@Table(name="t_leave")
@NamedQuery(name="Leave.findAll", query="SELECT l FROM Leave l")
@JsonIgnoreProperties(value={"task"})
public class Leave implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name="APPLY_DATE")
	private Date applyDate;

	private String attach;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME")
	private Date createTime;

	@Column(name="CREATE_USER")
	private String createUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="END_TIME")
	private Date endTime;

	@Column(name="LEAVE_TYPE")
	private String leaveType;

	private String reason;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="START_TIME")
	private Date startTime;

	private String status;

	@Column(name="TIME_LENGTH")
	private Float timeLength;

	@Column(name="TIME_TYPE")
	private Integer timeType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_TIME")
	private Date updateTime;

	@Column(name="UPDATE_USER")
	private String updateUser;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private User user;
	
	@Column(name="PROCESS_INSTANCE_ID")
	private String processInstanceId;
	// 流程任务
	@Transient
    private Task task;

	public Leave() {
	}

	public String getTaskName(){
		return task.getName();
	}
	public String getTaskAssignee(){
		return task.getAssignee();
	}
	public String getTaskId(){
		return task.getId();
	}
	public String getInstanceId(){
		return task.getProcessInstanceId();
	}
	public String getProcessDefinitionId(){
		return task.getProcessDefinitionId();
	}
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getApplyDate() {
		return this.applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getAttach() {
		return this.attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getLeaveType() {
		return this.leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Float getTimeLength() {
		return this.timeLength;
	}

	public void setTimeLength(Float timeLength) {
		this.timeLength = timeLength;
	}

	public Integer getTimeType() {
		return this.timeType;
	}

	public void setTimeType(Integer timeType) {
		this.timeType = timeType;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

}