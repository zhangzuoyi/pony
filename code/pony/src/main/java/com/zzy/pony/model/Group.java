package com.zzy.pony.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the t_group database table.
 * 
 */
@Entity
@Table(name="t_group")
@NamedQuery(name="Group.findAll", query="SELECT g FROM Group g")
public class Group implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="GROUP_ID")
	private int groupId;

	@Column(name="GROUP_TYPE")
	private String groupType;

	private String name;

	//bi-directional many-to-many association to User
	@ManyToMany
	@JoinTable(
			name="t_user_group"
			, joinColumns={
				@JoinColumn(name="GROUP_ID")
				}
			, inverseJoinColumns={
				@JoinColumn(name="USER_ID")
				}
			)
	private List<User> users;

	public Group() {
	}

	public int getGroupId() {
		return this.groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupType() {
		return this.groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}