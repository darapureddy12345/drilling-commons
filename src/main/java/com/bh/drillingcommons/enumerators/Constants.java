package com.bh.drillingcommons.enumerators;

/**
 * Constants
 * 
 * @author 212769495
 *
 */
public enum Constants {

	OR_FORM_SITE_REGION("ORFormSiteRegion"), CSA_KEY("CsaUploadEmail"),DML_FROM_EMAIL("BHGE.engageDrilling@bakerhughes.com"), //DML_FROM_EMAIL("BHGE.engagedrilling@ge.com"),
	DML_SUBMIT_EMAIL_SUBJECT("ACTION REQUIRED: engageDrilling DML Work Pack Approval Required"),
	DML_APPROVED_EMAIL_SUBJECT("NO ACTION REQUIRED:  engageDrilling DML Work Pack Approved"),
	DML_INPROGRESS_EMAIL_SUBJECT("ACTION REQUIRED:  engageDrilling DML Work Pack Returned With Approver Comments"),
	TO_LIST_FOR_DML_EMAIL_NOTIFICATION("DigitalMaintenanceLibraryApproverId"), DML_STATUS_INPROGRESS("inProgress"),
	DML_STATUS_SUBMITTED("submitted"), DML_STATUS_APPROVED("approved"), COLOR_BLUE("#2ba6cb"), COLOR_GREY("#BFC2C5"),
	COLOR_INDIGO("#3B73B9"), COLOR_GREEN("#68BC68"), LOOKUP_KEY("lookup"),ACTIVE_FLAG_Y("Y"),
	USERID_BADREQUEST_MESSAGE("Error: Invalid value <empty string> for : userId"),
	FEEDBACK_TO_MAIL("FeedbackNotificationToMail"), FEEDBACK_FROM_MAIL("FeedbackNotificationFromMail"),
	FEEDBACK_EMAIL_SUBJECT("End-User feedback posted in engageDrilling"),
	FEEDBACK_EMAIL_BODY("The following end-user feedback has been received at the engageDrilling Portal."),
	FEEDBACK_USER("Feedback entered by : "), NOREPLY_EMAIL("NoReplyEmail"),

	TRAINING_TEAM_CONTACT_PHONE("123-456-7890"),
	TRAINING_REGISTRATION_CC("TeamITO"), TRAINING_REGISTRATION_BCC("TrainingRegistrationBcc"),
	TRAINING_REQUEST_TO("TrainingRequestTo"), TRAINING_REGISTRATION_TO("TrainingRegistrationTo"),
	TRAINING_TEAM_CONTACT_NO("TrainingTeamContactNo"),
	TRAINING_COURSE_REGISTRATION_REQUEST("Request for Registration"),
	TRAINING_COURSE_WAITLIST_REQUEST("Request for Wait List"), 
	COURSE_REQUEST_TYPE_REG("registration"),
	COURSE_REQUEST_TYPE_WAITLIST("waitlist"), EMAIL_CONTACT("Contact"),
	TRAINING_COURSE_SPECIAL_REQUEST("Request for a Custom Course"),
	TRAINING_COURSE_STANDARD_REQUEST("Request for a Session"),
	/*******************************************/
	PDF("application/pdf"), DOC("application/msword"),
	DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
	PPT("application/vnd.ms-powerpoint"), XLS("application/vnd.ms-excel"),
	XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
	PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"), PNG("image/png"),
	ZIP("application/x-zip-compressed"), TEXT("text/plain"), TXT("text/plain"),

	CUSTOMVIDEOEMAIL("CustomVideoEmail"), INSP_EMAIL_BCC("igatedrillexpteam@bakerhughes.com"), // INSP_EMAIL_BCC("igatedrillexpteam@ge.com"),
	FEEDBACK_POST_EMAIL_FROM("no-reply.dcexp@bakerhughes.com"), DRILL_SHARED_VIDEO_SEQ("DRILL_SHARED_VIDEO_SEQ"), // FEEDBACK_POST_EMAIL_FROM("no-reply@ge.com"),
	DRILL_VIDEO_REQ_SEQ("DRILL_VIDEO_REQ_SEQ"),

	TEAM_ITO("TeamITO"), TRAINING_VIDEO_EMAIL("TrainingVideoEmail"), EMAIL_FROM("no-reply.dcexp@bakerhughes.com"), // EMAIL_FROM("no-reply@ge.com"),

	/****************** bulletin enums ******************/

	BULLETIN_TO_EMAIL("BulletinToEmail"), BULLETIN_TYPE("BulletinType"),
	TOKEN("F365RRRBD027BA93"),


	/******************** Chatbot Messages ********************/

	CHATBOT_FEEDBACK_EMAIL_SUBJECT("CHATBOT:End-User feedback posted in engageDrilling"), CHATBOT_FEEDBACK_SUCCESS(
			"We have submitted this issue on your behalf. We apologise for the inconvenience and will provide you an update once the issue is resolved."), BUSINESS_CC_CHATBOT("BusinessCcChatbot");


	private String value;

	public String getValue() {
		return value;
	}

	private Constants(String value) {
		this.value = value;
	}

}
