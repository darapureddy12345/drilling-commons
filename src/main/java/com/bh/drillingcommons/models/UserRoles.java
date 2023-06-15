/*
 * Copyright (c) 2012 General Electric Company. All rights reserved.
 *
 * The copyright to the computer software herein is the property of
 * General Electric Company. The software may be used and/or copied only
 * with the written permission of General Electric Company or in accordance
 * with the terms and conditions stipulated in the agreement/contract
 * under which the software has been supplied.
 */
package com.bh.drillingcommons.models;

import java.io.Serializable;

import lombok.Data;

/**
 * {@link UserRoles} object
 * 
 * @author BH
 * 
 */
@Data
public class UserRoles implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String ssoId;
	
	private String roleDesc;
	
	private String permission;
		
	private String userMailId;
	
	private String userContactNo;
	
	private String company;
	
	private String userName;
    
	private String firstName;
    
	private String lastName;
}
