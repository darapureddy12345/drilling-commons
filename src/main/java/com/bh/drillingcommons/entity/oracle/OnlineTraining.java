package com.bh.drillingcommons.entity.oracle;
import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
@Table(name = "OG_DRILL_ONLINE_TRAINING_MST")
public class OnlineTraining implements Serializable {

	private static final long serialVersionUID = -7541772112716881195L;

	@JsonIgnore
	@Id
	@Column(name = "ID")
	private long id;
	
	@Column(name = "TRAINING_NAME")
	private String trainingName;
	
	@Column(name = "TRAINING_DIRECTORY")
	private String trainingDirectory;

	@Column(name = "PUBLISH_DATE")
	@JsonFormat(pattern = "dd-MMM-yyyy")
	private LocalDate publishDate;
	
	@JsonIgnore
	@Column(name = "ISACTIVE")
	private String isActive;

}
