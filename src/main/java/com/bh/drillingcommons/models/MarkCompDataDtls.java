package com.bh.drillingcommons.models;
import java.io.Serializable;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;

@Data
public class MarkCompDataDtls implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String docType;
	
	private String description;
	
    private String revisionNum;
	
	@JsonFormat(pattern = "MM/dd/yyyy") 
	@JsonDeserialize(using= LocalDateDeserializer.class)
	@JsonSerialize(using= LocalDateSerializer.class) 
	private LocalDate issueDt;
	
	private String documentName;
	
	private String scope;
	
	private String notifyCustomers;
	
    private String fleetDoc;
    
    private String isActive;
	
    private String createdBy;

	@JsonFormat(pattern = "MM/dd/yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate createdDate;
	
	private String lastUpdatedBy;

	@JsonFormat(pattern = "MM/dd/yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate lastUpdateDate;
	
	private Long docId;
	
}