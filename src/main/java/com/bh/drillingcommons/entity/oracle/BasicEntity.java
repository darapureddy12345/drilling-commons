package com.bh.drillingcommons.entity.oracle;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BasicEntity {

	@JsonIgnore
	@Column(name="CRTD_BY")
	private String createdBy;
	
	@JsonIgnore
	@Column(name="CRDT_DT")
	private Date createdDate;
	
	@JsonIgnore
	@Column(name="LST_UPDT_BY")
	private String lastUpdatedBy;
	
	@JsonIgnore
	@Column(name="LST_UPDT_DT")
	private Date lastUpdatedDate;
}
