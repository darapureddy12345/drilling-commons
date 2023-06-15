package com.bh.drillingcommons.ras;

import java.util.List;

import com.bh.drillingcommons.entity.oracle.TrainingContact;
import com.bh.drillingcommons.models.CustomVideo;
import com.bh.drillingcommons.models.ShareVideo;

public interface ITrainingResourceAccessor {

	/**
	 * Get training contacts
	 * 
	 * @param emailContact {@link String}
	 * 
	 * @return Returns a {@link List<TrainingContact>}
	 */
	List<TrainingContact> getTrainingContact(String emailContact);

	/**
	 * Share Video
	 * 
	 * @param shareVideo {@link ShareVideo}
	 * 
	 * @return Returns a {@link String}
	 */
	String shareVideo(ShareVideo shareVideo);

	/**
	 * Create Custom Video Request
	 * 
	 * @param customVideo {@link CustomVideo}
	 * 
	 * @return Returns a {@link String}
	 */
	String requestCustomVideo(CustomVideo customVideo);

}
