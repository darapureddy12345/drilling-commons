package com.bh.drillingcommons.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Data
public class CourseSchedule implements Serializable {

	
	private static final long serialVersionUID = 5435964915638508460L;

	@Id
	@Column(name="PID")
	private Long pid;
	
	@Column(name="COURSEID")
	private Long courseId;	
	
	@JsonFormat(pattern = "MM/dd/yyyy")
	@Column(name="STARTDT")
	private Date startDate;
	
	@JsonFormat(pattern = "MM/dd/yyyy")
	@Column(name="ENDDT")
	private Date endDate;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="CITYANDSTATE")
	private String cityAndState;
	
	@Column(name="COUNTRY")
	private String country;
	
	@Column(name="CUSTID")
	private String custId;
	
	@Column(name="RIGNAME")
	private String rigName;
	
	@Column(name="COURSENAME")
	private String courseName;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="CATEGORY")
	private String category;
	
	@Column(name="INSTCRFNAME")
	private String instuctorFname;
	
	@Column(name="INSTCRLNAME")
	private String instuctorLname;
	
	@Column(name="CLASSDATE")
	private String classDate;
	
	
}
