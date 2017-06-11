package com.zzy.pony.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the t_role database table.
 * 
 */
@Entity
@Table(name="t_role")
@NamedQuery(name="Role.findAll", query="SELECT r FROM Role r")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ROLE_CODE")
	private String roleCode;

	@Column(name="ROLE_NAME")
	private String roleName;
	
	//bi-directional many-to-many association to Resource
	@ManyToMany
	@JoinTable(
		name="t_role_resource"
		, joinColumns={
			@JoinColumn(name="ROLE_CODE")
			}
		, inverseJoinColumns={
			@JoinColumn(name="RES_ID")
			}
		)
	private List<Resource> resources;

//	@ManyToMany(mappedBy="roles")
//	private List<User> users;

	public Role() {
	}

	public String getRoleCode() {
		return this.roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
	
	

//	public List<User> getUsers() {
//		return this.users;
//	}
//
//	public void setUsers(List<User> users) {
//		this.users = users;
//	}

//	public User addUser(User user) {
//		getUsers().add(user);
//		user.setRole(this);
//
//		return user;
//	}
//
//	public User removeUser(User user) {
//		getUsers().remove(user);
//		user.setRole(null);
//
//		return user;
//	}

}