package com.bh.drillingcommons.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CourseRegistration implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String requestorName;
	private String requestorEmail;
	private Long customerId;
	private String customerName;
	private String poNumber;
	private String rigName;
	private String otherRigName;
	private String supervisorName;
	private String courseName;
	private String courseDesc;
	private Long scheduleId; 
	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date startDate; 
	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date endDate; 
	private String registrationType;
	private List<CourseEnrollee> enrollees;
	private Course course;
	private CourseSchedule schedule;
	
}