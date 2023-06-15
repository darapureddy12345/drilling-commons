package com.bh.drillingcommons.models;

import java.io.Serializable;

import lombok.Data;

@Data
public class CourseEnrollee implements Serializable{

	private static final long serialVersionUID = -6475200179904041393L;
	
	private String name;
	private String email;
	private String id;
}
