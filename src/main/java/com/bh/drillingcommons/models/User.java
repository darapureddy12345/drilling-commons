package com.bh.drillingcommons.models;

import java.io.Serializable;

import lombok.Data;

/**
 * @author 502260453
 * 
 */
@Data
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long drillUserId;

	private String userId;

	private String name;

	private String email;

	private String permission;

	private String roleName;

	private String lastUpdatedDate;

	private String shopDrlAccess;

	private String comments;

	private String createdDate;

	private String buyer;

	private String billToAddress;

}
