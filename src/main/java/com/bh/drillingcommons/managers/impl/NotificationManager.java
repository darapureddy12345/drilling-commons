package com.bh.drillingcommons.managers.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bh.drillingcommons.config.EmailUtil;
import com.bh.drillingcommons.entity.oracle.Lookup;
import com.bh.drillingcommons.entity.oracle.OgDrillWebNotification;
import com.bh.drillingcommons.enumerators.Constants;
import com.bh.drillingcommons.exceptions.BadRequestException;
import com.bh.drillingcommons.exceptions.SystemException;
import com.bh.drillingcommons.managers.INotificationManager;
import com.bh.drillingcommons.models.BulletinDetail;
import com.bh.drillingcommons.models.DmlModel;
import com.bh.drillingcommons.models.Email;
import com.bh.drillingcommons.models.Feedback;
import com.bh.drillingcommons.models.MarkCompDataDtls;
import com.bh.drillingcommons.models.NotificationRelease;
import com.bh.drillingcommons.models.User;
import com.bh.drillingcommons.models.UserNotificationModel;
import com.bh.drillingcommons.ras.ICommonResourceAccessor;
import com.bh.drillingcommons.ras.INotificationResourceAccessor;
import com.bh.drillingcommons.util.DCQueries;
import com.bh.drillingcommons.util.DCUtils;
import com.bh.drillingcommons.util.MessageUtil;


