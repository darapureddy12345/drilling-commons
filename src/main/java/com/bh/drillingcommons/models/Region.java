package com.bh.drillingcommons.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class Region implements Serializable {
	
	private static final long serialVersionUID = -2278913883377322601L;
	
	private long regionId;
	
	private String regionCode;
	
	private String regionDesc;
	
	private Long inspCount;
	
	private Long inspOpenCount;
	
	private Long inspClosedCount;
}
