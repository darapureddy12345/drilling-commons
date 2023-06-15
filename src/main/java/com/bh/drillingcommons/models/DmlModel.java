package com.bh.drillingcommons.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class DmlModel implements Serializable {

	private static final long serialVersionUID = 2850935840353482263L;

	private String lastUpdatedBySSO;
	private String dateUpdated;
	private String lastUpdatedBy;
	private String parentAssembly;
	private String childAssembly;
	private String discipline;
	private String maintenanceInterval;
	private String taskName;
	private String workPackStatus;
	private String id;
	private String comments;
	private String customers;
	private String rigs;
}
