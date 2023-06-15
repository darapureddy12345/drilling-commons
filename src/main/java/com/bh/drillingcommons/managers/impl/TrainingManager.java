package com.bh.drillingcommons.managers.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.drillingcommons.config.EmailUtil;
import com.bh.drillingcommons.entity.oracle.Lookup;
import com.bh.drillingcommons.entity.oracle.OnlineTraining;
import com.bh.drillingcommons.entity.oracle.TrainingContact;
import com.bh.drillingcommons.enumerators.Constants;
import com.bh.drillingcommons.exceptions.BadRequestException;
import com.bh.drillingcommons.exceptions.SystemException;
import com.bh.drillingcommons.managers.ICommonManager;
import com.bh.drillingcommons.managers.ITrainingManager;
import com.bh.drillingcommons.models.CourseEnrollee;
import com.bh.drillingcommons.models.CourseRegistration;
import com.bh.drillingcommons.models.CourseRequest;
import com.bh.drillingcommons.models.CustomVideo;
import com.bh.drillingcommons.models.Email;
import com.bh.drillingcommons.models.ShareVideo;
import com.bh.drillingcommons.ras.ICommonResourceAccessor;
import com.bh.drillingcommons.ras.INotificationResourceAccessor;
import com.bh.drillingcommons.ras.ITrainingResourceAccessor;
import com.bh.drillingcommons.repository.IOnlineTrainingRepo;
import com.bh.drillingcommons.util.CommonMessage;
import com.bh.drillingcommons.util.DCUtils;
import com.bh.drillingcommons.util.MessageUtil;

