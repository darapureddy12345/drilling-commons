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

/**
 * {@link 
 * @author BH
 *
 */
@Entity
@Table(name = "OG_DRILL_USER_VIEW_PREFERENCE")
@SequenceGenerator(sequenceName="OG_DRILL_USER_VIEW_PREF_SEQ", name="og_drill_user_view_pref_seq")
@Data
public class OgDrillUserEula implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "user_view_pref_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OG_DRILL_USER_VIEW_PREF_SEQ")
	private BigDecimal userViewPrefId;

	@Column(name="USER_ID")
	private long userId;
	
	@Column(name = "VIEW_OBJECT")
	private String viewObj;
	
	@Column(name = "PROP_KEY")
	private String propKey;
	
	@Column(name = "PROP_VALUE")
	private String propValue;
	
	@Column(name = "UPDATE_DT")
	private Date updatedDate;
	
	@Column(name = "CREATE_DT")
	private Date createdDate;
	
	@Column(name = "USER_ID1")
	private String userId1;
	
	@PrePersist
	public void prePersist() {
		Date now = new Date();
		createdDate = now;
		updatedDate = now;
	}
}
