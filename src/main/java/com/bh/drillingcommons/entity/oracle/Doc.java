package com.bh.drillingcommons.entity.oracle;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * {@link Doc} object
 * 
 * @author BH
 *
 */
@Entity
@Data
public class Doc implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "DOC_NUM")
	private String docNum;
	 
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "REVISION_NUM")
	private String revisionNum;
	
	@Column(name = "PO_NUM")
	private String poNum;
	
	@Column(name = "PART_NUM")
	private String partNum;
	
	@Column(name = "SERIAL_NUM")
	private String serialNum;
	
	@Column(name = "ORDER_NUM")
	private String orderNum;
	
	@Column(name = "REFER_ID")
	private Long referId;
	
	@Column(name = "DRILL_CUST_ID")
	private Long customerId;

	@Column(name = "DRILL_CUST_NAME")
	private String customerName;
	
	@Column(name = "RIG_ID")
	private Long rigId;
	
	@Column(name = "DRILL_PROJ_NAME")
	private String projectName;

	@Column(name = "DRILL_PROJ_ID")
	private Long projectId;

	@Column(name = "LST_UPDT_BY")
	private String lastUpdatedBy;

	@Column(name = "CRTD_BY")
	private String createdBy;

	@Column(name = "NOTIFY_EMAIL")
	private String notifyEmail;

	@Column(name = "RIG_NAME")
	private String rigName;
	
	@Column(name = "LST_UPDT_DT")
	private Date updated;
	
	@Column(name = "ORIG_ISSUE_DT")
	private Date created;
	
	@Column(name = "BULLETIN_NUM")
	private String bulletinNum;

	@Column(name = "DRILL_VID_ID")
	private Long videoId;
	
	@Column(name = "DESCRIPTION")
	private String desc;
	
	@Column(name = "DRILL_ORD_DOC_ID") 
	private String drillOrdDocId;
	
	@Column(name= "PROJ_ID")
	private String projId;
	
	@Column(name= "DRILL_PRSNTATN_ID")
	private String drillPresentationId;
	
	@Column(name= "PART_REV")
	private String partRev;
	
	@Column(name= "COC_SR_NO")
	private String cocServiceNo;
}
