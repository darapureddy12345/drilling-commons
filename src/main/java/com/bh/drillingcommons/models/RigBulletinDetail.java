package com.bh.drillingcommons.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.bh.drillingcommons.entity.oracle.OgDrillBulletinRigAudit;

import lombok.Data;

/**
 * {@link RigBulletinDetail} object
 * 
 * @author BH
 *
 */
@Data
public class RigBulletinDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long bulletinRigId;
	
	private String rigId;
	
	private String rigName;
	
	private String bulletinRigApplicableStatus;
	
	private Date actionDt;
	
	private String bulletinRigImplStatus;
	
	private Date bulletinRigImplStatusDt;
	
	private List<OgDrillBulletinRigAudit> bulletinAuditLog;
}
