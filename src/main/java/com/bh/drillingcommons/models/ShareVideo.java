package com.bh.drillingcommons.models;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * ShareVideo Model Object
 * 
 * @author BH
 * 
 */
@Data
public class ShareVideo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long videoRecommendId;
	
	private Long videoId;
	
	private String videoName;
	
	private String recommendedBy;
	
	private String recommendedTo;
	
	private String createdBy;
	
	private Date createdDate;
	
	private String lastUpdatedBy;
	
	private Date lastUpdatedDate;
	
	private String emailSubject;
	
	private String emailBody;
	
	private String userRole;
	
	private String userCompany;
}