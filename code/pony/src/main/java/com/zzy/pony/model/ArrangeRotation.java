package com.zzy.pony.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the t_arrange_rotation database table.
 * 
 */
@Entity
@Table(name="t_arrange_rotation")
@NamedQuery(name="ArrangeRotation.findAll", query="SELECT a FROM ArrangeRotation a")
public class ArrangeRotation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ROTATION_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int rotationId;

	//bi-directional many-to-one association to SchoolYear
	@ManyToOne
	@JoinColumn(name="YEAR_ID")
	private SchoolYear schoolYear;

	//bi-directional many-to-one association to Term
	@ManyToOne
	@JoinColumn(name="TERM_ID")
	private Term term;

	//bi-directional many-to-many association to TeacherSubject
	@ManyToMany
	@JoinTable(
			name="t_teacher_arrange_rotation"
			, joinColumns={
				@JoinColumn(name="ROTATION_ID")
				}
			, inverseJoinColumns={
				@JoinColumn(name="TS_ID")
				}
			)
	private List<TeacherSubject> teacherSubjects;

	public ArrangeRotation() {
	}

	public int getRotationId() {
		return this.rotationId;
	}

	public void setRotationId(int rotationId) {
		this.rotationId = rotationId;
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

	public List<TeacherSubject> getTeacherSubjects() {
		return this.teacherSubjects;
	}

	public void setTeacherSubjects(List<TeacherSubject> teacherSubjects) {
		this.teacherSubjects = teacherSubjects;
	}

}