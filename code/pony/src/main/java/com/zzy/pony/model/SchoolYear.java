package com.zzy.pony.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the t_school_year database table.
 * 
 */
@Entity
@Table(name="t_school_year")
@NamedQuery(name="SchoolYear.findAll", query="SELECT s FROM SchoolYear s")
public class SchoolYear implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="YEAR_ID")
	private Integer yearId;
	
	@Column(name="START_YEAR")
	private Integer startYear;
	
	@Column(name="END_YEAR")
	private Integer endYear;
	
	@Column(name="IS_CURRENT")
	private String isCurrent;

	public SchoolYear() {
	}

	public String getName(){
		return startYear+"-"+endYear+"学年";
	}
	public Integer getYearId() {
		return this.yearId;
	}

	public void setYearId(Integer yearId) {
		this.yearId = yearId;
	}

	public String getIsCurrent() {
		return isCurrent;
	}

	public void setIsCurrent(String isCurrent) {
		this.isCurrent = isCurrent;
	}

	public Integer getStartYear() {
		return startYear;
	}

	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}

	public Integer getEndYear() {
		return endYear;
	}

	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}

}