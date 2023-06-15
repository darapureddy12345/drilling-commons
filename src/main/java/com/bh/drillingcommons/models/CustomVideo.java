package com.bh.drillingcommons.models;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * CustomVideo Model Object
 * 
 * @author BH 
 *
 */
@Data
public class CustomVideo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long drillReqId;
	
	private String requestorFirstName;
	
	private String requestorLastName;
	
	private String userId;
	
	private String requestorEmail;
	
	private String videoCategory;
	
	private String videoCategoryDesc;
	
	private String videoDesc;
	
	private String reqStatus;
	
	private String crtdBy;
	
	private Date crtdDt;
	
	private String lstUpdtdBy;
	
	private Date lstUpdtDt;
}