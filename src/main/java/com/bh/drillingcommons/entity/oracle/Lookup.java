package com.bh.drillingcommons.entity.oracle;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Lookup implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "VALUE")
	private String value;
	
	@Column(name = "LIST_VAL_ID")
	private Long listValId;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "LOOKUP_TYPE")
	private String type;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getListValId() {
		return listValId;
	}

	public void setListValId(Long listValId) {
		this.listValId = listValId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Lookup [value=" + value + ", listValId=" + listValId + ", description=" + description + ", type=" + type
				+ "]";
	}
}