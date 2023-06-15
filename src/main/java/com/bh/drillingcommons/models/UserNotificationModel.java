package com.bh.drillingcommons.models;

import java.io.Serializable;

import lombok.Data;

/**
 * @author BH
 * 
 */
@Data
public class UserNotificationModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String drillNotificationId;

	private String notificationType;

	private String eventType;

	private String markAsRead;

	private String notificationName;

	private String notificationDescription;

	private String notificationDate;

	private String createdBy;

	private String createdDate;

	private String parentId;

	private String subParentId;

	private String rigName;
	
	private String custName;
}
