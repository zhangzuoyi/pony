package com.zzy.pony.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the t_lesson_period database table.
 * 
 */
@Entity
@Table(name="t_lesson_period")
@NamedQuery(name="LessonPeriod.findAll", query="SELECT l FROM LessonPeriod l")
public class LessonPeriod implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PERIOD_ID")
	private Integer periodId;

	@Column(name="END_TIME")
	private String endTime;

	private Integer seq;

	@Column(name="START_TIME")
	private String startTime;
	
	private String stage;//所属时段，1 上午 2 下午 3 晚上
	
	private Integer importance;

	//bi-directional many-to-one association to LessonArrange
//	@OneToMany(mappedBy="lessonPeriod")
//	private List<LessonArrange> lessonArranges;

	//bi-directional many-to-one association to Term
	@ManyToOne
	@JoinColumn(name="TERM_ID")
	private Term term;

	//bi-directional many-to-one association to SchoolYear
	@ManyToOne
	@JoinColumn(name="YEAR_ID")
	private SchoolYear schoolYear;

	public LessonPeriod() {
	}

	public Integer getPeriodId() {
		return this.periodId;
	}

	public void setPeriodId(Integer periodId) {
		this.periodId = periodId;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

//	public List<LessonArrange> getLessonArranges() {
//		return this.lessonArranges;
//	}
//
//	public void setLessonArranges(List<LessonArrange> lessonArranges) {
//		this.lessonArranges = lessonArranges;
//	}

//	public LessonArrange addLessonArrange(LessonArrange lessonArrange) {
//		getLessonArranges().add(lessonArrange);
//		lessonArrange.setLessonPeriod(this);
//
//		return lessonArrange;
//	}
//
//	public LessonArrange removeLessonArrange(LessonArrange lessonArrange) {
//		getLessonArranges().remove(lessonArrange);
//		lessonArrange.setLessonPeriod(null);
//
//		return lessonArrange;
//	}

	public Term getTerm() {
		return this.term;
	}

	public void setTerm(Term term) {
		this.term = term;
	}

	public SchoolYear getSchoolYear() {
		return this.schoolYear;
	}

	public void setSchoolYear(SchoolYear schoolYear) {
		this.schoolYear = schoolYear;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public Integer getImportance() {
		return importance;
	}

	public void setImportance(Integer importance) {
		this.importance = importance;
	}

}