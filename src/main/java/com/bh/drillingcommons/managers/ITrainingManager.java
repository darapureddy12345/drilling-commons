package com.bh.drillingcommons.managers;

import java.util.List;

import com.bh.drillingcommons.entity.oracle.OnlineTraining;
import com.bh.drillingcommons.enumerators.Constants;
import com.bh.drillingcommons.models.CourseRegistration;
import com.bh.drillingcommons.models.CourseRequest;
import com.bh.drillingcommons.models.CustomVideo;
import com.bh.drillingcommons.models.ShareVideo;

public interface ITrainingManager {

	/**
	 * Request registration to a specific course
	 * 
	 * @param courseRegDetails,courseRequestType {@link CourseRegistration,Constants}
	 * 
	 * @return Returns a {@link String}
	 */
	String requestCourseRegistration(CourseRegistration courseRegDetails, Constants courseRequestType);

	/**
	 * Request for a custom course
	 * 
	 * @param courseRequest {@link CourseRequest}
	 * 
	 * @return Returns a {@link String}
	 */
	String requestCustomCourse(CourseRequest courseRequest);

	/**
	 * Share Video and send email
	 * 
	 * @param shareVideo {@link ShareVideo
	 * 
	 * @return Returns a {@link String}
	 */
	String shareVideo(ShareVideo shareVideo);

	/**
	 * Request to create a custom video and send email notification
	 * 
	 * @param customVideo {@link CustomVideo}
	 * 
	 * @return Returns a {@link String}
	 */
	String requestCustomVideo(CustomVideo customVideo);

	List<OnlineTraining> getOnlineTraining();
}
