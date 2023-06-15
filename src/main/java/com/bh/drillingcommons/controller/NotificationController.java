package com.bh.drillingcommons.controller;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bh.drillingcommons.entity.oracle.OgDrillWebNotification;
import com.bh.drillingcommons.exceptions.BadRequestException;
import com.bh.drillingcommons.exceptions.SystemException;
import com.bh.drillingcommons.managers.INotificationManager;
import com.bh.drillingcommons.models.BulletinDetail;
import com.bh.drillingcommons.models.DmlModel;
import com.bh.drillingcommons.models.Email;
import com.bh.drillingcommons.models.Feedback;
import com.bh.drillingcommons.models.MarkCompDataDtls;
import com.bh.drillingcommons.models.NotificationRelease;
import com.bh.drillingcommons.models.UserNotificationModel;
import com.bh.drillingcommons.util.AuthorizationUtility;
import com.bh.drillingcommons.util.DCUtils;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping({ "api/notification/v1/", "notification/api/v1/" })
public class NotificationController {

	@Autowired
	private INotificationManager notificationManager;

	@ApiOperation(value = "removeNotification", nickname = "Remove notification", produces = "application/json", notes = "This route will remove notification")
	@GetMapping("rmvNotifctn/{notfnId}")
	public ResponseEntity<Map<String, String>> removeNotification(@PathVariable("notfnId") String notfnId) {
		try {
			AuthorizationUtility.isAuthorized(null, null, null);
			String normalNotifId = Normalizer.normalize(notfnId, Form.NFKC);
			return new ResponseEntity<>(this.notificationManager.removeNotification(normalNotifId), HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "removeAllNotification", nickname = "Remove all notifications", produces = "application/json", notes = "This route will remove all notifications for a user")
	@GetMapping("removeAllNotification/{userID}")
	public ResponseEntity<Map<String, String>> removeAllNotification(@PathVariable("userID") String userId) {
		try {
			AuthorizationUtility.isAuthorized(null, null, null);
			String normalUserId = Normalizer.normalize(userId, Form.NFKC);
			return new ResponseEntity<>(this.notificationManager.removeAllNotification(normalUserId), HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "getNotificationList", nickname = "Get notification list by user", produces = "application/json", notes = "This route will return the list of all notifications by specific user id.")
	@GetMapping("notification/notificationList/{userId}/{readOrNot}")
	public ResponseEntity<Map<String, List<OgDrillWebNotification>>> getNotificationList(
			@PathVariable("userId") String userId, @PathVariable("readOrNot") String readOrNot) {
		try {
			AuthorizationUtility.isAuthorized(null, null, null);
			String normalUserId = Normalizer.normalize(userId, Form.NFKC);
			String normalReadOrNot = Normalizer.normalize(readOrNot, Form.NFKC);
			return new ResponseEntity<>(this.notificationManager.getNotificationList(normalUserId, normalReadOrNot),
					HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "markAsRead/{userId}/{readId}", nickname = "Mark Notification as Read", produces = "application/json", notes = "This route will helps to mark notification as read.")
	@PutMapping("markAsRead/{userId}/{readId}")
	public ResponseEntity<Map<String, String>> markNotificationAsRead(@PathVariable("userId") String userId,
			@PathVariable("readId") String readId) {
		try {
			AuthorizationUtility.isAuthorized(64, null, null);
			String normalUserId = Normalizer.normalize(userId, Form.NFKC);
			String normalReadId = Normalizer.normalize(readId, Form.NFKC);
			return new ResponseEntity<>(this.notificationManager.markNotificationAsRead(normalUserId, normalReadId),
					HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "sendCSAReportEmail", nickname = "Send CSA Email reports", produces = "application/json", notes = "This route will send emails of CSA Reports of specific rig to list of users.")
	@PostMapping("sendEmail/{emailLookupValue}/{rigId}")
	public ResponseEntity<Map<String, String>> sendCSAReportEmail(@RequestBody Email emailSettings,
			@PathVariable("emailLookupValue") String emailLookupValue, @PathVariable("rigId") String rigId) {
		try {
			AuthorizationUtility.isAuthorized(null, null, null);
			Email sanitizedEmail = new Email();
			sanitizedEmail.setFrom((DCUtils.isNullOrEmpty(emailSettings.getFrom()))?null:Normalizer.normalize(emailSettings.getFrom(), Form.NFKC));
			sanitizedEmail.setTo(DCUtils.isNullOrEmpty(emailSettings.getTo())?null:Normalizer.normalize(emailSettings.getTo(), Form.NFKC));
			sanitizedEmail.setSubject(DCUtils.isNullOrEmpty(emailSettings.getSubject())?null:Normalizer.normalize(emailSettings.getSubject(), Form.NFKC));
			sanitizedEmail.setBody(DCUtils.isNullOrEmpty(emailSettings.getBody())?null:Normalizer.normalize(emailSettings.getBody(), Form.NFKC));
			
			String normalemailLkpValue = DCUtils.isNullOrEmpty(emailLookupValue)?null:Normalizer.normalize(emailLookupValue, Form.NFKC);
			String normalizedRigId = DCUtils.isNullOrEmpty(rigId)?null:Normalizer.normalize(rigId, Form.NFKC);
			return new ResponseEntity<>(
					this.notificationManager.sendEmail(sanitizedEmail, normalemailLkpValue, normalizedRigId),
					HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException se) {
			throw se;
		}

	}

	@ApiOperation(value = "sendDmlEmail", nickname = "Send DML Email", produces = "application/json", notes = "This route will send emails of DML workpack based on status to list of users.")
	@PostMapping("sendDmlEmail")
	public ResponseEntity<String> sendDMLEmail(@RequestBody DmlModel dmlModel,
			@QueryParam("loginURL") String loginURL) {
		try {
			AuthorizationUtility.isAuthorized(1, null, null);
			String normalLoginURL = (DCUtils.isNullOrEmpty(loginURL))?null:Normalizer.normalize(loginURL, Form.NFKC);
			DmlModel sanitizedDmlModel = new DmlModel();
			sanitizedDmlModel.setLastUpdatedBySSO((DCUtils.isNullOrEmpty(dmlModel.getLastUpdatedBySSO()))?null:Normalizer.normalize(dmlModel.getLastUpdatedBySSO(), Form.NFKC));
			sanitizedDmlModel.setDateUpdated((DCUtils.isNullOrEmpty(dmlModel.getDateUpdated()))?null:Normalizer.normalize(dmlModel.getDateUpdated(), Form.NFKC));
			sanitizedDmlModel.setLastUpdatedBy((DCUtils.isNullOrEmpty(dmlModel.getLastUpdatedBy()))?null:Normalizer.normalize(dmlModel.getLastUpdatedBy(), Form.NFKC));
			sanitizedDmlModel.setParentAssembly((DCUtils.isNullOrEmpty(dmlModel.getParentAssembly()))?null:Normalizer.normalize(dmlModel.getParentAssembly(), Form.NFKC));
			sanitizedDmlModel.setChildAssembly((DCUtils.isNullOrEmpty(dmlModel.getChildAssembly()))?null:Normalizer.normalize(dmlModel.getChildAssembly(), Form.NFKC));
			sanitizedDmlModel.setDiscipline((DCUtils.isNullOrEmpty(dmlModel.getDiscipline()))?null:Normalizer.normalize(dmlModel.getDiscipline(), Form.NFKC));
			sanitizedDmlModel.setMaintenanceInterval((DCUtils.isNullOrEmpty(dmlModel.getMaintenanceInterval()))?null:Normalizer.normalize(dmlModel.getMaintenanceInterval(), Form.NFKC));
			sanitizedDmlModel.setTaskName((DCUtils.isNullOrEmpty(dmlModel.getTaskName()))?null:Normalizer.normalize(dmlModel.getTaskName(), Form.NFKC));
			sanitizedDmlModel.setWorkPackStatus((DCUtils.isNullOrEmpty(dmlModel.getWorkPackStatus()))?null:Normalizer.normalize(dmlModel.getWorkPackStatus(), Form.NFKC));
			sanitizedDmlModel.setId((DCUtils.isNullOrEmpty(dmlModel.getId()))?null:Normalizer.normalize(dmlModel.getId(), Form.NFKC));
			sanitizedDmlModel.setComments((DCUtils.isNullOrEmpty(dmlModel.getComments()))?null:Normalizer.normalize(dmlModel.getComments(), Form.NFKC));
			sanitizedDmlModel.setCustomers((DCUtils.isNullOrEmpty(dmlModel.getCustomers()))?null:Normalizer.normalize(dmlModel.getCustomers(), Form.NFKC));
			sanitizedDmlModel.setRigs((DCUtils.isNullOrEmpty(dmlModel.getRigs()))?null:Normalizer.normalize(dmlModel.getRigs(), Form.NFKC));
			return new ResponseEntity<>(this.notificationManager.sendDmlEmail(sanitizedDmlModel, normalLoginURL),
					HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException se) {
			throw se;
		}

	}

	@ApiOperation(value = "alertNotificationList", nickname = "Alert Notification List", produces = "application/json", notes = "This route will alert all notifactions")
	@GetMapping("alertNotificationList/{userId}")
	public ResponseEntity<List<UserNotificationModel>> alertNotificationList(@PathVariable("userId") String userId) {
		try {
			AuthorizationUtility.isAuthorized(2, null, null);
			String normalUserId = Normalizer.normalize(userId, Form.NFKC);
			return new ResponseEntity<>(this.notificationManager.getAlertNotificationList(normalUserId), HttpStatus.OK);
		} catch (BadRequestException br) {
			throw br;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "markAsReadNotification", nickname = "Mark notification as read", produces = "application/json", notes = "This route will mark notification as read")
	@PutMapping("markAsReadNotification/{notificationId}")
	public ResponseEntity<Map<String, String>> markAsReadNotification(
			@PathVariable("notificationId") String notificationId) {
		try {
			AuthorizationUtility.isAuthorized(null, null, null);
			String normalNotifId = Normalizer.normalize(notificationId, Form.NFKC);
			return new ResponseEntity<>(this.notificationManager.markAsReadNotification(normalNotifId), HttpStatus.OK);
		} catch (BadRequestException br) {
			throw br;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "removeAllNotification", nickname = "Mark Notifications a read", produces = "application/json", notes = "This route will mark all notifications as read")
	@PutMapping("markAsReadAllNotification/{userId}")
	public ResponseEntity<Map<String, String>> markAsReadAllNotification(@PathVariable("userId") String userId) {
		try {
			AuthorizationUtility.isAuthorized(null, null, null);
			String normalUserId = Normalizer.normalize(userId, Form.NFKC);
			return new ResponseEntity<>(this.notificationManager.markAsReadAllNotification(normalUserId),
					HttpStatus.OK);
		} catch (BadRequestException br) {
			throw br;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "sendBullEmails", nickname = "send bulletin emails", produces = "application/json", notes = "This route will helps to send bulletin emails")
	@PostMapping("sendBullEmails/{type}")
	public ResponseEntity<Map<String, String>> sendBulletinEmails(@PathVariable("type") String type,
			@RequestBody BulletinDetail bulletinDetail) {
		try {
			AuthorizationUtility.isAuthorized(1, null, null);
			String normalizedType = Normalizer.normalize(type, Form.NFKC);

			return new ResponseEntity<>(this.notificationManager.sendBulletinEmails(normalizedType, bulletinDetail),
					HttpStatus.OK);
		} catch (BadRequestException br) {
			throw br;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "sendNotificationRelease", nickname = "Send Notification Release", produces = "application/json", notes = "This route will helps to send notification releases")
	@PostMapping("sendNotificationRelease")
	public ResponseEntity<String> sendNotificationRelease(@RequestBody NotificationRelease notificationRelease,@QueryParam("triggerer") String triggerer) {
		try {
			AuthorizationUtility.isAuthorized(64, null, null);
			return new ResponseEntity<>(this.notificationManager.sendNotificationRelease(notificationRelease,triggerer),
					HttpStatus.OK);
		} catch (BadRequestException br) {
			throw br;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "postfeedback", nickname = "Send post feedback", produces = "application/json", notes = "This route will helps to send post feedback notification")
	@PostMapping("feedback/postfeedback")
	public ResponseEntity<String> postFeedback(@RequestBody Feedback feedback) {
		try {
			return new ResponseEntity<>(this.notificationManager.postFeedback(feedback), HttpStatus.OK);
		} catch (BadRequestException br) {
			throw br;
		} catch (SystemException se) {
			throw se;
		}
	}
	
	@ApiOperation(value = "sendMCDEmails", nickname = "send marketing/compliance/datasheet emails", produces = "application/json", notes = "This route will helps to send marketing/compliance/datasheet emails")
	@PostMapping("sendMCDEmails/{type}")
	public ResponseEntity<Map<String, String>> sendMCDEmails(@PathVariable("type") String type,
			@RequestBody MarkCompDataDtls markCompDataDtls) {
		try {
			String normalizedType = Normalizer.normalize(type, Form.NFKC);

			return new ResponseEntity<>(this.notificationManager.sendMCDEmails(normalizedType, markCompDataDtls),
					HttpStatus.OK);
		} catch (BadRequestException br) {
			throw br;
		} catch (SystemException se) {
			throw se;
		}
	}
}
