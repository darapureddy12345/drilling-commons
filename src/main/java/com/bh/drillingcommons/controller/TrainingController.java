package com.bh.drillingcommons.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bh.drillingcommons.entity.oracle.OnlineTraining;
import com.bh.drillingcommons.enumerators.Constants;
import com.bh.drillingcommons.exceptions.BadRequestException;
import com.bh.drillingcommons.exceptions.SystemException;
import com.bh.drillingcommons.managers.ITrainingManager;
import com.bh.drillingcommons.models.CourseRegistration;
import com.bh.drillingcommons.models.CourseRequest;
import com.bh.drillingcommons.models.CustomVideo;
import com.bh.drillingcommons.models.ShareVideo;
import com.bh.drillingcommons.util.AuthorizationUtility;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping({ "api/training/v1/", "training/api/v1/" })
public class TrainingController {

	@Autowired
	ITrainingManager trainingManager;

	@ApiOperation(value = "requestRegistration", nickname = "Request Course registration", produces = "application/json", notes = "This route will let users request for a specific course registration.")
	@PostMapping("course/requestRegistration")
	public ResponseEntity<String> requestRegistrationForCourse(@RequestBody CourseRegistration courseRegDetails) {
		try {
			AuthorizationUtility.isAuthorized(1, courseRegDetails.getCustomerId(), null);
			return new ResponseEntity<>(this.trainingManager.requestCourseRegistration(courseRegDetails,
					Constants.TRAINING_COURSE_REGISTRATION_REQUEST), HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "requestWaitlist", nickname = "Request Course wailist", produces = "application/json", notes = "This route will let users request for a specific course waitlisting.")
	@PostMapping("course/requestWaitlist")
	public ResponseEntity<String> requestWaitlistForCourse(@RequestBody CourseRegistration courseRegDetails) {
		try {
			AuthorizationUtility.isAuthorized(51, courseRegDetails.getCustomerId(), null);
			return new ResponseEntity<>(this.trainingManager.requestCourseRegistration(courseRegDetails,
					Constants.TRAINING_COURSE_WAITLIST_REQUEST), HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "Share Video", nickname = "Share Video and Send Email Notification", produces = "application/json", notes = "This route will helps to share video and send email notification")
	@PostMapping("shareVideo")
	public ResponseEntity<String> shareVideo(@RequestBody ShareVideo shareVideo) {
		try {
			AuthorizationUtility.isAuthorized(53, null, null);	
			return new ResponseEntity<>(trainingManager.shareVideo(shareVideo), HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "requestCourse", nickname = "Request custom course", produces = "application/json", notes = "This route will let users request for a custom course.")
	@PostMapping("course/requestCustomCourse")
	public ResponseEntity<String> requestCustomCourse(@RequestBody CourseRequest courseRequest) {
		try {
			AuthorizationUtility.isAuthorized(55, courseRequest.getCustomerId(), null);
			return new ResponseEntity<>(this.trainingManager.requestCustomCourse(courseRequest), HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "Request to create ustom video", nickname = "Request for creating custom video", produces = "application/json", notes = "This route will helps to request custom video and send email notification")
	@PostMapping("requestCustomVideo")
	public ResponseEntity<String> shareVideo(@RequestBody CustomVideo customVideo) {
		try {
			AuthorizationUtility.isAuthorized(56, null, null);
			return new ResponseEntity<>(trainingManager.requestCustomVideo(customVideo), HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "Request to get list of available Online Training", nickname = "Request to get list of available Online Training", produces = "application/json", notes = "This route returns the list of available Online Training")
	@GetMapping("onlineTrainings")
	public ResponseEntity<List<OnlineTraining>> getOnlineTraining () {
		try {
			AuthorizationUtility.isAuthorized(53, null, null);
			return new ResponseEntity<>(trainingManager.getOnlineTraining(), HttpStatus.OK);			
		} catch (SystemException se) {
			throw se;
		}
	}

}
