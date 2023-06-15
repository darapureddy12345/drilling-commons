package com.bh.drillingcommons.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.Data;

@Entity()
@Data
public class Course implements Serializable {

	private static final long serialVersionUID = -7513780443709157208L;

	@Id
	private Long id;
	
	private String name;
	
	private String group;
	@Transient
	private String status;
	
	private String description;
	
	private String audience;
	
	private String prereq;
	
	private String ppereq;
	@Transient
	private List<Course> prereqCourses;
	
	private Long length;
	@Transient
	private Long totalSessions;
	
	private Long customerId;
	
	private String offsiteDelivery;	
}