@Service
public class TrainingManager implements ITrainingManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrainingManager.class);

	private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	@Autowired
	private ICommonResourceAccessor commonAccessor;

	@Autowired
	private ITrainingResourceAccessor trainingAccessor;

	@Autowired
	private INotificationResourceAccessor notificationAccessor;

	@Autowired
	private EmailUtil emailUtil;

	@Autowired
	private IOnlineTrainingRepo onlineTrainingRepo;
	
	/**
	 * @see {@link ITrainingManager#requestcourseRegistration(courseRegDetailsistration)}
	 */
	@Override
	public String requestCourseRegistration(CourseRegistration courseRegDetails, Constants courseRequestType) {
		String noReplyEmail = null;
		String emailReqList = null;
		String trainingRegistrationTo = "";
		String[] emailArray = null;
		String[] emailList = null;
		String[] ccEmail = null;
		String[] bccEmail = null;
		
		try {
			if (courseRegDetails != null && !courseRegDetails.getRequestorName().isEmpty()
					&& courseRegDetails.getRequestorName() != null) {
				List<Lookup> lookupNoReply = commonAccessor.getLookupValues(Constants.NOREPLY_EMAIL.getValue());
				for(Lookup lookup : lookupNoReply){
					noReplyEmail = lookup.getValue();
				}
				
				List<Lookup> registationLookups = commonAccessor.getLookupValues(Constants.TRAINING_REGISTRATION_TO.getValue());
				for(Lookup lookup : registationLookups){
					if (trainingRegistrationTo.isEmpty()) {
						trainingRegistrationTo = lookup.getValue();
					} else {
						trainingRegistrationTo = emailArray + "," + lookup.getValue();
					}
				}
				
				StringBuilder body = formEmailContent(courseRegDetails, courseRequestType);
								
				String emailCC = getCCEmail(ccEmail);
				
				ccEmail = DCUtils.convertEmailIdStringToArray(emailCC);
				emailArray = DCUtils.convertEmailIdStringToArray(trainingRegistrationTo);
				emailReqList = courseRegDetails.getRequestorEmail();
				emailList = DCUtils.convertEmailIdStringToArray(emailReqList);
				bccEmail = ArrayUtils.addAll(ccEmail, emailArray);
				notificationAccessor.mailSender(emailList, null, bccEmail, noReplyEmail, courseRequestType.getValue(), body.toString(), true);
			}
			else{
				throw new IllegalArgumentException("Course Registration request cann't be Empty");
			}			

			}catch (Exception exception) {
				LOGGER.error("Exception while registering/waitlisting course {}", exception);
				throw new SystemException(exception.getMessage(), exception);
			}
		
		return "success";
	}

	private String getCCEmail(String[] ccEmail) {
		String emailCC="";
		List<Lookup> lookups = commonAccessor.getLookupValues(Constants.TEAM_ITO.getValue());
		for(Lookup lookup : lookups){
			if (emailCC.isEmpty()) {
				emailCC = lookup.getValue();
			} else {
				emailCC = ccEmail + "," + lookup.getValue();
			}
		}
		return emailCC;
	}

	@Override
	public String requestCustomCourse(CourseRequest courseRequest) {
		String noReplyEmail = null;
		
		try {
			if (courseRequest != null && !courseRequest.getRequestorName().isEmpty()
					&& courseRequest.getRequestorName() != null) {
				List<Lookup> lookupNoReply = commonAccessor.getLookupValues(Constants.NOREPLY_EMAIL.getValue());
				for (Lookup lookup : lookupNoReply) {
					noReplyEmail = lookup.getValue();
				}
				
				List<Lookup> lookups = commonAccessor.getLookupValues(Arrays.asList(
						Constants.TRAINING_REQUEST_TO.getValue(), Constants.TRAINING_REGISTRATION_CC.getValue()));
				
				StringBuilder body = formCustomRequestBody(courseRequest);
				
				List<String> ccEmailLst = lookups.stream()
						.filter(p -> p.getType().equalsIgnoreCase(Constants.TRAINING_REGISTRATION_CC.getValue()))
						.map(p -> p.getValue()).collect(Collectors.toList());
				
				List<String> traingReqEmailTo = lookups.stream()
						.filter(p -> p.getType().equalsIgnoreCase(Constants.TRAINING_REQUEST_TO.getValue()))
						.map(p -> p.getValue()).collect(Collectors.toList());
				
				
				List<String> bccList = Stream.concat(ccEmailLst.stream(), traingReqEmailTo.stream())
                        .collect(Collectors.toList());
				
				notificationAccessor.sendEmail(noReplyEmail, courseRequest.getRequestorEmail(),
						null, getCommaSeparatedValues(bccList, false),
						("SPECIAL".equalsIgnoreCase(courseRequest.getCourseType()))
								? Constants.TRAINING_COURSE_SPECIAL_REQUEST.getValue()
								: Constants.TRAINING_COURSE_STANDARD_REQUEST.getValue(),
						body.toString());
			} else {
				throw new IllegalArgumentException("Course request cann't be Empty");
			}
			return "success";

		} catch (Exception exception) {
			LOGGER.error("Exception while requesting custom course {}", exception);
			throw new SystemException(exception.getMessage(), exception);

		}

	}

	private StringBuilder formCustomRequestBody(CourseRequest courseRequest) {
		String firstName = "";
		String lastName = "";
		String contactNumber = "";
		List<TrainingContact> trainingContact = trainingAccessor.getTrainingContact(Constants.EMAIL_CONTACT.getValue());
		if (trainingContact != null && !trainingContact.isEmpty()) {
			firstName = trainingContact.get(0).getFirstName();
			lastName = trainingContact.get(0).getLastName();
			contactNumber = trainingContact.get(0).getContactNumber();
		}

		StringBuilder body = new StringBuilder("Hi " + courseRequest.getRequestorName() + ",</br>");
		if (isNullOrEmpty(courseRequest.getCourse()))
			body.append("</br>Thank you for your request for an additional session of '"
					+ ((isNullOrEmpty(courseRequest.getCourseName())) ? "Custom Course" : courseRequest.getCourseName())
					+ " ("
					+ (isNullOrEmpty(courseRequest.getCourseDesc()) ? "Not Specified" : courseRequest.getCourseDesc())
					+ ")'.");
		else
			body.append("</br>Thank you for your request for an additional session of '"
					+ courseRequest.getCourse().getName() + "'.");

		body.append(MessageUtil.EMAIL_BREAK);
		body.append("</br>Rig Name : " + ((isNullOrEmpty(courseRequest.getRigName())) ? courseRequest.getOtherRigName()
				: courseRequest.getRigName()));
		if ("OTHER".equalsIgnoreCase(courseRequest.getRigName()))
			body.append(" (" + courseRequest.getOtherRigName() + ")");
		body.append("</br>Preferred Start Date : "
				+ (isNullOrEmpty(courseRequest.getPreferredStartDate()) ? "Not Specified"
						: dateFormat.format(courseRequest.getPreferredStartDate())));
		body.append(
				"</br>Preferred End Date : " + (isNullOrEmpty(courseRequest.getPreferredEndDate()) ? "Not Specified"
						: dateFormat.format(courseRequest.getPreferredEndDate())));
		body.append(
				"</br>Preferred Location : " + (isNullOrEmpty(courseRequest.getPreferredLocation()) ? "Not Specified"
						: courseRequest.getPreferredLocation()));
		body.append(
				"</br>Number of Students : " + (isNullOrEmpty(courseRequest.getNumberOfStudents()) ? "Not Specified"
						: courseRequest.getNumberOfStudents()));
		body.append("</br>Course Description : "
				+ (isNullOrEmpty(courseRequest.getCourseDesc()) ? "Not Specified" : courseRequest.getCourseDesc()));

		body.append(MessageUtil.EMAIL_BREAK);

		body.append("</br>Our Training team will evaluate your request and get in touch with you within 48 hours.");

		body.append(MessageUtil.EMAIL_BREAK);
		body.append("</br>Best regards,");

		body.append(MessageUtil.EMAIL_BREAK);
		body.append("</br>Your engageDrilling Support Team");
		body.append("</br>Powered by Baker Hughes</br>");
		body.append(System.getenv(MessageUtil.APP_URL));

		body.append(MessageUtil.EMAIL_BREAK);
		body.append("</br>Please do not reply to this email. For any questions related to this request, please contact "
				+ firstName + " " + lastName + " at " + contactNumber);
		return body;
	}

	/**************** Private Methods **************************************/

	private StringBuilder formEmailContent(CourseRegistration courseRegDetails, Constants courseRequestType) {

		String location = String.format("%s%s",
				(isNullOrEmpty((courseRegDetails.getSchedule().getCityAndState())) ? ""
						: String.format("%s, ", courseRegDetails.getSchedule().getCityAndState())),
				courseRegDetails.getSchedule().getCountry());

		StringBuilder body = new StringBuilder();
		formWelcomeContent(courseRegDetails, location, body);

		formCourseDetailContent(courseRegDetails, body);

		body.append(courseRequestType.equals(Constants.COURSE_REQUEST_TYPE_REG)
				? CommonMessage.COURSE_REG_REQUEST_CONFIRMATION
				: CommonMessage.COURSE_WAITLIST_REQUEST_CONFIRMATION);

		formPrerequisiteContent(courseRegDetails, body);
		
		formEndContent(body);

		return body;
	}

	private void formEndContent(StringBuilder body) {
		String trainingTeamContactNumber = "";
		List<Lookup> contactLookups = commonAccessor.getLookupValues(Constants.TRAINING_TEAM_CONTACT_NO.getValue());
		for(Lookup lookup : contactLookups){
			trainingTeamContactNumber = lookup.getValue();
		}
		
		body.append("</br>The Drilling Training Team");
		body.append("</br>Powered by Baker Hughes</br>");
		body.append(System.getenv(MessageUtil.APP_URL));

		body.append("</br>");
		body.append("</br>Please do not reply to this email. For any questions please contact your HMH Training Administrator at " + trainingTeamContactNumber);

		
	}

	private void formCourseDetailContent(CourseRegistration courseRegDetails, StringBuilder body) {
		formCourseEnrolleeContent(courseRegDetails, body);

		body.append("</br>PO Number: ");
		body.append(isNullOrEmpty(courseRegDetails.getPoNumber()) ? "" : courseRegDetails.getPoNumber());
		if (!(isNullOrEmpty(courseRegDetails.getRigName()) && isNullOrEmpty(courseRegDetails.getOtherRigName()))) {
			body.append("</br>Rig: ");
			body.append("Other".equalsIgnoreCase(courseRegDetails.getRigName()) ? courseRegDetails.getOtherRigName()
					: courseRegDetails.getRigName());
		}
		body.append("</br>Supervisor: ");
		body.append(isNullOrEmpty(courseRegDetails.getSupervisorName()) ? "" : courseRegDetails.getSupervisorName());

	}

	private void formCourseEnrolleeContent(CourseRegistration courseRegDetails, StringBuilder body) {
		if (!isNullOrEmpty(courseRegDetails.getEnrollees())) {
			for (CourseEnrollee enrollee : courseRegDetails.getEnrollees()) {

				if (!(isNullOrEmpty(enrollee.getName()) && isNullOrEmpty(enrollee.getEmail()))) {
					body.append(MessageUtil.EMAIL_BREAK);
					body.append((isNullOrEmpty(enrollee.getName()) ? "Not Provided" : enrollee.getName())
							+ " (" + (isNullOrEmpty(enrollee.getEmail()) ? "Email Not Provided" : enrollee.getEmail())
							+ ")");
				}
			}
		}
	}

	private void formPrerequisiteContent(CourseRegistration courseRegDetails, StringBuilder body) {
		if (null != courseRegDetails.getCourse()) {
			body.append(MessageUtil.EMAIL_BREAK);
			body.append("</br>Prerequisites required: "
					+ (isNullOrEmpty(courseRegDetails.getCourse().getPrereqCourses()) ? "None"
							: courseRegDetails.getCourse().getPrereqCourses()));
			body.append(MessageUtil.EMAIL_BREAK);
			body.append("</br>PPE required: " + (isNullOrEmpty(courseRegDetails.getCourse().getPpereq()) ? "None"
					: courseRegDetails.getCourse().getPpereq()));
			body.append(MessageUtil.EMAIL_BREAK);
		} else {
			body.append(MessageUtil.EMAIL_BREAK);
			body.append(courseRegDetails.getCourseName());
			body.append(MessageUtil.EMAIL_BREAK);
			body.append(MessageUtil.EMAIL_BREAK);
			body.append(courseRegDetails.getCourseDesc());
		}
	}

	private void formWelcomeContent(CourseRegistration courseRegDetails, String location, StringBuilder body) {
		body.append("Hi " + courseRegDetails.getRequestorName() + ",</br>");
		body.append("</br></br>Thank you for requesting the following student registration for ");
		body.append(isNullOrEmpty(courseRegDetails.getCourseName()) ? "" : courseRegDetails.getCourseName());
		body.append("scheduled for ");
		body.append(isNullOrEmpty(courseRegDetails.getSchedule().getStartDate()) ? "Not Specified"
				: dateFormat.format(courseRegDetails.getSchedule().getStartDate()));
		body.append(" through ");
		body.append(isNullOrEmpty(courseRegDetails.getSchedule().getStartDate()) ? ""
				: dateFormat.format(courseRegDetails.getSchedule().getEndDate()));
		body.append(" in ");
		body.append(location);
		body.append("</br></br>");
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNullOrEmpty(Object obj) {
		boolean result = false;
		if (obj != null) {
			if (obj instanceof String) {
				if (((String) obj).trim().equalsIgnoreCase("")) {
					result = true;
				}
			} else if (obj instanceof Collection<?>) {
				if (((Collection<?>) obj).isEmpty()) {
					result = true;
				}
			} else if (obj instanceof Map<?, ?>) {
				if (((Map<?, ?>) obj).isEmpty()) {
					result = true;
				}
			}
		} else {
			result = true;
		}
		return result;
	}

	/**
	 * Get {@link String} object from {@link List<String>,boolean}
	 * 
	 * @param objectList,usedInQuery {@link List<String>,boolean}
	 * 
	 * @return Returns a {@link String}
	 */
	private String getCommaSeparatedValues(List<String> objectList, boolean usedInQuery) {
		StringBuilder list = new StringBuilder();
		if (usedInQuery)
			objectList.forEach(object -> list.append(",'").append(object).append("'"));
		else
			objectList.forEach(object -> list.append(',').append(object));
		return list.toString().length() > 0 ? list.toString().substring(1) : "";

	}

	/**
	 * @see {@link ICommonManager#shareVideo(ShareVideo)}
	 */
	@Override
	public String shareVideo(ShareVideo shareVideo) {
		String response = null;
		try {
			if (shareVideo != null) {
				String result = trainingAccessor.shareVideo(shareVideo);
				if (result != null) {
					String[] bccEmail = DCUtils.convertStringToArray(Constants.INSP_EMAIL_BCC.getValue());
					String[] toEmail = DCUtils.convertStringToArray(shareVideo.getRecommendedTo());
					Email email = new Email(Constants.FEEDBACK_POST_EMAIL_FROM.getValue(), null, null, bccEmail,
							shareVideo.getEmailSubject(), shareVideo.getEmailBody());
					emailUtil.constructAndSendEmail(email, toEmail, true);
					response = MessageUtil.SUCCESS;
				}
			} else {
				throw new BadRequestException("Share Video content can't be Empty");
			}
		} catch (BadRequestException e) {
			throw new BadRequestException(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("Exception while Posting request for Share Video, Exception : {}, StackTrace : {}",
					e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return response;
	}

	/**
	 * @see {@link ICommonManager#requestCustomVideo(CustomVideo)}
	 */
	@Override
	public String requestCustomVideo(CustomVideo customVideo) {
		String response = null;
		String[] fromEmailArray = null;
		String fromEmail = null;
		
		try {
			if (customVideo != null) {
				String result = trainingAccessor.requestCustomVideo(customVideo);
				if (result != null) {
					List<Lookup> lookups = commonAccessor
							.getLookupValues(Arrays.asList(Constants.NOREPLY_EMAIL.getValue(),
									Constants.TEAM_ITO.getValue(), Constants.TRAINING_VIDEO_EMAIL.getValue()));

					fromEmailArray = getStringArrayFromList(lookups, Constants.NOREPLY_EMAIL.getValue());
					List<String> ccList = lookups.stream()
							.filter(p -> p.getType().equalsIgnoreCase(Constants.TEAM_ITO.getValue()))
							.map(p -> p.getValue()).collect(Collectors.toList());
					List<String> videoList = lookups.stream()
							.filter(p -> p.getType().equalsIgnoreCase(Constants.TRAINING_VIDEO_EMAIL.getValue()))
							.map(p -> p.getValue()).collect(Collectors.toList());
					
					List<String> bccList = Stream.concat(ccList.stream(), videoList.stream())
	                        .collect(Collectors.toList());
					
					if (fromEmailArray != null && fromEmailArray.length > 0)
						fromEmail = fromEmailArray[0];
					
					String subject = MessageUtil.CUSTOM_VIDEO_REQUEST_EMAIL_SUBJECT;
					String body = getRequestCustomVideoEmailBody(customVideo);
					
					notificationAccessor.sendEmail(fromEmail, customVideo.getRequestorEmail(), null, getCommaSeparatedValues(bccList, false), subject, body);
					
					response = MessageUtil.SUCCESS;
				}
			} else {
				throw new BadRequestException("request Video content can't be Empty");
			}
		} catch (BadRequestException e) {
			throw new BadRequestException(e.getMessage(), e);
		} catch (Exception ex) {
			LOGGER.error("Exception while posting request for Custom Video, Exception : {}, StackTrace : {}",
					ex.getMessage(), ex);
			throw new SystemException(ex.getMessage(), ex);
		}
		return response;
	}

	/***************** Private Methods *****************************/

	/**
	 * Get Request Custom Video Email Body
	 * 
	 * @param customVideo {@link CustomVideo}
	 * 
	 * @return Returns a {@link String}
	 */
	private String getRequestCustomVideoEmailBody(CustomVideo customVideo) {
		return "<html> " + "<head>" + "</head>" + "<body style='font-family:calibri;'>" + "Hi "
				+ customVideo.getRequestorFirstName() + " " + customVideo.getRequestorLastName() + "," + "<br/><br/>"
				+ "Thank you for your request that the following custom video be created for engageDrilling."
				+ "<br/><br/>" + "Custom video request category : " + customVideo.getVideoCategoryDesc() + "<br/>"
				+ "Custom video description : " + customVideo.getVideoDesc() + "<br/><br/>"
				+ "Our Engineering and Training Media teams will evaluate your request and respond within seven business days."
				+ "<br/><br/>" + "Best regards," + "<br/><br/>" + "The Drilling Training Team" + "<br/>"
				+ "Powered by Baker Hughes" + "<br/>" + System.getenv(MessageUtil.APP_URL) + "<br/><br/>"
				+ "Please do not reply to this email.";
	}

	/**
	 * Get String Array from ArrayList using lookup type as filter key
	 * 
	 * @param lookups {@link List<Lookup>}
	 * 
	 * @param key     {@link String}
	 * @return Returns a {@link String[]}
	 */
	private String[] getStringArrayFromList(List<Lookup> lookups, String key) {
		return lookups.stream().filter(p -> p.getType().equalsIgnoreCase(key)).map(p -> p.getValue())
				.toArray(String[]::new);
	}

	@Override
	public List<OnlineTraining> getOnlineTraining() {
		try {
			return onlineTrainingRepo.getOnlineTraining();
		}catch(Exception ex) {
			LOGGER.error("Exception while getting List of available Online Training, Exception : {}, StackTrace : {}",ex.getMessage(), ex);
			throw new SystemException(ex.getMessage(), ex);						
		}
	}
}