@Service
public class NotificationManager implements INotificationManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationManager.class);

	@Autowired
	private ICommonResourceAccessor commonAccessor;

	@Autowired
	private INotificationResourceAccessor notificationResourceAccessor;

	@Autowired
	private EmailUtil emailUtil;

	@Autowired
	public JavaMailSender emailSender;

	/**
	 * @see {@link INotificationManager#getServiceLocations(String)}
	 */
	@Override
	public Map<String, String> removeNotification(String notfnId) {
		if (StringUtils.isEmpty(notfnId))
			throw new BadRequestException("Error: Invalid value <empty string> for : notfnId");
		try {
			this.notificationResourceAccessor.removeNotification(notfnId);
			Map<String, String> result = new HashMap<>();
			result.put(MessageUtil.RESULT, MessageUtil.SUCCESS);
			return result;
		} catch (BadRequestException bre) {
			throw bre;
		} catch (Exception e) {
			LOGGER.error("Exception while removing notification {}", e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link INotificationManager#removeAllNotification(String)}
	 */
	@Override
	public Map<String, String> removeAllNotification(String userId) {
		if (StringUtils.isEmpty(userId))
			throw new BadRequestException(Constants.USERID_BADREQUEST_MESSAGE.getValue());
		try {
			this.notificationResourceAccessor.removeAllNotification(userId);
			Map<String, String> result = new HashMap<>();
			result.put(MessageUtil.RESULT, MessageUtil.SUCCESS);
			return result;
		} catch (BadRequestException bre) {
			throw bre;
		} catch (Exception e) {
			LOGGER.error("Exception while removing all notifications {}", e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link INotificationManager#getNotificationList(String,String)}
	 */
	@Override
	public Map<String, List<OgDrillWebNotification>> getNotificationList(String userId, String readOrNot) {
		try {
			Map<String, List<OgDrillWebNotification>> map = new HashMap<>();
			map.put("body", this.notificationResourceAccessor.getNotificationList(userId, readOrNot));

			return map;
		} catch (Exception exception) {
			LOGGER.error("Exception in getNotificationList {}", exception);
			throw new SystemException(exception.getMessage(), exception);
		}
	}

	/**
	 * @see {@link INotificationManager#markNotificationAsRead(String,String)}
	 */
	@Override
	public Map<String, String> markNotificationAsRead(String userId, String readId) {
		try {
			Map<String, String> notificationMap = new HashMap<>();
			notificationMap.put("notification", this.notificationResourceAccessor.markAsRead(userId, readId));
			return notificationMap;
		} catch (Exception exception) {
			LOGGER.error("Exception in markNotificationAsRead {}", exception);
			throw new SystemException(exception.getMessage(), exception);
		}
	}

	/**
	 * @see {@link INotificationManager#sendEmail()}
	 */
	@Override
	public Map<String, String> sendEmail(Email emailSettings, String emailLookupValue, String rigId) {

		try {
			LOGGER.info("Sending CSA Report email...");

			String rigName = "No_Rig";
			if (emailLookupValue.equals(Constants.CSA_KEY.getValue())) {
				rigName = this.notificationResourceAccessor.getRigName(rigId);
				rigName = rigName.replaceAll("\\s", "_");
			}

			List<Lookup> noReplyEmail = commonAccessor.getLookupValues("NoReplyEmail");
			emailSettings.setFrom(DCUtils.isNullOrEmpty(noReplyEmail)?null:noReplyEmail.get(0).getValue());
			
			// The first email distribution list: IT Team & other important contacts.
			List<Lookup> standardEmailList = this.commonAccessor.getLookupValues(emailLookupValue).stream().distinct()
					.collect(Collectors.toList());
			LOGGER.info("standardEmailList 1st DL : {} ", standardEmailList);
			String[] standardEmailTo = new String[standardEmailList.size()];
			standardEmailList.stream().map(e -> e.getValue()).toArray();
			for (int listindex = 0; listindex < standardEmailList.size(); listindex++) {
				standardEmailTo[listindex] = standardEmailList.get(listindex).getValue();
			}

			// The second email distribution list: addresses specified under each rig.
			String[] rigEmailTo = new String[0];
			if (emailLookupValue.equals(Constants.CSA_KEY.getValue()) && !rigName.equals("CsaUploadEmail_No_Rig")) {
				List<Lookup> rigEmailList = this.commonAccessor
						.getLookupValues(Constants.CSA_KEY.getValue() + "_" + rigName).stream().distinct()
						.collect(Collectors.toList());
				LOGGER.info("rigEmailList 2nd DL : {}", rigEmailList);
				rigEmailTo = new String[rigEmailList.size()];
				for (int listindex = 0; listindex < rigEmailList.size(); listindex++) {
					rigEmailTo[listindex] = rigEmailList.get(listindex).getValue();
				}
			}

			// Final email distribution list.
			String[] emailTo = new String[standardEmailTo.length + rigEmailTo.length];
			System.arraycopy(standardEmailTo, 0, emailTo, 0, standardEmailTo.length);
			if (emailLookupValue.equals(Constants.CSA_KEY.getValue())) {
				System.arraycopy(rigEmailTo, 0, emailTo, standardEmailTo.length, rigEmailTo.length);
			}
			
			for (int emailIndex = 0; emailIndex < emailTo.length; emailIndex++) {
				if(!DCUtils.isNullOrEmpty(emailSettings.getTo())) {
					emailSettings.setTo(emailSettings.getTo().concat(String.format(",%s", emailTo[emailIndex])));
				}
				else
				{
					emailSettings.setTo(emailTo[emailIndex]);
				}
			}
			LOGGER.info("emailTo 3rd All : {}", emailTo[0]);
			if (emailTo.length > 0) {
				constructAndSendEmail(emailSettings, emailTo);
			}

			LOGGER.info("CSA Report email sent!");
		} catch (Exception exception) {
			LOGGER.error("Exception in sendEmail {}", exception);
			throw new SystemException(exception.getMessage(), exception);
		}
		return null;
	}

	/**
	 * @see {@link INotificationManager#sendDmlEmail()}
	 */
	@Override
	public String sendDmlEmail(DmlModel dmlModel, String loginURL) {
		String fromEmail = Constants.DML_FROM_EMAIL.getValue();
		String toEmail = "";
		String response = "";

		String subjectLine = dmlModel.getWorkPackStatus().equalsIgnoreCase(Constants.DML_STATUS_SUBMITTED.getValue())
				? Constants.DML_SUBMIT_EMAIL_SUBJECT.getValue()
				: getSubjectForNotSubmitted(dmlModel.getWorkPackStatus());

		List<Lookup> lookups = this.commonAccessor
				.getLookupValues(Constants.TO_LIST_FOR_DML_EMAIL_NOTIFICATION.getValue());
		List<String> ssoIds = new ArrayList<>();
		for(Lookup lookup:lookups) {
			if(!DCUtils.isNullOrEmpty(lookup.getValue()))
				ssoIds.add(lookup.getValue());
		}
		List<String> requestor = this.notificationResourceAccessor.getUserEmailIds(ssoIds, true);

		String url = String.format("%sManageWorkPacks/email/%s", loginURL, dmlModel.getId());

		if (!DCUtils.isNullOrEmpty(dmlModel.getLastUpdatedBySSO())) {
			ssoIds = new ArrayList<>();
			ssoIds.add(dmlModel.getLastUpdatedBySSO());
			toEmail = this.notificationResourceAccessor.getUserEmailIds(ssoIds, false).get(0);
		}
		String dmlEmailContent = this.notificationResourceAccessor.getDMLEmailContentByStatus(dmlModel, url);

		if (dmlModel.getWorkPackStatus().equalsIgnoreCase(Constants.DML_STATUS_INPROGRESS.getValue())
				&& !dmlModel.getComments().equalsIgnoreCase("null")) {
			response = this.notificationResourceAccessor.sendEmail(fromEmail, toEmail, null, null, subjectLine,
					dmlEmailContent);
		} else if (dmlModel.getWorkPackStatus().equalsIgnoreCase(Constants.DML_STATUS_APPROVED.getValue())) {
			response = this.notificationResourceAccessor.sendEmail(fromEmail, toEmail, null, null, subjectLine,
					dmlEmailContent);
		} else if (dmlModel.getWorkPackStatus().equalsIgnoreCase(Constants.DML_STATUS_SUBMITTED.getValue())) {
			response = this.notificationResourceAccessor.sendEmail(fromEmail, getCommaSeparatedValues(requestor, false),
					null, null, subjectLine, dmlEmailContent);
		}
		return response;
	}

	/**
	 * @see {@link INotificationManager#sendDmlEmail()}
	 */
	@Override
	public List<UserNotificationModel> getAlertNotificationList(String userId) {
		if (StringUtils.isEmpty(userId))
			throw new BadRequestException("Error: Invalid value <empty string> for : userId");
		List<UserNotificationModel> notifications = new ArrayList<>();
		List<Object[]> notificationList = notificationResourceAccessor.getAlertNotificationList(userId);
		for (Object[] notification : notificationList)
			notifications.add(getUserNotificationModel(notification));
		return notifications;
	}

	/**
	 * @see {@link INotificationManager#markAsReadNotification(String)}
	 */
	@Override
	public Map<String, String> markAsReadNotification(String notificationId) {
		if (StringUtils.isEmpty(notificationId))
			throw new BadRequestException("Error: Invalid value <empty string> for : notificationId");
		try {
			notificationResourceAccessor.markAsReadNotification(notificationId);
			Map<String, String> result = new HashMap<>();
			result.put(MessageUtil.RESULT, MessageUtil.SUCCESS);
			return result;
		} catch (Exception e) {
			LOGGER.error("Exception while marking notification as read {}", e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link INotificationManager#markAsReadAllNotification(String)}
	 */
	@Override
	public Map<String, String> markAsReadAllNotification(String userId) {
		if (StringUtils.isEmpty(userId))
			throw new BadRequestException("Error: Invalid value <empty string> for : userId");
		try {
			notificationResourceAccessor.markAsReadAllNotification(userId);
			Map<String, String> result = new HashMap<>();
			result.put(MessageUtil.RESULT, MessageUtil.SUCCESS);
			return result;
		} catch (Exception e) {
			LOGGER.error("Exception while marking all notifications as read {}", e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link INotificationManager#sendBulletinEmails(String, BulletinDetail)}
	 */
	@Override
	public Map<String, String> sendBulletinEmails(String type, BulletinDetail bulletinDetail) {
		Map<String, String> result = new HashMap<>();
		try {
			if (bulletinDetail.getNotifyCustomers().equals("Y")) {
				// send email notification
				sendEmailForNewBulletin(bulletinDetail);

				// To persist notification in Database
				notificationResourceAccessor.saveBulletinNotification(bulletinDetail, type);
			}
		} catch (Exception e) {
			LOGGER.error("Exception in sendBullEmails(), Exception : {}, StackTrace : {}", e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		result.put(MessageUtil.RESULT, MessageUtil.SUCCESS);
		return result;
	}

	/**
	 * @link {@link INotificationManager#sendNotificationRelease(NotificationRelease)}
	 */
	//@SuppressWarnings("null")
	@Override
	public String sendNotificationRelease(NotificationRelease notificationRelease,String triggerer) {
		String noReplyEmail = null;
		StringBuilder body = new StringBuilder();
		List<User> usersList = new ArrayList<>();
		List<String> userEmailList = new ArrayList<>();
		List<String> businessSupportTo = new ArrayList<>();
		String[] triggeringUser= {triggerer};
		try {
			validateNotificationRelease(notificationRelease);
			body.append(notificationRelease.getMailContent());
			if (notificationRelease.getCustomer() != null
					&& MessageUtil.STR_BOOLEAN_TRUE.equalsIgnoreCase(notificationRelease.getCustomer())
					&& notificationRelease.getCustomersList() != null) {
				usersList.addAll(notificationResourceAccessor.getUsers(DCQueries.QRY_GET_CUSTOMERS,
						notificationRelease.getCustomersList()));
			}
			if (notificationRelease.getGeUser() != null
					&& MessageUtil.STR_BOOLEAN_TRUE.equalsIgnoreCase(notificationRelease.getGeUser()))
				usersList.addAll(notificationResourceAccessor.getUsers(DCQueries.QRY_GET_GEUSERS, null));
			
			List<Lookup> lookupNoReply = commonAccessor.getLookupValues(Constants.NOREPLY_EMAIL.getValue());
			for (Lookup lookup : lookupNoReply)
				noReplyEmail = lookup.getValue();
			
			List<Lookup> supportTOs = commonAccessor.getLookupValues(Constants.BULLETIN_TO_EMAIL.getValue());
			for (Lookup supportTO : supportTOs)
				businessSupportTo.add(supportTO.getValue());
			
			List<String> emailList = new ArrayList<>();

			emailList = Arrays.asList(DCUtils.convertEmailIdStringToArray(notificationRelease.getAdditionalAudience()));
			if (notificationRelease.getEmail() != null
					&& MessageUtil.STR_BOOLEAN_TRUE.equalsIgnoreCase(notificationRelease.getEmail())) {
				//String[] bccEmail = new String[usersList.size()];
				//int index = 0;
				LOGGER.info("This Notification Release has been triggered by : "+triggerer);
				Email firstEmail = new Email(noReplyEmail, null, businessSupportTo.toArray(new String[businessSupportTo.size()]), null, notificationRelease.getSubject(),
						body.toString());
				emailUtil.constructAndSendEmail(firstEmail,triggeringUser, true);
				LOGGER.info("First Mail Triggered to Support DL and Triggerer successfully");
				
				for (User user : usersList) {
					userEmailList.add(user.getEmail());

					if (userEmailList.size()==180) {

						Email email = new Email(noReplyEmail, null, null, userEmailList.toArray(new String[userEmailList.size()]), notificationRelease.getSubject(),
								body.toString());
						emailUtil.constructAndSendEmail(email, emailList.toArray(new String[emailList.size()]), true);
						userEmailList = new ArrayList<>();
					}
				}
				if (!userEmailList.isEmpty()) {

					try {
						Email email = new Email(noReplyEmail, null, null, userEmailList.toArray(new String[userEmailList.size()]), notificationRelease.getSubject(),

								body.toString());
						emailUtil.constructAndSendEmail(email, emailList.toArray(new String[emailList.size()]), true);
					}catch(Exception e) {
						e.getStackTrace();
					}
				}
			}
                 
			if ((notificationRelease.getWeb() != null
					&& MessageUtil.STR_BOOLEAN_TRUE.equalsIgnoreCase(notificationRelease.getWeb())) && (notificationRelease.getEmail() == null || notificationRelease.getEmail() =="" )) {
				LOGGER.error("NO Mail Trigger, Only web visibility has been chosen by : " + triggerer);
             }
			if (notificationRelease.getWeb() != null
					&& MessageUtil.STR_BOOLEAN_TRUE.equalsIgnoreCase(notificationRelease.getWeb())) {
				notificationResourceAccessor.updateNotification(body.toString(), notificationRelease.getSubject());
				notificationResourceAccessor.updateviewPreferences(usersList);

			}

		} catch (Exception e) {
			LOGGER.error("Exception in sendNotificationRelease(), Exception : {}, StackTrace : {}", e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return "OK";
	}

	/**
	 * @see {@link INotificationManager#postFeedback(Feedback)}
	 */
	@Override
	public String postFeedback(Feedback feedback) {
		String response = null;
		String[] emailList = null;
		String feedbackEmailSendFrom = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		StringBuilder toEmail = new StringBuilder();
		try {
			if (feedback != null && !StringUtils.isEmpty(feedback.getFeedbackName())) {
				List<Lookup> lookups = commonAccessor.getLookupValues(Constants.FEEDBACK_TO_MAIL.getValue());
				for (Lookup lookup : lookups) {
					toEmail.append(lookup.getValue());
					toEmail.append(",");
				}
				lookups = commonAccessor.getLookupValues(Constants.FEEDBACK_FROM_MAIL.getValue());
				for (Lookup lookup : lookups) {
					feedbackEmailSendFrom = lookup.getValue();
				}
				emailList = DCUtils.convertStringToArray(toEmail.toString().substring(0, toEmail.length() - 1));
				String body = MessageUtil.FEEDBACK_EMAIL_BODY + "\r\n\r\n" + feedback.getFeedbackName() + "\r\n\r\n"
						+ MessageUtil.FEEDBACK_USER + "\r\n First Name : " + feedback.getFirstName()
						+ "\r\n Last Name : " + feedback.getLastName() + "\r\n Company Name : "
						+ feedback.getCompanyName() + "\r\n User Id : " + feedback.getUserId() + "\r\n Email Id : "
						+ feedback.getEmailId() + "\r\n\n Date : " + dateFormat.format(new Date())
						+"\r\n\n Your engageDrilling Support Team \r\n Powered by Baker Hughes\r\n"+System.getenv(MessageUtil.APP_URL);
				Email email = new Email(feedbackEmailSendFrom, null, null, null, MessageUtil.FEEDBACK_EMAIL_SUBJECT,
						body);
				emailUtil.constructAndSendEmail(email, emailList,false);
				postFeedbackVerificationEmail(feedback, feedbackEmailSendFrom);
				response = MessageUtil.SUCCESS;
			} else {
				throw new BadRequestException("Feedback content cann't be Empty");
			}
		} catch (BadRequestException e) {
			LOGGER.error("Exception in Posting Feedback, Exception : {}, StackTrace : {}", e.getMessage(), e);
			throw new BadRequestException(e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("Exception in Posting Feedback, Exception : {}, StackTrace : {}", e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return response;
	}

	/*************************** Private Methods ***********************/

	/**
	 * constructAndSendEmail using {@link Email} object
	 * 
	 * @param feature {@link Email}
	 * 
	 * @return NIL
	 */
	private void constructAndSendEmail(Email email, String[] toEmail) {
		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setFrom(new InternetAddress(email.getFrom()));
			
			if (!StringUtils.isEmpty(email.getTo()))
				helper.setTo(email.getTo().split(","));
			else
				helper.setTo(toEmail);
			if (email.getBcc() != null && email.getBcc().length > 0)
				helper.setBcc(String.join(",", email.getBcc()));
			if (email.getCc() != null && email.getCc().length > 0)
				helper.setCc(String.join(",", email.getCc()));
			helper.setSubject(email.getSubject());
			helper.setSentDate(new Date());
			helper.setText(email.getBody(), true);
			this.emailSender.send(message);
		} catch (MessagingException e) {
			LOGGER.error("Exception in constructAndSendEmail, Exception : {}, StackTrace : {}", e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
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
		
		if(list.toString().indexOf(',')==0) {
			return list.toString().substring(1);
		}
		return list.toString();

	}

	/**
	 * Get {@link String} object from {@link String}
	 * 
	 * @param value {@link String}
	 * 
	 * @return Returns a {@link String}
	 */
	private String getSubjectForNotSubmitted(String value) {
		return value.equalsIgnoreCase(Constants.DML_STATUS_APPROVED.getValue())
				? Constants.DML_APPROVED_EMAIL_SUBJECT.getValue()
				: Constants.DML_INPROGRESS_EMAIL_SUBJECT.getValue();
	}

	/**
	 * Get {@link UserNotificationModel} from Object[]
	 * 
	 * @param notification {@link Object[]}
	 * 
	 * @return Returns a {@link UserNotificationModel}
	 */
	private UserNotificationModel getUserNotificationModel(Object[] notification) {
		UserNotificationModel notificationModel = new UserNotificationModel();
		notificationModel.setDrillNotificationId(DCUtils.getValueAsString(notification[0]));
		notificationModel.setNotificationType(DCUtils.getValueAsString(notification[1]));
		notificationModel.setEventType(DCUtils.getValueAsString(notification[3]));
		notificationModel.setMarkAsRead(DCUtils.getValueAsString(notification[2]));
		notificationModel.setNotificationName(DCUtils.getValueAsString(notification[4]));
		notificationModel.setNotificationDescription(DCUtils.getValueAsString(notification[5]));
		notificationModel.setNotificationDate(DCUtils.getValueAsString(notification[6]));
		notificationModel.setCreatedBy(DCUtils.getValueAsString(notification[7]));
		notificationModel.setCreatedDate(DCUtils.getValueAsString(notification[8]));
		notificationModel.setParentId(DCUtils.getValueAsString(notification[9]));
		notificationModel.setSubParentId(DCUtils.getValueAsString(notification[10]));
		notificationModel.setRigName(DCUtils.getValueAsString(notification[11]));
		notificationModel.setCustName(DCUtils.getValueAsString(notification[12]));
		return notificationModel;
	}

	/**
	 * Get Bulletin Email Body
	 * 
	 * @param bulletinDetail {@link BulletinDetail}
	 * 
	 * @return Returns a {@link String}
	 */
	private void sendEmailForNewBulletin(BulletinDetail bulletinDetail) {

		String[] fromEmailArray = null;
		String fromEmail = null;
		List<String> userEmailList = new ArrayList<>();
		String[] toEmail = null;

		List<Lookup> lookups = commonAccessor.getLookupValues(
				Arrays.asList(Constants.NOREPLY_EMAIL.getValue(), Constants.BULLETIN_TO_EMAIL.getValue()));

		fromEmailArray = getStringArrayFromList(lookups, Constants.NOREPLY_EMAIL.getValue());
		toEmail = getStringArrayFromList(lookups, Constants.BULLETIN_TO_EMAIL.getValue());
		if (fromEmailArray != null && fromEmailArray.length > 0)
			fromEmail = fromEmailArray[0];

		List<Object> userEmailIds = notificationResourceAccessor.getBulletinEmailIds(bulletinDetail.getFleetCustomers(),
				bulletinDetail.getBulletinNum());

		Lookup lookup = commonAccessor.getLookupValues(Constants.BULLETIN_TYPE.getValue(),
				bulletinDetail.getBulletinType());
		String strEmailSubject = MessageUtil.NEW_BULLETIN_SUB + DCUtils.checkEmpty(lookup.getDescription()) + " - "
				+ bulletinDetail.getBulletinNum() + "  Revision - " + bulletinDetail.getRevision();

		String emailBody = getEmailBodyForNewBulletin(bulletinDetail);
		for (Object object : userEmailIds) {
			if (object != null)
				userEmailList.add(object.toString());

			if (userEmailList.size() == 50) {
				Email email = new Email(fromEmail, null, null, userEmailList.toArray(new String[userEmailList.size()]),
						strEmailSubject, emailBody);
				try {
					emailUtil.constructAndSendEmail(email, toEmail, true);
				} catch (Exception e) {
					LOGGER.error("Exception while sending email for New bulletin, Exception : {}, StackTrace : {}",
							e.getStackTrace(), e);
				}
				userEmailList = new ArrayList<>();
			}
		}
		if (!userEmailList.isEmpty()) {
			Email email = new Email(fromEmail, null, null, userEmailList.toArray(new String[userEmailList.size()]),
					strEmailSubject, emailBody);
			try {
				emailUtil.constructAndSendEmail(email, toEmail, true);
			} catch (Exception e) {
				LOGGER.error("Exception while sending email for New bulletin, Exception : {}, StackTrace : {}",
						e.getStackTrace(), e);
			}
		}
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

	/**
	 * Get Email Body for New Bulletin
	 * 
	 * @return Returns a {@link String}
	 */
	private String getEmailBodyForNewBulletin(BulletinDetail bulletinDetail) {
		Long existingDocId = notificationResourceAccessor.getBulletinDocId(bulletinDetail.getBulletinNum());
		String docLink = System.getenv("BULLETIN_MAIL_URL") + existingDocId;
		StringBuilder sb = new StringBuilder();
		sb.append("<html> <head></head> <body style='font-family:calibri;'> <br/><br/><br/>");
		sb.append(bulletinDetail.getBulletinType()).append("  -  ");
		sb.append(bulletinDetail.getBulletinNum()).append("    Revision - ");
		sb.append(bulletinDetail.getRevision()).append("  ").append(MessageUtil.NEW_BULLETIN_BODY1)
				.append("<br/><br/>");
		sb.append(MessageUtil.NEW_BULLETIN_BODY2).append("<br/><br/> Bulletin  - ")
				.append(bulletinDetail.getBulletinNum());
		sb.append("  Revision - ").append(bulletinDetail.getRevision()).append("<br/> Issue Date - ")
				.append(bulletinDetail.getBulletinIssueDtStr());
		sb.append("<br/> Description - ").append(bulletinDetail.getBulletinDesc()).append("<br/> Scope - ")
				.append(bulletinDetail.getBulletinScope());
		sb.append("<br/><br/> Click <a href=\"").append(docLink)
				.append("\">  here </a> to view Bulletin Document. <br/><br/>");
		sb.append(MessageUtil.NEW_BULLETIN_BODY3).append("<br/><br/>").append(MessageUtil.NEW_BULLETIN_BODY4)
				.append("<br/><br/>");
		sb.append(MessageUtil.NEW_BULLETIN_BODY5).append("<br/> Note: ").append(MessageUtil.NEW_BULLETIN_BODY6);
		return sb.toString();
	}

	/*
	 * Helper method for Post Feedback. Sends verification e-Mail to Customer who is
	 * posting Feedback. Verification / Receipt e-Mail sent to Customer after
	 * submitting a feedback response.
	 */
	private void postFeedbackVerificationEmail(Feedback feedback, String feedbackEmailSendFrom) {
		try {
			String verificationBody = "Thank you for submitting feedback through the engageDrilling platform. "
					+ "\r\n Our team is now reviewing your submission; you should receive a response within 24 hours."
					+ "\r\n A response may take up to 48 hours if submitted outside normal business hours."
					+ "\r\n\r\n\r\n" + "Regards," + "\r\n" + "engageDrilling Team";
			String[] verificationEmailList = new String[] { feedback.getEmailId() };
			if (feedback.getEmailId() != null) {
				Email email = new Email(feedbackEmailSendFrom, null, null, null, MessageUtil.FEEDBACK_EMAIL_SUBJECT,
						verificationBody);
				emailUtil.constructAndSendEmail(email, verificationEmailList,false);
			}
		} catch (Exception e) {
			LOGGER.error("Exception while postFeedbackVerificationEmail, Exception : {}, StackTrace : {}",
					e.getMessage(), e);
		}
	}

	/**
	 * Validate Notification Release
	 * 
	 * @param notificationRelease {@link NotificationRelease}
	 * @throws {@link SystemException}
	 */
	private void validateNotificationRelease(NotificationRelease notificationRelease) {
		if (notificationRelease == null) {
			LOGGER.info("No recipient and No Message Found");
			throw new SystemException("No recipient and No Message Found");
		}
	}

	@Override
	public Map<String, String> sendMCDEmails(String normalizedType, MarkCompDataDtls markCompDataDtls) {
		Map<String, String> result = new HashMap<>();
		try {
			if (markCompDataDtls.getNotifyCustomers().equals("Y")) {
				// send email notification
				sendEmailForNewMCD(markCompDataDtls);
           }
		} catch (Exception e) {
			LOGGER.error("Exception in sendMCDEmails(), Exception : {}, StackTrace : {}", e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		result.put(MessageUtil.RESULT, MessageUtil.SUCCESS);
		return result;
	}
	private void sendEmailForNewMCD(MarkCompDataDtls markCompDataDtls) {

		String[] fromEmailArray = null;
		String fromEmail = null;
		List<String> userEmailList = new ArrayList<>();
		String[] toEmail = null;
		int extPos = 0;
		String docName = null;

		List<Lookup> lookups = commonAccessor.getLookupValues(
				Arrays.asList(Constants.NOREPLY_EMAIL.getValue(), Constants.BULLETIN_TO_EMAIL.getValue()));
		
		extPos = markCompDataDtls.getDocumentName().lastIndexOf(".");
		docName = (extPos == -1) ?markCompDataDtls.getDocumentName() :markCompDataDtls.getDocumentName().substring(0, extPos);

		fromEmailArray = getStringArrayFromList(lookups, Constants.NOREPLY_EMAIL.getValue());
		toEmail = getStringArrayFromList(lookups, Constants.BULLETIN_TO_EMAIL.getValue());
		if (fromEmailArray != null && fromEmailArray.length > 0)
			fromEmail = fromEmailArray[0];

		List<Object> userEmailIds = notificationResourceAccessor.getMCDEmailIds(markCompDataDtls.getFleetDoc(),
				markCompDataDtls.getDocumentName(),markCompDataDtls.getDocType(),markCompDataDtls.getId());

		String strEmailSubject = MessageUtil.NEW_BULLETIN_SUB + docName + "  Revision - " + markCompDataDtls.getRevisionNum();

		String emailBody = getEmailBodyForNewMCD(markCompDataDtls,docName);
		for (Object object : userEmailIds) {
			if (object != null)
				userEmailList.add(object.toString());

			if (userEmailList.size() == 50) {
				Email email = new Email(fromEmail, null, null, userEmailList.toArray(new String[userEmailList.size()]),
						strEmailSubject, emailBody);
				try {
					emailUtil.constructAndSendEmail(email, toEmail, true);
				} catch (Exception e) {
					LOGGER.error("Exception while sending email for New MCD, Exception : {}, StackTrace : {}",
							e.getStackTrace(), e);
				}
				userEmailList = new ArrayList<>();
			}
		}
		if (!userEmailList.isEmpty()) {
			Email email = new Email(fromEmail, null, null, userEmailList.toArray(new String[userEmailList.size()]),
					strEmailSubject, emailBody);
			try {
				emailUtil.constructAndSendEmail(email, toEmail, true);
			} catch (Exception e) {
				LOGGER.error("Exception while sending email for New MCD, Exception : {}, StackTrace : {}",
						e.getStackTrace(), e);
			}
		}
	}
	
	private String getEmailBodyForNewMCD(MarkCompDataDtls markCompDataDtls,String docName) {
		Long existingDocId = notificationResourceAccessor.getMCDDocId(markCompDataDtls.getId(), markCompDataDtls.getDocType());
		String docLink = System.getenv("MCD_MAIL_URL") + markCompDataDtls.getDocType() + "/" + existingDocId;
		String docType = DCUtils.capitalize(markCompDataDtls.getDocType().toLowerCase());
		
		StringBuilder sb = new StringBuilder();
		sb.append("<html> <head></head> <body style='font-family:calibri;'> <br/><br/><br/>");
		sb.append(docName).append("    Revision - ");
		sb.append(markCompDataDtls.getRevisionNum()).append("  ").append(MessageUtil.NEW_BULLETIN_BODY1)
				.append("<br/><br/>");
		sb.append(MessageUtil.NEW_MCD_BODY).append(docType).append(MessageUtil.NEW_MCD_BODY2).append(MessageUtil.NEW_MCD_BODY3).append(docType.toLowerCase()).append(" details.").append("<br/><br/> "+docType +" - ")
				.append(docName);
		sb.append("  Revision - ").append(markCompDataDtls.getRevisionNum()).append("<br/> Issue Date - ")
				.append(markCompDataDtls.getIssueDt());
		sb.append("<br/> Description - ").append(markCompDataDtls.getDescription()).append("<br/> Scope - ")
				.append(markCompDataDtls.getScope());
		sb.append("<br/><br/> Click <a href=\"").append(docLink)
				.append("\">  here </a> to view "+ docType +" Document. <br/><br/>");
		sb.append(MessageUtil.NEW_BULLETIN_BODY3).append("<br/><br/>");
		return sb.toString();
	}

}
