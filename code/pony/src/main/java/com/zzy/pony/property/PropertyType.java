package com.zzy.pony.property;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the t_property_type database table.
 * 
 */
@Entity
@Table(name="t_property_type")
@NamedQuery(name="PropertyType.findAll", query="SELECT p FROM PropertyType p")
public class PropertyType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TYPE_ID")
	private int typeId;

	private Integer category;

	private String name;

	//bi-directional many-to-one association to Consumable
//	@OneToMany(mappedBy="propertyType")
//	private List<Consumable> consumables;

	//bi-directional many-to-one association to Property
//	@OneToMany(mappedBy="propertyType")
//	private List<Property> properties;

	public PropertyType() {
	}

	public int getTypeId() {
		return this.typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public Integer getCategory() {
		return this.category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}