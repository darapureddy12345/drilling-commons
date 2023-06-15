package com.bh.drillingcommons.ras.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bh.drillingcommons.entity.oracle.TrainingContact;
import com.bh.drillingcommons.enumerators.Constants;
import com.bh.drillingcommons.exceptions.SystemException;
import com.bh.drillingcommons.models.CustomVideo;
import com.bh.drillingcommons.models.ShareVideo;
import com.bh.drillingcommons.ras.ICommonResourceAccessor;
import com.bh.drillingcommons.ras.ITrainingResourceAccessor;
import com.bh.drillingcommons.util.DCQueries;

@Component("trainingResourceAccessor")
public class TrainingResourceAccessor implements ITrainingResourceAccessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(TrainingResourceAccessor.class);

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<TrainingContact> getTrainingContact(String emailContact) {
		try {
			Query query = entityManager.createNativeQuery(
					"SELECT * FROM OG_DRILL_TRAINING_CONTACT WHERE contact_type = ?1", TrainingContact.class);
			query.setParameter(1, emailContact);
			return query.getResultList();
		} catch (Exception exception) {
			LOGGER.error("Exception while removing notification, Exception : {}", exception);
			throw new SystemException(exception.getMessage(), exception);

		}

	}

	@Autowired
	private ICommonResourceAccessor commonResourceAccessor;

	/**
	 * @see {@link ICommonResourceAccessor#shareVideo(ShareVideo)}
	 */
	@Transactional
	@Override
	public String shareVideo(ShareVideo shareVideo) {
		String response = null;
		try {
			Date today = new Date();
			Long videoRecommendId = commonResourceAccessor
					.getNextSeqNumber(Constants.DRILL_SHARED_VIDEO_SEQ.getValue());
			Query query = entityManager.createNativeQuery(DCQueries.INSERT_OG_DRILL_SHARE_VIDEO);
			query.setParameter(1, videoRecommendId);
			query.setParameter(2, shareVideo.getVideoId());
			query.setParameter(3, shareVideo.getRecommendedBy());
			query.setParameter(4, shareVideo.getRecommendedTo());
			query.setParameter(5, shareVideo.getCreatedBy());
			query.setParameter(6, today);
			query.setParameter(7, shareVideo.getLastUpdatedBy());
			query.setParameter(8, today);
			query.executeUpdate();
			response = String.valueOf(videoRecommendId);
		} catch (Exception e) {
			LOGGER.error("Exception in shareVideo(), Exception : {}, StackTrace : {}", e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return response;
	}

	/**
	 * @see {@link ICommonResourceAccessor#requestCustomVideo(CustomVideo)}
	 */
	@Transactional
	@Override
	public String requestCustomVideo(CustomVideo customVideo) {
		String response = null;
		try {
			Date today = new Date();
			Long drillVideoId = commonResourceAccessor.getNextSeqNumber(Constants.DRILL_VIDEO_REQ_SEQ.getValue());
			Query query = entityManager.createNativeQuery(DCQueries.INSERT_OG_DRILL_VIDEO_REQ);
			query.setParameter(1, drillVideoId);
			query.setParameter(2, customVideo.getRequestorFirstName());
			query.setParameter(3, customVideo.getRequestorLastName());
			query.setParameter(4, customVideo.getUserId());
			query.setParameter(5, customVideo.getRequestorEmail());
			query.setParameter(6, customVideo.getVideoCategory());
			query.setParameter(7, customVideo.getVideoDesc());
			query.setParameter(8, "A");
			query.setParameter(9, customVideo.getCrtdBy());
			query.setParameter(10, today);
			query.setParameter(11, customVideo.getLstUpdtdBy());
			query.setParameter(12, today);
			query.executeUpdate();
			response = String.valueOf(drillVideoId);
		} catch (Exception e) {
			LOGGER.error("Exception in requestCustomVideo(), Exception : {}, StackTrace : {}", e.getMessage(), e);
			throw new SystemException(e.getMessage(), e);
		}
		return response;
	}
}
