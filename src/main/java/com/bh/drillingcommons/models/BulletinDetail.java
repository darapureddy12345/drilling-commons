package com.bh.drillingcommons.models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bh.drillingcommons.util.DCUtils;

import lombok.Data;

/**
 * {@link BulletinDetail} object
 * 
 * @author BH
 *
 */
@Data
public class BulletinDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BulletinDetail.class);
	
	private String bulletinId;
	
	private String bulletinNum;
	
	private String bulletinType;
	
	private String bulletinTypeDesc;
	
	private String bulletinDesc;
	
	private Date bulletinIssueDt;
	
	private String bulletinIssueDtStr;
	
	private Date bulletinLastUpdateDt;
	
	private String bulletinLastUpdateDtStr;
	
	private String lastUpdatedBy;
	
	private String bulletinScope;
	
	private Long documentId;
	
	private String documentName;
	
	private String revision;
	
	private String notifyCustomers;
	
	private String[] customerId;
	
	private String[] rigId;
	
	private String userType;
	
	private String fleetCustomers;
	
	private String mlpBulletin;
	
    private String solutionTypeCode;
    
    private List<RigBulletinDetail> rigBulletinList;
	
	private String bulletinActiveFlag;

	
	/**
	 * @return the bulletinIssueDt
	 */
	public Date getBulletinIssueDt() {
		if(DCUtils.isNullOrEmpty(bulletinIssueDt) && !DCUtils.isNullOrEmpty(bulletinIssueDtStr)) {
			try {
				this.bulletinIssueDt = dateFormatter.parse(bulletinIssueDtStr);
			} catch (ParseException e) {
				LOGGER.error("Exception while parsing to date, Exception : {}, StackTrace : {}", e.getMessage(), e);
			}
		}		
		return bulletinIssueDt;
	}
	
	
	/**
	 * @return the bulletinLastUpdateDt
	 */
	public Date getBulletinLastUpdateDt() {
		if(DCUtils.isNullOrEmpty(bulletinLastUpdateDt) && !DCUtils.isNullOrEmpty(bulletinLastUpdateDtStr)) {
			try {
				this.bulletinLastUpdateDt = dateFormatter.parse(bulletinLastUpdateDtStr);
			} catch (ParseException e) {
				LOGGER.error("Exception while parsing to date, Exception : {}, StackTrace : {}", e.getMessage(), e);

			}
		}
		return bulletinLastUpdateDt;
	}
}
