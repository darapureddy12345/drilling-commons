package com.bh.drillingcommons.ras;

import java.util.List;

import com.bh.drillingcommons.entity.oracle.Doc;
import com.bh.drillingcommons.entity.oracle.OgDrillWebNotification;
import com.bh.drillingcommons.models.BulletinDetail;
import com.bh.drillingcommons.models.DmlModel;
import com.bh.drillingcommons.models.User;

public interface INotificationResourceAccessor {

	/**
	 * Remove a specific notification
	 * 
	 * @param notfnId {@link String}
	 * 
	 * @return Returns NIL
	 */
	void removeNotification(String notfnId);

	/**
	 * Remove all notifications for a specific user
	 * 
	 * @param userId {@link String}
	 * 
	 * @return Returns NIL
	 */
	void removeAllNotification(String userId);

	/**
	 * Get list of notifications
	 * 
	 * @param userId,readOrNot {@link String,String}
	 * 
	 * @return Returns a {@link List<OgDrillWebNotification>}
	 */
	List<OgDrillWebNotification> getNotificationList(String userId, String readOrNot);

	/**
	 * Mark specific notification as read for a user
	 * 
	 * @param userId,readId {@link String,String}
	 * 
	 * @return Returns a {@link String}
	 */
	String markAsRead(String userId, String readId);

	/**
	 * Fetch rig name by id
	 * 
	 * @param rigId {@link String}
	 * 
	 * @return Returns a {@link String}
	 */
	String getRigName(String rigId);

	/**
	 * Fetch user email for user id
	 * 
	 * @param userIdValues {@link String,boolean}
	 * 
	 * @return Returns a {@link List<String>}
	 */
	List<String> getUserEmailIds(List<String> ssoIds,boolean multipleParams);

	/**
	 * Fetch Email content for DML based on status
	 * 
	 * @param dmlModel,url {@link DmlModel,String}
	 * 
	 * @return Returns a {@link String}
	 */
	String getDMLEmailContentByStatus(DmlModel dmlModel, String url);

	/**
	 * send email
	 * 
	 * @param (fromList,toList,ccList,bccList,emailSubject, emailContent)
	 *                 {@link String, String, String, String ,String,String)}
	 * 
	 * @return Returns a {@link String}
	 */
	String sendEmail(String fromList,String toList, String ccList,String bccList,String emailSubject,String emailContent);

	/**
	 * Mark specific notification as read
	 * 
	 * @param userId {@link String}
	 * 
	 * @return Returns NIL
	 */
	void markAsReadNotification(String notificationId);

	/**
	 * Mark all user notifications as read
	 * 
	 * @param userId {@link String}
	 * 
	 * @return Returns NIL
	 */
	void markAsReadAllNotification(String userId);

	/**
	 * Fetch list of alert notifications for a specific user
	 * 
	 * @param userId {@link String)}
	 * 
	 * @return Returns a {@link List<Object[]>}
	 */
	List<Object[]> getAlertNotificationList(String userId);
	
	/**
	 * Get Bulletin Email Id's
	 * 
	 * @param fleetLevel
	 * 
	 * @param bulletinNumber
	 * 
	 * @return Returns a {@link List<Object[]>}
	 */
	List<Object> getBulletinEmailIds(String fleetLevel, String bulletinNumber);
	
	/**
	 * Save Bulletin Notification
	 * 
	 * @param bulletinDetail
	 *                     {@link BulletinDetail}
	 * @param type
	 *           {@link String}
	 */
	void saveBulletinNotification(BulletinDetail bulletinDetail, String type);
	
	/**
	 * Get Bulletin Document 
	 * 
	 * @param bulletinId
	 *                {@link String}
	 * 
	 * @return Returns a {@link Doc}
	 */
	Long getBulletinDocId(String bulletinId);
	
	/**
	 * Get Users
	 * 
	 * @param query,userIdList
	 * 
	 * @return Returns a Collection of {@link User}
	 */
	List<User> getUsers(String query,String[] userIdList);
	
	/**
	 * Update Notification
	 * 
	 * @param content
	 *              {@link String}
	 * @param subject
	 *              {@link String}
	 */
	void updateNotification(String content, String subject);
	
	/**
	 * Update View Preference
	 * 
	 * @param usersList
	 *                {@link List<User>}
	 */
	void updateviewPreferences(List<User> usersList);

	void mailSender(String[] emailList, String[] ccList, String[] bccList, String fromMail, String subjectLine,
			String mailBody, boolean isHtml);

	List<Object> getMCDEmailIds(String fleetLevel, String documentName, String docType,Long mcdId);

	Long getMCDDocId(Long mcdId, String docType);
}
