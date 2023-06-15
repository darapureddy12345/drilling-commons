package com.bh.drillingcommons.models;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CourseRequest  implements Serializable {

	private static final long serialVersionUID = -3938717501065676781L;
	
	private String requestorName;
	private String requestorEmail;
	private Long customerId;
	private String customerName;
	private String rigName;
	private String otherRigName;
	private Long courseId;
	private String courseName;
	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date preferredStartDate; 
	@JsonFormat(pattern = "MM/dd/yyyy")
	private Date preferredEndDate; 
	private String courseType;
	private Long numberOfStudents;
	private String preferredLocation;
	private String courseDesc;
	private Course course;
}
