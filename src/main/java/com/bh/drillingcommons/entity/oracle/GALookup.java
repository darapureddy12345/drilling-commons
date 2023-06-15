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
@Table(name = "OG_DRILL_GOOGLE_ANALYTICS_LKP")
@Data
@EqualsAndHashCode(callSuper=false)
public class GALookup extends BasicEntity implements Serializable {

	private static final long serialVersionUID = -4146687694152052111L;
	
	@Id
	@Column(name = "GA_LKP_ID")
	@JsonIgnore
	private Long gaId;
	
	@Column(name="EVENT_CODE")
	private String eventCode;
	
	@Column(name="EVENT_CATEGORY")
	private String eventCategory;
	
	@Column(name="EVENT_ACTION")
	private String eventAction;
	
	@Column(name="EVENT_LABEL")
	private String eventLabel;
	
	@JsonIgnore
	@Column(name="DESCRIPTION")
	private String description;
	
	@JsonIgnore
	@Column(name="SCREEN_NAME")
	private String screenName;
	
	@JsonIgnore
	@Column(name="ACTV_FLG")
	private String activeFlag;
	
}