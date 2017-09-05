package com.zzy.pony.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the t_arrange_combine database table.
 * 
 */
@Entity
@Table(name="t_arrange_combine")
@NamedQuery(name="ArrangeCombine.findAll", query="SELECT a FROM ArrangeCombine a")
public class ArrangeCombine implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COMBINE_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int combineId;

	//bi-directional many-to-one association to Term
	@ManyToOne
	@JoinColumn(name="TERM_ID")
	private Term term;

	//bi-directional many-to-one association to SchoolYear
	@ManyToOne
	@JoinColumn(name="YEAR_ID")
	private SchoolYear schoolYear;

	//bi-directional many-to-many association to TeacherSubject
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="t_teacher_arrange_combine"
			, joinColumns={
				@JoinColumn(name="COMBINE_ID")
				}
			, inverseJoinColumns={
				@JoinColumn(name="TS_ID")
				}
			)
	private List<TeacherSubject> teacherSubjects;

	public ArrangeCombine() {
	}

	public int getCombineId() {
		return this.combineId;
	}

	public void setCombineId(int combineId) {
		this.combineId = combineId;
	}

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

	public List<TeacherSubject> getTeacherSubjects() {
		return this.teacherSubjects;
	}

	public void setTeacherSubjects(List<TeacherSubject> teacherSubjects) {
		this.teacherSubjects = teacherSubjects;
	}

}