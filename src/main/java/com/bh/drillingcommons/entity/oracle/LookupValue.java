package com.bh.drillingcommons.entity.oracle;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "OG_DRILL_LIST_VAL_LKP")
@Data
@EqualsAndHashCode(callSuper=false)
public class LookupValue extends BasicEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	@Column(name = "LIST_ID")
	private Long listId;
	
	@Column(name = "LIST_VAL")
	private String value;
	
	@Id
	@Column(name="LIST_VAL_ID")
	private Long listValId;
	
	@JsonIgnore
	@Column(name = "ACTV_FLG")
	private String activeFlag;
	
	@Column(name = "LIST_VAL_DESC")
	private String description;
	
	@Column(name = "LIST_CD")
	private String type;	
	
	@Override
	public String toString() {
		return "LookupValue :["+value+","+listValId+","+description+","+type+"]";
	}
}
