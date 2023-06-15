package com.bh.drillingcommons.entity.oracle;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "OG_DRILL_TRAINING_CONTACT")
public class TrainingContact implements Serializable {

	private static final long serialVersionUID = -7541772112716881195L;

	@Column(name = "TRAINING_CONTACT_ID")
	@Id
	private long contactId;
	
	@Column(name = "SSO_ID")
	private String ssoId;
	
	@Column(name = "FIRST_NAME")
	private String firstName;
	
	@Column(name = "LAST_NAME")
	private String lastName;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "PHONE")
	private String contactNumber;
	
	@Column(name = "PHONE_MOBILE")
	private String phoneNumber;
	
	@Column(name = "DESIGNATION")
	private String designation;
	
	@Column(name = "ACTV_FLG")
	private String activeFlag;
	
	@Column(name = "CONTACT_TYPE")
	private String contactType;
	
	@Column(name = "REMARK")
	private String remark;
	
	@Column(name = "PROFILE_IMAGE")
	private String profileImage;
	
	@Column(name = "CRTD_BY")
	private String createdBy;
	
	@Column(name = "LST_UPDT_BY")
	private String updatedBy;
	
	@Column(name = "CRTD_DT")
	private Date createdDate;
	
	@Column(name = "LST_UPDT_DT")
	private Date updatedDate;

}
