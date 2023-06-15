package com.bh.drillingcommons.ras.impl;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bh.drillingcommons.entity.oracle.OGDrillEDNotification;
import com.bh.drillingcommons.entity.oracle.OgDrillUserEula;
import com.bh.drillingcommons.entity.oracle.OgDrillWebNotification;
import com.bh.drillingcommons.enumerators.Constants;
import com.bh.drillingcommons.exceptions.SystemException;
import com.bh.drillingcommons.models.BulletinDetail;
import com.bh.drillingcommons.models.DmlModel;
import com.bh.drillingcommons.models.User;
import com.bh.drillingcommons.ras.INotificationResourceAccessor;
import com.bh.drillingcommons.repository.IOGDrillEDNotificationRepo;
import com.bh.drillingcommons.util.DCQueries;
import com.bh.drillingcommons.util.DCUtils;

@Component("notificationResourceAccessor")
public class NotificationResourceAccessor implements INotificationResourceAccessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationResourceAccessor.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public JavaMailSender emailSender;

	@Autowired
    private IOGDrillEDNotificationRepo iogDrillEDNotificationRepo;
	
	@Autowired
	private Environment environment;

	/**
	 * @see {@link INotificationResourceAccessor#removeNotification(String)}
	 */
	@Override
	@Transactional
	public void removeNotification(String notfnId) {
		try {
            OGDrillEDNotification ogDrillEDNotification = iogDrillEDNotificationRepo.findByDrillNotificationId(Long.parseLong(notfnId));
			if(ogDrillEDNotification!=null) {
				ogDrillEDNotification.setActiveFlag("N");
				iogDrillEDNotificationRepo.save(ogDrillEDNotification);
			}
		} catch (Exception e) {
			LOGGER.error("Exception while removing notification, Exception : {}", e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link INotificationResourceAccessor#removeAllNotification(String)}
	 */
	@Override
	@Transactional
	public void removeAllNotification(String userId) {
		try {
			iogDrillEDNotificationRepo.updateNotification(userId);
		} catch (Exception e) {
			LOGGER.error("Exception while removing notification, Exception : {}", e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link INotificationResourceAccessor#getNotificationList(String,String)}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<OgDrillWebNotification> getNotificationList(String userId, String readOrNot) {
		Query query = null;
		try {
			if (null != readOrNot && "NO".equalsIgnoreCase(readOrNot))
				query = entityManager.createNativeQuery(DCQueries.QRY_GET_NOTIFICATION_LIST);
			else
				query = entityManager.createNativeQuery(DCQueries.QRY_GET_NOTIFICATION_LIST_ALL);
			query.setParameter(1, userId != null ? userId.toLowerCase() : userId);
			List<Object[]> notificationObjects = query.getResultList();
			return getNotificationModels(notificationObjects);
		} catch (Exception exception) {
			LOGGER.error("Exception while fetching notification lookups, Exception : {}", exception);
			throw new SystemException(exception.getMessage(), exception);
		}
	}

	/**
	 * @see {@link INotificationResourceAccessor#markAsReadNotification(String)}
	 */
	@Override
	@Transactional
	public void markAsReadNotification(String notificationId) {
		try {
			Query query = entityManager.createNativeQuery(DCQueries.QUERY_UPDATE_NOTIF);
			query.setParameter(1, notificationId);
			query.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Exception while updating read notification, Exception : {}, StackTrace : {}", e.getMessage(),
					e);
			throw new SystemException(e.getMessage(), e);
		}

	}

	/**
	 * @see {@link INotificationResourceAccessor#markAsRead(String,String)}
	 */
	@Override
	@Transactional
	public String markAsRead(String userId, String readId) {
		String retValue = "Success";
		Query query = null;
		try {
			if (readId != null && "ALL".equalsIgnoreCase(readId)) {
				query = entityManager.createNativeQuery(DCQueries.QRY_UPDATE_NOTIFICATION_RELEASE);
				query.setParameter(1, userId != null ? userId.toLowerCase() : userId);
			} else {
				query = entityManager.createNativeQuery(DCQueries.QRY_UPDATE_NOTIFICATION_RELEASE_SPECIFICREAD);
				query.setParameter(1, userId != null ? userId.toLowerCase() : userId);
				query.setParameter(2, readId);
			}
			query.executeUpdate();
		} catch (Exception exception) {
			LOGGER.error("Exception while marking notification as read : {}", exception);
			throw new SystemException(exception.getMessage(), exception);
		}
		return retValue;
	}

	/**
	 * @see {@link INotificationResourceAccessor#getRigName(String)}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getRigName(String rigId) {
		String rigName = null;
		try {
			Query query = entityManager.createNativeQuery(DCQueries.GET_RIG_NAME_BY_ID);
			query.setParameter("rigId", rigId);
			List<Object> rigNameObj = query.getResultList();
			if(!rigNameObj.isEmpty())
				return (String)rigNameObj.get(0);
		} catch (Exception exception) {
			LOGGER.error("Exception while fetching rig name {}", exception);
			throw new SystemException(exception.getMessage(), exception);
		}
		return rigName;
	}

	/**
	 * @see {@link INotificationResourceAccessor#getUserEmailIds(String,boolean)}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getUserEmailIds(List<String> ssoIds, boolean multipleParams) {
		Query query = null;
		
		try {
			query = (multipleParams)
					? entityManager
							.createNativeQuery(
									"SELECT USER_EMAIL AS userMailId FROM og_drill_user  WHERE USER_ID IN :ssoIds")
							.setParameter("ssoIds", ssoIds)
					: entityManager
							.createNativeQuery(
									"SELECT USER_EMAIL AS userMailId FROM og_drill_user  WHERE USER_ID =:ssoIds")
							.setParameter("ssoIds", ssoIds);
			return query.getResultList();
		} catch (Exception exception) {
			LOGGER.error("Exception while fetching user email ids {}", exception);
			throw new SystemException(exception.getMessage(), exception);
		}
	}

	/**
	 * @see {@link INotificationResourceAccessor#getDMLEmailContentByStatus(String,String)}
	 */
	@Override
	public String getDMLEmailContentByStatus(DmlModel dmlModel, String url) {
		String bgColorCode1 = "";
		String bgColorCode2 = "";
		String bgColorCode3 = "";
		StringBuilder dmlEmailContent = new StringBuilder().append("<html> " + "<head><h2 style='color:red;'>");

		if (dmlModel.getWorkPackStatus().equalsIgnoreCase(Constants.DML_STATUS_INPROGRESS.getValue())) {
			bgColorCode1 = Constants.COLOR_BLUE.getValue();
			bgColorCode2 = Constants.COLOR_GREY.getValue();
			bgColorCode3 = Constants.COLOR_GREY.getValue();
			dmlEmailContent
					.append("Please review approver comments on returned Work Pack:</h2>" + "<h4>Approver Comment:"
							+ dmlModel.getComments() + "</h4><h4>Approver Name:" + dmlModel.getLastUpdatedBy() + "</h4>");
		} else if (dmlModel.getWorkPackStatus().equalsIgnoreCase(Constants.DML_STATUS_APPROVED.getValue())) {
			bgColorCode1 = bgColorCode2 = bgColorCode3 = Constants.COLOR_GREEN.getValue();
			dmlEmailContent.append("Notification of approved Work Pack:</h2>");
		} else if (dmlModel.getWorkPackStatus().equalsIgnoreCase(Constants.DML_STATUS_SUBMITTED.getValue())) {
			bgColorCode1 = Constants.COLOR_INDIGO.getValue();
			bgColorCode2 = Constants.COLOR_BLUE.getValue();
			bgColorCode3 = Constants.COLOR_GREY.getValue();
			dmlEmailContent.append("Please review and approve submitted Work Pack:</h2>");
		}

		dmlEmailContent.append("</head>" + "<script></br>" + "function ShowContent(){</br>"
				+ "alert('hiii in function')}</br></script>" + "<body style='font-family:calibri;'>" + "<h3>"
				+ dmlModel.getTaskName() + "</h3>" + "<table border='0' cellpadding='4' cellspacing='0' width='650'>"
				+ "<tr style='font-size:16px'>"
				+ "<td text-align='center' style='background-color: #3B73B9;'>&nbsp;&nbsp;&nbsp;Not Started&nbsp;&nbsp;&nbsp;</th>");

		if (dmlModel.getTaskName().equalsIgnoreCase(Constants.DML_STATUS_APPROVED.getValue()))
			dmlEmailContent.append("<td style='color: #68BC68; font-size:22px;'>&raquo; &nbsp;</th>");
		else
			dmlEmailContent.append("<td style='color: #3B73B9; font-size:22px;'>&raquo; &nbsp;</th>");

		dmlEmailContent.append("<td text-align='center' style='background-color:" + bgColorCode1
				+ ";'>&nbsp;&nbsp;&nbsp;In Progress&nbsp;&nbsp;&nbsp;</th>" + "<td style='color: " + bgColorCode1
				+ "; font-size:22px;'>&raquo; &nbsp;</td>" + "<td text-align='center' style='background-color: "
				+ bgColorCode2 + ";'>&nbsp;&nbsp;&nbsp;Submitted&nbsp;&nbsp;&nbsp;</th>" + "<td style='color: "
				+ bgColorCode2
				+ "; font-size:22px;'>&raquo; &nbsp;</td><td text-align='center' style='background-color: "
				+ bgColorCode3
				+ ";'>&nbsp;&nbsp;&nbsp;Approved&nbsp;&nbsp;&nbsp;</th></tr><td colspan='7'> </td></tr>");

		dmlEmailContent.append(getRepeatedHtmlCode("Parent Assembly:", "Child Assembly:")
				+ getRepeatedHtmlCodeValues(dmlModel.getParentAssembly(), dmlModel.getChildAssembly())
				+ getRepeatedHtmlCode("Discipline:", "Maintenance Interval:")
				+ getRepeatedHtmlCodeValues(dmlModel.getDiscipline(), dmlModel.getMaintenanceInterval())
				+ getRepeatedHtmlCode("Last Updated By:", "Last Updated Date:")
				+ getRepeatedHtmlCodeValues(dmlModel.getLastUpdatedBy(), dmlModel.getDateUpdated())
				+ getRepeatedHtmlCode("Customers:", "Rigs:")
				+ getRepeatedHtmlCodeValues(dmlModel.getCustomers(), dmlModel.getRigs()) + "</table><br/><br/>"
				+ "<span>");
		dmlEmailContent.append(dmlModel.getWorkPackStatus().equalsIgnoreCase(Constants.DML_STATUS_SUBMITTED.getValue())
				? "Please click below to take action:"
				: "Please click below to review Work Pack:");
		dmlEmailContent.append("<span>" + "</br>"
				+ "<a style='background-color: #313337;border: none;color: white;padding: 20px;text-align: center;text-decoration: none;display: inline-block;font-size: 16px;margin: 4px 2px;border-radius: 12px;' href="
				+ url + "   " + "onclick='ShowContent();'>Click Here</a>" + "</body></br>" + "</html>");
		return dmlEmailContent.toString();
	}

	/**
	 * @see {@link INotificationResourceAccessor#sendEmail(String, String, String, String , String, String)}
	 */
	@Override
	public String sendEmail(String fromList, String toList, String ccList, String bccList, String emailSubject,
			String emailContent) {
		List<String> list =new ArrayList<>();
		try {
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setContent(emailContent, "text/html");

			MimeMultipart mimeMultipart = new MimeMultipart();
			mimeMultipart.addBodyPart(textBodyPart);

			InternetAddress iaSender = new InternetAddress(fromList);

			MimeMessage mimeMessage = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setFrom(iaSender);
			helper.setSubject(emailSubject);
			if(toList.contains(",")){
				 list = Arrays.asList(toList.split(","));
				}else {
					list=Arrays.asList(toList);
				}
				String[] toAarray =list.toArray(new String[0]);
				helper.setTo(toAarray);
			if (!DCUtils.isNullOrEmpty(ccList))
				helper.setCc(ccList);
			if (!DCUtils.isNullOrEmpty(bccList))
				helper.setBcc(InternetAddress.parse(bccList));
			mimeMessage.setContent(mimeMultipart);

			this.emailSender.send(mimeMessage);
			LOGGER.info("Email Send");
		} catch (Exception ex) {
			LOGGER.error("Exception while sending mail ::" + ex.getMessage(), ex);
		}
		return "OK";
	}
	
	
	@Override
	public void mailSender(String[] emailList, String[] ccList, String[] bccList, String fromMail, String subjectLine,
			String mailBody, boolean isHtml) {
		JavaMailSenderImpl mailimpl = new JavaMailSenderImpl();
	    mailimpl.setHost(environment.getProperty("spring.mail.host"));
	    mailimpl.setPort(Integer.parseInt(environment.getProperty("spring.mail.port")));
		MimeMessageHelper helper = new MimeMessageHelper(mailimpl.createMimeMessage());
		
		try {
			helper.setTo(emailList);
			if (ccList != null && ccList.length != 0)
				helper.setCc(ccList);
			if (bccList != null && bccList.length != 0)
				helper.setBcc(bccList);
			helper.setFrom(fromMail);
			helper.setSubject(subjectLine);
			helper.setText(mailBody, isHtml);
			mailimpl.send(helper.getMimeMessage());
		} catch (MessagingException ex) {
			LOGGER.error("Error while sending email {}", ex.getMessage());
			throw new SystemException(ex.getMessage(), ex);
		}
	}

	/**
	 * @see {@link INotificationResourceAccessor#markAsReadAllNotification(String)}
	 */
	@Override
	@Transactional
	public void markAsReadAllNotification(String userId) {
		try {
			Query query = entityManager.createNativeQuery(DCQueries.UPDATE_NOTIFICATIONS_BY_USER_ID);
			query.setParameter(1, userId);
			query.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Exception while updating read all notification, Exception : {}, StackTrace : {}",
					e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link INotificationResourceAccessor#getAlertNotificationList(String)}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAlertNotificationList(String userId) {
		try {
			Query query = entityManager.createNativeQuery(DCQueries.GET_ALERT_NOTIFICATIONS);
			query.setParameter(1, userId);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error("Exception while getting alert notifications for a user : {}, Exception : {}, StackTrace : {}",
					userId, e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link INotificationResourceAccessor#getBulletinEmailIds(String, String)}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getBulletinEmailIds(String fleetLevel, String bulletinNumber) {
		List<Object> userEmailIds = null;
		try {
			if (fleetLevel.equals("Y")) {
				Query query = entityManager.createNativeQuery(DCQueries.GET_BULLETIN_MAILS_WITH_FLEET_LEVEL);
				userEmailIds = query.getResultList();
			} else {
				Query query = entityManager.createNativeQuery(DCQueries.GET_BULLETIN_MAILS_WITHOUT_FLEET_LEVEL);
				query.setParameter(1, bulletinNumber);
				userEmailIds = query.getResultList();
			}
		} catch (Exception e) {
			LOGGER.error("Exception in getBulletinEmailIds(), Exception : {}, StackTrace : {}", e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return userEmailIds;
	}

	/**
	 * @see {@link INotificationResourceAccessor#saveBulletinNotification(BulletinDetail, String)}
	 */
	@Transactional
	@Override
	public void saveBulletinNotification(BulletinDetail bulletinDetail, String type) {
		StringBuilder nativeQuery = new StringBuilder();
		try {
			String normalizedType = Normalizer.normalize(type, Form.NFKC);
			// Delete previous Notifications if Bulletin is revised
			nativeQuery.append(DCQueries.DELETE_NOTIFICATION);
			Query query = entityManager.createNativeQuery(nativeQuery.toString());
			query.setParameter("parentId", bulletinDetail.getBulletinNum());
			int result = query.executeUpdate();
			LOGGER.info("Deleted previous Notifications if Bulletin is revised, response : {} ", result);
			
			String bulletinNum = Normalizer.normalize(bulletinDetail.getBulletinNum(), Form.NFKC);
			String notificationName = String.format("%s %s %s", bulletinDetail.getBulletinType(), bulletinDetail.getBulletinNum(), bulletinDetail.getRevision());

			if ("N".equals(bulletinDetail.getFleetCustomers()))
				iogDrillEDNotificationRepo.addDrillEdNotificationByFleetFalse(getNotificationId(), normalizedType, notificationName, bulletinDetail.getBulletinDesc(), bulletinDetail.getRevision(), bulletinDetail.getLastUpdatedBy(), bulletinNum);
			else
				iogDrillEDNotificationRepo.addDrillEdNotification(getNotificationId(), normalizedType, notificationName, bulletinDetail.getBulletinDesc(), bulletinDetail.getRevision(), bulletinDetail.getLastUpdatedBy(), bulletinNum);

			if ("N".equals(bulletinDetail.getFleetCustomers())) {
				iogDrillEDNotificationRepo.addNotificationFleetFalse(getNotificationId(), bulletinNum, normalizedType, notificationName, bulletinDetail.getBulletinDesc(), bulletinDetail.getRevision(), bulletinDetail.getLastUpdatedBy());
			}
		} catch (Exception e) {
			LOGGER.error("Exception in saveBulletinNotification(), Exception : {}, StackTrace : {}", e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link INotificationResourceAccessor#getBulletinDoc(String)}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Long getBulletinDocId(String bulletinId) {
		List<Object> docIdList = new ArrayList<>();
		try {
			Query query = entityManager.createNativeQuery(DCQueries.GET_OG_DRILL_DOCUMENT_ID);
			query.setParameter(1, bulletinId);
			docIdList = query.getResultList();
		} catch (Exception e) {
			LOGGER.error("Exception in getDoc()", e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		if (docIdList.isEmpty()) {
			LOGGER.warn("Did not find doc with bulletin num = {}", bulletinId);
			return Long.valueOf(0);
		}
		return Long.valueOf(docIdList.get(0).toString());
	}

	/**
	 * @see {@link INotificationResourceAccessor#getUsers(String)}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsers(String query, String[] userIdList) {
		List<User> users = new ArrayList<>();
		try {
			List<Object[]> userList = userIdList != null
					? entityManager.createNativeQuery(query).setParameter("userIdList", Arrays.asList(userIdList))
							.getResultList()
					: entityManager.createNativeQuery(query).getResultList();
			for (Object[] user : userList)
				users.add(getUser(user));
		} catch (Exception e) {
			LOGGER.error("Exception in while fetcing users, Exception : {}, stackTrace : {}", e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return users;
	}

	/**
	 * @see {@link INotificationResourceAccessor#updateNotification(String, String)}
	 */
	@Override
	@Transactional
	public void updateNotification(String content, String subject) {
		try {
			OgDrillWebNotification webNotification = new OgDrillWebNotification();
			webNotification.setActiveFlag("A");
			Query query = entityManager.createNativeQuery(DCQueries.MAX_WEB_NOTIFICATION_ID);
			BigDecimal webId = (BigDecimal) query.getSingleResult();
			if (webId == null)
				webId = BigDecimal.valueOf(0);
			webNotification.setId(webId.add(new BigDecimal(1)));
			webNotification.setNotificationDetails(content);
			webNotification.setSubject(subject);
			entityManager.persist(webNotification);
		} catch (Exception e) {
			LOGGER.error("Exception in Notification Release updateNotification(), Exception : {}, StackTrace : {}",
					e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link INotificationResourceAccessor#updateviewPreferences(List)}
	 */
	@Override
	@Transactional
	public void updateviewPreferences(List<User> usersList) {
		try {
			OgDrillUserEula eula = null;
			Query query = entityManager.createNativeQuery(DCQueries.MAX_WEB_NOTIFICATION_ID);
			BigDecimal webId = (BigDecimal) query.getSingleResult();
			if (webId == null)
				webId = BigDecimal.valueOf(0);

			for (User user : usersList) {
				eula = new OgDrillUserEula();
				eula.setUserId1(user.getUserId());
				eula.setPropKey("Notification_Release");
				eula.setPropValue("0");
				eula.setViewObj(String.valueOf(webId));
				entityManager.persist(eula);
			}
		} catch (Exception e) {
			LOGGER.error("Exception in Notification Release updateviewPreferences(), Exception : {}, StackTrace : {}",
					e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/*************************** Private Methods ***********************/

	/**
	 * Get {@link List<OgDrillWebNotification>} object from {@link List<Object[]>}
	 * 
	 * @param feature {@link List<Object[]>}
	 * 
	 * @return Returns a {@link List<OgDrillWebNotification>}
	 */
	private List<OgDrillWebNotification> getNotificationModels(List<Object[]> objectList) {
		List<OgDrillWebNotification> notificationModels = new ArrayList<>();
		for (Object[] object : objectList) {
			OgDrillWebNotification notification = new OgDrillWebNotification();
			notification.setId(object[0] == null ? BigDecimal.ZERO : (BigDecimal) object[0]);
			notification.setSubject(object[1] == null ? null : (String) object[1]);
			notification.setNotificationDetails(object[2] == null ? null : (String) object[2]);
			if (object.length > 3)
				notification.setActiveFlag(String.valueOf(object[3] == null ? 'N' : (char) object[3]));
			notificationModels.add(notification);
		}
		return notificationModels;
	}

	/**
	 * Get {@link String} object from {@link String,String}
	 * 
	 * @param column1,column2 {@link String,String}
	 * 
	 * @return Returns a {@link String}
	 */
	private String getRepeatedHtmlCode(String column1, String column2) {
		return "<tr>"
				+ "<td colspan='7' style='align:center;background-color: #313337; vertical-align: middle;'><p style='color: #ffffff;text-align: center;'>Work Pack Summary</p></td>"
				+ "</tr><tr><td colspan='7'> </td>" + "</tr><tr>"
				+ "<td colspan='3' width='49%' style='align:left; padding-left:15px;'>" + column1
				+ "</th><td colspan='1' width='2%' style='align:left; padding-left:15px;'> </td>"
				+ "<td colspan='3' width='49%' style='align:left; padding-left:15px;'>" + column2 + "</th></tr>";
	}

	/**
	 * Get {@link String} object from {@link String,String}
	 * 
	 * @param column1,column2 {@link String,String}
	 * 
	 * @return Returns a {@link String}
	 */
	private String getRepeatedHtmlCodeValues(String column1, String column2) {
		return "<tr>"
				+ "<td colspan='7' style='align:center;background-color: #313337; vertical-align: middle;'><p style='color: #ffffff;text-align: center;'>Work Pack Summary</p></td>"
				+ "</tr><tr>" + "<td colspan='7'> </td></tr><tr>"
				+ "<td colspan='3' width='49%' style='text-align: left;border:1px;background-color: #A9A9A9; padding-left:15px;'>"
				+ column1 + "</th>" + "<td colspan='1' width='2%' style='align:left; padding-left:15px;'> </td>"
				+ "<td colspan='3' width='49%' style='text-align: left;border:1px;background-color: #A9A9A9; padding-left:15px;'>"
				+ column2 + "</th></tr>";
	}

	/**
	 * Get Next Notification Id
	 * 
	 * @return Returns a {@link Long}
	 */
	private long getNotificationId() {
		Query query = null;
		BigDecimal nextId = null;
		Long startIndex = 1L;
		try {
			query = entityManager.createNativeQuery(DCQueries.MAX_NOTIFICATION_ID);
			nextId = (BigDecimal) query.getSingleResult();
			if (nextId != null) {
				LOGGER.info("Next sequence for the og_drill_ed_notification table : {}", nextId.longValue());
				return nextId.longValue();
			} else {
				return startIndex;
			}
		} catch (Exception e) {
			LOGGER.error("Exception in getNotificationId(), Exception : {}, StackTrace : {}", e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * Get {@link User} from {@link Object[]}
	 * 
	 * @param userObj {@link Object[]}
	 * @return Returns a {@link User}
	 */
	private User getUser(Object[] userObj) {
		User user = new User();
		user.setDrillUserId(userObj[0] != null ? Long.valueOf(userObj[0].toString()) : 0);
		user.setUserId(DCUtils.getValueAsString(userObj[1]));
		user.setName(DCUtils.getValueAsString(userObj[2]));
		user.setEmail(DCUtils.getValueAsString(userObj[3]));
		return user;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getMCDEmailIds(String fleetLevel, String documentName,String docType,Long mcdId) {
		Query query = null;
		List<Object> userEmailIds = null;
		StringBuilder queryStr = new StringBuilder();
		try {
			if (fleetLevel.equals("Y")) {
				queryStr.append(DCQueries.GET_MCD_MAILS_WITH_FLEET_LEVEL);
				getMCDFilter(docType, queryStr);
				
				queryStr.append(DCQueries.GET_MCD_MAILS_WITH_FLEET_LEVEL2);
				
				query = entityManager.createNativeQuery(queryStr.toString());
				userEmailIds = query.getResultList();
			}
			
		} catch (Exception e) {
			LOGGER.error("Exception in getMCDEmailIds(), Exception : {}, StackTrace : {}", e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return userEmailIds;
	}

	private void getMCDFilter(String docType, StringBuilder queryStr) {
		if(docType.equals("MARKETING")) {
			queryStr.append(DCQueries.MARK_FLAG_CONDTN);
			}else if(docType.equals("COMPLIANCE")) {
				queryStr.append(DCQueries.COMP_FLAG_CONDTN);
			}else if(docType.equals("DATASHEET")) {
				queryStr.append(DCQueries.DATA_FLAG_CONDTN);
			}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long getMCDDocId(Long mcdId,String docType) {
		List<Object> docIdList = new ArrayList<>();
		try {
			Query query = entityManager.createNativeQuery(DCQueries.GET_OG_DRILL_DOCUMENT_MCD_ID);
			query.setParameter(1, mcdId);
			query.setParameter(2, docType);
			docIdList = query.getResultList();
		} catch (Exception e) {
			LOGGER.error("Exception in getDoc()", e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		if (docIdList.isEmpty()) {
			LOGGER.warn("Did not find doc with MCD = {}", mcdId ," and docType {}",docType);
			return Long.valueOf(0);
		}
		return Long.valueOf(docIdList.get(0).toString());
	}

}
