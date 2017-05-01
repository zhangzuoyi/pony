package com.zzy.pony.property;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the t_department database table.
 * 
 */
@Entity
@Table(name="t_department")
@NamedQuery(name="Department.findAll", query="SELECT d FROM Department d")
public class Department implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="DEPT_ID")
	private int deptId;

	private String name;

	//bi-directional many-to-one association to Property
//	@OneToMany(mappedBy="department")
//	private List<Property> properties;

	public Department() {
	}

	public int getDeptId() {
		return this.deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}