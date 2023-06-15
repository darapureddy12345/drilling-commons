package com.bh.drillingcommons.entity.oracle;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

/**
 * {@link OgDrillBulletinRigAudit} entity object
 * 
 * @author BH
 *
 */
@Entity
@Table(name = "OG_DRILL_BULLETIN_RIG_AUDIT")
@NamedQuery(name = "OgDrillBulletinRigAudit.findAllByDrillBulletinRigId", query = "select a from OgDrillBulletinRigAudit a where a.drillBulletinRigId = ?1 order by a.createdOn desc")
@SequenceGenerator(sequenceName = "OG_DRILL_BULTN_RIG_AUDIT_SEQ", name = "OG_DRILL_BULTN_RIG_AUDIT_SEQ")
@Data
public class OgDrillBulletinRigAudit implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "DRILL_BULLETIN_RIG_AUDIT_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OG_DRILL_BULTN_RIG_AUDIT_SEQ")
	private Long id;

	@Column(name = "DRILL_BULLETIN_RIG_ID")
	private Long drillBulletinRigId;

	@Column(name = "CONFIRMED_BY")
	private String confirmedBy;

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "ATTACHMENT_NAME")
	private String attachmentName;

	@Column(name = "ATTACHMENT_DESC")
	private String attachmentDesc;

	@Column(name = "CRTD_DT ")
	private Date createdOn;

	@Column(name = "BULLETIN_RIG_STAT")
	private String bulletinRigStat;

	@Column(name = "IMPLEMENT_STAT")
	private String implementStat;

	@Column(name = "IMPLEMENTED_DT")
	private Date implementedDt;

	@PrePersist
	public void prePersist() {
		Date now = new Date();
		createdOn = now;
	}
}
