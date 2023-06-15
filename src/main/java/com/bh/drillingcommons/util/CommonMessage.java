package com.bh.drillingcommons.util;

import com.bh.drillingcommons.enumerators.Constants;

public final class CommonMessage {

	private CommonMessage() {
		
	}
	
	 public static final String COURSE_WAITLIST_REQUEST_CONFIRMATION="\r\n NOTE: This is NOT a confirmation of your registration."+ 
	 		"This course is currently full. We will monitor the roster for cancellations up to 7 days before the course."+ 
	 		"You will be notified if a seat becomes available. Wait list requests are processed in the order they are received."+
	 		"\r\nPlease review the prequisites and PPE requirements for this course, as they will be strictly followed in the processing "+ 
	 										"and delivery of the training. ";
	 
	 public static final String COURSE_REG_REQUEST_CONFIRMATION="\r\n NOTE: This is NOT a confirmation of your registration."
	 		+ "\r\n We will review your request and you will receive confirmation in the next two business days."; 
	 
	 public static final String COURSE_REQ_FINAL_MSG="\r\nThe Drilling Training Team\r\nPowered by Baker Hughes\r\nhttps://www.engagedrilling.com/"+
			 "\r\nPlease do not reply to this email. For any questions please contact your GE Training Administrator at"+
			 Constants.TRAINING_TEAM_CONTACT_PHONE.getValue();
	 
	 public static final String COURSE_REQ_REG_EXCEPTION="Course Registration request can't be Empty"; 
	 public static final String COURSE_REQ_WAITLIST_EXCEPTION="Course Waitlist request cann't be Empty";
	 		
	 public static final String INVALID_FILE_EXTENSION_TYPE = "Invalid extension of File Name" ;
	 
}
