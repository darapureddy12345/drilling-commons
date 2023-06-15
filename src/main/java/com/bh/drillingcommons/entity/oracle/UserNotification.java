package com.bh.drillingcommons.entity.oracle;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author BH
 * 
 */
@Entity
@Data
public class UserNotification implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "DRILL_NOTIFICATION_ID")
	private String drillNotificationId;

	@Column(name = "NOTIFICATION_TYPE")
	private String notificationType;

	@Column(name = "EVENT_TYPE")
	private String eventType;

	@Column(name = "MARK_AS_READ")
	private String markAsRead;

	@Column(name = "NOTIFICATION_NAME")
	private String notificationName;

	@Column(name = "NOTIFICATION_DESCRIPTION")
	private String notificationDeascription;

	@Column(name = "NOTIFICATION_DATE")
	private String notificationDate;

	@Column(name = "CRTD_BY")
	private String createdBy;

	@Column(name = "CRTD_DT")
	private String createdDate;

	@Column(name = "PARENT_ID")
	private String parentId;

	@Column(name = "SUB_PARENT_ID")
	private String subParentId;
}
