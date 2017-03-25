package com.zzy.pony.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the t_grade database table.
 * 
 */
@Entity
@Table(name="t_grade")
@NamedQuery(name="Grade.findAll", query="SELECT g FROM Grade g")
public class Grade implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="GRADE_ID")
	private Integer gradeId;

	private String name;
	
	private Integer seq;

	//bi-directional many-to-one association to SchoolClass
//	@OneToMany(mappedBy="grade")
//	private List<SchoolClass> schoolClasses;

	public Grade() {
	}

	public Integer getGradeId() {
		return this.gradeId;
	}

	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

//	public List<SchoolClass> getSchoolClasses() {
//		return this.schoolClasses;
//	}
//
//	public void setSchoolClasses(List<SchoolClass> schoolClasses) {
//		this.schoolClasses = schoolClasses;
//	}

//	public SchoolClass addSchoolClass(SchoolClass schoolClass) {
//		getSchoolClasses().add(schoolClass);
//		schoolClass.setGrade(this);
//
//		return schoolClass;
//	}
//
//	public SchoolClass removeSchoolClass(SchoolClass schoolClass) {
//		getSchoolClasses().remove(schoolClass);
//		schoolClass.setGrade(null);
//
//		return schoolClass;
//	}

}