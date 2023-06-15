package com.bh.drillingcommons.entity.oracle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "OG_DRILL_WEB_NOTIFICATION")
@Data
public class OgDrillWebNotification implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "WEB_NOTIFICATION_ID")
	private BigDecimal id;

	@Column(name = "NOTIF_DETAIL")
	private String notificationDetails;

	@Column(name = "ACTIVE_FLAG")
	private String activeFlag;

	@Column(name = "CRTD_DT")
	private Date createdDate;

	@Column(name = "LST_UPDT_DT")
	private Date lstUpdtDt;

	@Column(name = "Subject")
	private String subject;

	@PrePersist
	public void prePersist() {
		Date now = new Date();
		createdDate = now;
		lstUpdtDt = now;
	}

}
