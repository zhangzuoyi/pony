package com.zzy.pony.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.zzy.pony.config.Constants;


/**
 * The persistent class for the t_lesson_arrange database table.
 * 
 */
@Entity
@Table(name="t_lesson_arrange")
@NamedQuery(name="LessonArrange.findAll", query="SELECT l FROM LessonArrange l")
public class LessonArrange implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ARRANGE_ID")
	private Integer arrangeId;

	@Column(name="CLASS_ID")
	private int classId;

	@Column(name="OTHER_LESSON")
	private String otherLesson;

	@Column(name="WEEK_DAY")
	private String weekDay;
	
	@Column(name="SOURCE_TYPE")
	private String sourceType;//来源类型 0 预排，1 自动，3 调课
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME")
	private Date createTime;

	@Column(name="CREATE_USER")
	private String createUser;

	//bi-directional many-to-one association to LessonPeriod
	@ManyToOne
	@JoinColumn(name="PERIOD_ID")
	private LessonPeriod lessonPeriod;

	//bi-directional many-to-one association to SchoolYear
	@ManyToOne
	@JoinColumn(name="YEAR_ID")
	private SchoolYear schoolYear;

	//bi-directional many-to-one association to Term
	@ManyToOne
	@JoinColumn(name="TERM_ID")
	private Term term;
	
	@ManyToOne
	@JoinColumn(name="SUBJECT_ID")
	private Subject subject;

	public LessonArrange() {
	}
	/**
	 * 上课时间的显示名称
	 * @return
	 */
	public String getTimeShowName(){
		return Constants.WEEKDAYS.get(weekDay)+"第"+lessonPeriod.getSeq()+"节";
	}
	public Integer getArrangeId() {
		return this.arrangeId;
	}

	public void setArrangeId(Integer arrangeId) {
		this.arrangeId = arrangeId;
	}

	public int getClassId() {
		return this.classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public String getOtherLesson() {
		return this.otherLesson;
	}

	public void setOtherLesson(String otherLesson) {
		this.otherLesson = otherLesson;
	}

	public String getWeekDay() {
		return this.weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	public LessonPeriod getLessonPeriod() {
		return this.lessonPeriod;
	}

	public void setLessonPeriod(LessonPeriod lessonPeriod) {
		this.lessonPeriod = lessonPeriod;
	}

	public SchoolYear getSchoolYear() {
		return this.schoolYear;
	}

	public void setSchoolYear(SchoolYear schoolYear) {
		this.schoolYear = schoolYear;
	}

	public Term getTerm() {
		return this.term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
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

}