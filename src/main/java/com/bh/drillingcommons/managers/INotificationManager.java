package com.bh.drillingcommons.managers;

import java.util.List;
import java.util.Map;

import com.bh.drillingcommons.entity.oracle.OgDrillWebNotification;
import com.bh.drillingcommons.models.BulletinDetail;
import com.bh.drillingcommons.models.DmlModel;
import com.bh.drillingcommons.models.Email;
import com.bh.drillingcommons.models.Feedback;
import com.bh.drillingcommons.models.MarkCompDataDtls;
import com.bh.drillingcommons.models.NotificationRelease;
import com.bh.drillingcommons.models.UserNotificationModel;

public interface INotificationManager {

	/**
	 * Remove a specific notification
	 * 
	 * @param notfnId {@link String}
	 * 
	 * @return Returns a {@link Map<String, String>}
	 */
	Map<String, String> removeNotification(String notfnId);

	/**
	 * Remove all notifications for a specific user
	 * 
	 * @param userId {@link String}
	 * 
	 * @return Returns a {@link Map<String, String>}
	 */
	Map<String, String> removeAllNotification(String userId);

	/**
	 * Get list of notifications
	 * 
	 * @param userId,readOrNot {@link String,String}
	 * 
	 * @return Returns a {@link Map<String, List<OgDrillWebNotification>>}
	 */
	Map<String, List<OgDrillWebNotification>> getNotificationList(String userId, String readOrNot);

	/**
	 * Mark specific notification as read for a user
	 * 
	 * @param userId,readId {@link String,String}
	 * 
	 * @return Returns a {@link Map<String, String>}
	 */
	Map<String, String> markNotificationAsRead(String userId, String readId);

	/**
	 * Send rigwise email for CSA
	 * 
	 * @param emailSettings,emailLookupValue,rigId {@link Email,String,String}
	 * 
	 * @return Returns a {@link Map<String, String>}
	 */
	Map<String, String> sendEmail(Email emailSettings, String emailLookupValue, String rigId);

	/**
	 * send email for DML
	 * 
	 * @param dmlModel,loginUrl {@link DmlModel,String}
	 * 
	 * @return Returns a {@link String}
	 */
	String sendDmlEmail(DmlModel dmlModel, String loginUrl);

	/**
	 * Get list of alert notifications for a specific user
	 * 
	 * @param userId {@link String}
	 * 
	 * @return Returns a {@link List<UserNotificationModel>}
	 */
	List<UserNotificationModel> getAlertNotificationList(String userId);

	/**
	 * Mark all notifications as read
	 * 
	 * @param userId {@link String}
	 * 
	 * @return Returns a {@link Map<String, String>}
	 */
	Map<String, String> markAsReadAllNotification(String userId);

	/**
	 * Mark notification as read
	 * 
	 * @param notificationId {@link String}
	 * 
	 * @return Returns a {@link Map<String, String>}
	 */
	Map<String, String> markAsReadNotification(String notificationId);

	/**
	 * Send Bulletin Mails
	 * 
	 * @param normalizedType {@link String}
	 * 
	 * @param bulletinDetail {@link BulletinDetail}
	 * 
	 * @return Returns a {@link Map<String, String>}
	 */
	Map<String, String> sendBulletinEmails(String normalizedType, BulletinDetail bulletinDetail);

	/**
	 * Send Notification Release
	 * 
	 * @param notificationRelease {@link NotificationRelease}
	 * @param triggerer 
	 * 
	 * @return Returns a {@link String}
	 */
	String sendNotificationRelease(NotificationRelease notificationRelease, String triggerer);

	/**
	 * Post Feedback
	 * 
	 * @param feedback {@link Feedback}
	 * 
	 * @return Returns a {@link String}
	 */
	String postFeedback(Feedback feedback);

	Map<String, String> sendMCDEmails(String normalizedType, MarkCompDataDtls markCompDataDtls);
}
