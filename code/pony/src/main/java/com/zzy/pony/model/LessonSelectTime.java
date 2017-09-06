package com.zzy.pony.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the t_lesson_select_time database table.
 * 
 */
@Entity
@Table(name="t_lesson_select_time")
@NamedQuery(name="LessonSelectTime.findAll", query="SELECT l FROM LessonSelectTime l")
public class LessonSelectTime implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TIME_ID")
	private Integer timeId;

	@Column(name="WEEK_DAY")
	private String weekDay;

	//bi-directional many-to-one association to LessonPeriod
	@ManyToOne
	@JoinColumn(name="PERIOD_ID")
	private LessonPeriod lessonPeriod;

	//bi-directional many-to-one association to LessonSelectArrange
	@ManyToOne
	@JoinColumn(name="ARRANGE_ID")
	private LessonSelectArrange lessonSelectArrange;

	public LessonSelectTime() {
	}

	public Integer getTimeId() {
		return this.timeId;
	}

	public void setTimeId(Integer timeId) {
		this.timeId = timeId;
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

	public LessonSelectArrange getLessonSelectArrange() {
		return this.lessonSelectArrange;
	}

	public void setLessonSelectArrange(LessonSelectArrange lessonSelectArrange) {
		this.lessonSelectArrange = lessonSelectArrange;
	}

}