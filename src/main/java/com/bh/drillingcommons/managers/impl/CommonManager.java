package com.bh.drillingcommons.managers.impl;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bh.drillingcommons.entity.oracle.GALookup;
import com.bh.drillingcommons.entity.oracle.GEServiceLocation;
import com.bh.drillingcommons.entity.oracle.GoogleAnalytics;
import com.bh.drillingcommons.entity.oracle.Lookup;
import com.bh.drillingcommons.entity.oracle.LookupValue;
import com.bh.drillingcommons.enumerators.Constants;
import com.bh.drillingcommons.exceptions.BadRequestException;
import com.bh.drillingcommons.exceptions.SystemException;
import com.bh.drillingcommons.managers.ICommonManager;
import com.bh.drillingcommons.models.Feedback;
import com.bh.drillingcommons.models.GoogleAnalyticsModel;
import com.bh.drillingcommons.models.GoogleAnalyticsTotalViewCount;
import com.bh.drillingcommons.models.Region;
import com.bh.drillingcommons.ras.ICommonResourceAccessor;
import com.bh.drillingcommons.repository.ILookupValueRepo;
import com.bh.drillingcommons.util.DCUtils;
import com.bh.drillingcommons.util.MessageUtil;
import com.google.gson.Gson;

@Service
public class CommonManager implements ICommonManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonManager.class);

	@Autowired
	private ICommonResourceAccessor commonAccessor;

	@Autowired
	private ILookupValueRepo iLookupRepo;

	/**
	 * @see {@link ICommonManager#getServiceLocations(String)}
	 */
	@Override
	@Cacheable(value = "servicelocations", cacheManager = "appCacheManager", unless = "#result == null")
	public List<GEServiceLocation> getServiceLocations() {
		try {
			List<GEServiceLocation> serviceLocations = this.commonAccessor.getServiceLocations("dashboard");
			for (GEServiceLocation loc : serviceLocations) {
				if (loc.getContactMobile() != null) {
					loc.setContactMobile(loc.getContactMobile() + " (cell)");
				}
				if (loc.getContactTelephone() != null) {
					loc.setContactTelephone(loc.getContactTelephone() + " (desk)");
				}
				if (loc.getContactFax() != null) {
					loc.setContactFax(loc.getContactFax() + " (fax)");
				}
			}
			return serviceLocations;
		} catch (Exception e) {
			LOGGER.error("Exception while fetching preferences {}", e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link ICommonManager#getGoogleAnalyticsLookUp(String)}
	 */
	@Override
	@Cacheable(value = "galookup", cacheManager = "appCacheManager", unless = "#result == null")
	public Map<String, Map<String, GALookup>> getGoogleAnalyticsLookUp() {
		try {
			List<GALookup> lookupValues = commonAccessor.getGoogleAnalyticsLkp();
			Map<String, Map<String, GALookup>> resultLookupMap = new HashMap<>();
			Map<String, GALookup> lookupMap = new HashMap<>();
			for (int i = 0; i < lookupValues.size(); i++) {
				if(lookupValues.get(i) != null && lookupValues.get(i).getEventCode() != null){
					lookupMap.put(lookupValues.get(i).getEventCode(), lookupValues.get(i));
				}
			}
			resultLookupMap.put("gaLookup", lookupMap);
			return resultLookupMap;
		} catch (Exception e) {
			LOGGER.error("Exception while fetching ga lookups {}", e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link ICommonManager#getLookupValues(String)}
	 */
	@Override
	@Cacheable(value = "lookupvalid", cacheManager = "appCacheManager", unless = "#result == null", key = "#lookupType")
	public Map<String, List<LookupValue>> getLookupValues(String lookupType) {
		if (StringUtils.isEmpty(lookupType))
			throw new BadRequestException("Error: Invalid value <empty string> for : lookupType");
		try {
			Map<String, List<LookupValue>> lookupMap = new HashMap<>();
			lookupMap.put(Constants.LOOKUP_KEY.getValue(), iLookupRepo.findByTypeAndActiveFlag(lookupType, Constants.ACTIVE_FLAG_Y.getValue()));
			return lookupMap;
		} catch (BadRequestException bre) {
			throw bre;
		} catch (Exception e) {
			LOGGER.error("Exception while fetching lookups {}", e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link ICommonManager#getMultipleLookupValues(String)}
	 */
	@Override
	@Cacheable(value = "multiplelookupvalids", cacheManager = "appCacheManager", unless = "#result == null", key = "#lookupTypes")
	public Map<String, List<LookupValue>> getMultipleLookupValues(String lookupTypes) {
		if (StringUtils.isEmpty(lookupTypes))
			throw new BadRequestException("Error: Invalid value <empty string> for : lookupTypes");
		List<String> lookupTypeList = Arrays.asList(lookupTypes.split(","));
		try {
			Map<String, List<LookupValue>> lookupMap = new HashMap<>();
			lookupMap.put(Constants.LOOKUP_KEY.getValue(), iLookupRepo.findByTypeIn(lookupTypeList));
			return lookupMap;
		} catch (BadRequestException bre) {
			throw bre;
		} catch (Exception e) {
			LOGGER.error("Exception while fetching multiple lookups {}", e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link ICommonManager#getLicenseeORRegionList()}
	 */
	@Override
	public List<LookupValue> getLicenseeORRegionList() {
		try {
			return iLookupRepo.findByType(Constants.OR_FORM_SITE_REGION.getValue());
		} catch (Exception e) {
			LOGGER.error("Exception while fetching Licensee OR RegionList : {}", e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link ICommonManager#getRegionList()}
	 */
	@Override
	@Cacheable(value = "allregions", cacheManager = "appCacheManager", unless = "#result == null")
	public Map<String, List<Region>> getRegionList() {
		List<Region> regionList = new ArrayList<>();
		Map<String, List<Region>> result = new HashMap<>();
		try {
			List<Object[]> regions = commonAccessor.getRegionList();
			for (Object[] region : regions)
				regionList.add(getRegion(region));
			result.put(Constants.LOOKUP_KEY.getValue(), regionList);
			return result;
		} catch (Exception e) {
			LOGGER.error("Exception while fetching RegionList : {}", e);
			throw new SystemException(e.getMessage(), e);
		}
	}

	/**
	 * @see {@link ICommonManager#postFeedbackReportIssue(String,String,InputStream)}
	 */
	@Override
	public String postFeedbackReportIssue(String jsonRequest, String fileName, InputStream inputStream) {
		String[] emailList = null;
		String feedbackEmailSendFrom = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		StringBuilder toEmail = new StringBuilder();

		try {
			Feedback feedback = new Gson().fromJson(jsonRequest, Feedback.class);
			if (feedback != null && feedback.getFeedbackName() != null
					&& !"".equals(feedback.getFeedbackName().trim())) {

				List<Lookup> lookups = commonAccessor.getLookupValues(Constants.FEEDBACK_TO_MAIL.getValue());
				for (Lookup lookup : lookups) {
					toEmail.append(",");
					toEmail.append(lookup.getValue());

				}
				lookups = commonAccessor.getLookupValues(Constants.FEEDBACK_FROM_MAIL.getValue());
				for (Lookup lookup : lookups) {
					feedbackEmailSendFrom = lookup.getValue();

				}
				emailList = DCUtils.convertStringToArray(toEmail.toString());
				String body = new StringBuilder().append(Constants.FEEDBACK_EMAIL_BODY.getValue()).append("\r\n\r\n")
						.append(feedback.getFeedbackName()).append("\r\n\r\n")
						.append(Constants.FEEDBACK_USER.getValue()).append("\r\n First Name : ")
						.append(feedback.getFirstName()).append("\r\n Last Name : ").append(feedback.getLastName())
						.append("\r\n Company Name : ").append(feedback.getCompanyName()).append("\r\n User Id : ")
						.append(feedback.getUserId()).append("\r\n Email Id : ").append(feedback.getEmailId())
						.append("\r\n\n Date : ").append(dateFormat.format(new Date()))
						.append("\r\n\r\n Your engageDrilling Support Team")
						.append("\r\n Powered by Baker Hughes \r\n")
						.append(System.getenv(MessageUtil.APP_URL)).toString();
				commonAccessor.emailFeedback(emailList, body, Constants.FEEDBACK_EMAIL_SUBJECT.getValue(), inputStream,
						feedbackEmailSendFrom, fileName);

			} else {
				throw new IllegalArgumentException("Feedback content cann't be Empty");
			}
		} catch (BadRequestException bre) {
			throw new BadRequestException("Feedback content cann't be Empty");
		} catch (Exception e) {
			LOGGER.error("Exception while posting feedback : {}", e);
			throw new SystemException(e.getMessage(), e);
		}

		return "success";
	}

	/**
	 * @see {@link ICommonManager#getViewCount(String, Date, Date)}
	 */
	@Override
	public List<GoogleAnalyticsModel> getViewCount(String eventCategory, Date startDate, Date endDate) {
		if (StringUtils.isEmpty(eventCategory) || startDate == null || endDate == null) {
			throw new BadRequestException("Invalid Request");
		}
		List<GoogleAnalyticsModel> getGoogleAnalyticsModels = new ArrayList<>();
		List<GoogleAnalytics> googleAnalyticsList = commonAccessor.getViewCount(eventCategory, startDate, endDate);
		for (GoogleAnalytics googleAnalytics : googleAnalyticsList)
			getGoogleAnalyticsModels.add(getGoogleAnalyticsModel(googleAnalytics));
		return getGoogleAnalyticsModels;
	}

	/**
	 * @see {@link ICommonManager#getAllViewCount(String)}
	 */
	@Override
	public List<GoogleAnalyticsTotalViewCount> getAllViewCount(String eventCategory) {
		if (StringUtils.isEmpty(eventCategory)) {
			throw new BadRequestException("Invalid Request");
		}
		List<GoogleAnalyticsTotalViewCount> getGoogleAnalyticsViewCount = new ArrayList<>();
		List<Object[]> googleAnalyticsList = commonAccessor.getAllViewCount(eventCategory);
		for (Object[] googleAnalytics : googleAnalyticsList)
			getGoogleAnalyticsViewCount.add(getGoogleAnalyticsTotalViewCount(googleAnalytics));
		return getGoogleAnalyticsViewCount;
	}

	/**************** Private Methods **************************************/

	/**
	 * Get Region
	 * 
	 * @param object {@link Region}
	 * 
	 * @return Returns a {@link Region}
	 */
	private Region getRegion(Object[] object) {
		Region region = new Region();
		region.setRegionId(object[0] == null ? null : Long.valueOf(object[0].toString()));
		region.setRegionCode(object[1] == null ? null : object[1].toString());
		region.setRegionDesc(object[2] == null ? null : object[2].toString());
		return region;
	}

	/**
	 * Get {@link GoogleAnalyticsModel} object from {@link GoogleAnalytics}
	 * 
	 * @param googleAnalytics {@link GoogleAnalytics}
	 * 
	 * @return Returns a {@link GoogleAnalyticsModel}
	 */
	private GoogleAnalyticsModel getGoogleAnalyticsModel(GoogleAnalytics googleAnalytics) {
		GoogleAnalyticsModel googleAnalyticsModel = new GoogleAnalyticsModel();
		googleAnalyticsModel.setId(googleAnalytics.getId());
		googleAnalyticsModel.setEventAction(googleAnalytics.getEventAction());
		googleAnalyticsModel.setEventCategory(googleAnalytics.getEventCategory());
		googleAnalyticsModel.setEventDate(googleAnalytics.getEventDate());
		googleAnalyticsModel.setTotalEvents(googleAnalytics.getTotalEvents());
		return googleAnalyticsModel;
	}

	/**
	 * Get {@link GoogleAnalyticsTotalViewCount} from {@link Object[]}
	 * 
	 * @param object {@link Object[]}
	 * 
	 * @return
	 */
	private GoogleAnalyticsTotalViewCount getGoogleAnalyticsTotalViewCount(Object[] object) {
		GoogleAnalyticsTotalViewCount googleAnalyticsViewCount = new GoogleAnalyticsTotalViewCount();
		googleAnalyticsViewCount.setId(object[0] == null ? null : object[0].toString());
		googleAnalyticsViewCount.setTotalEvents(object[1] == null ? null : (Number) object[1]);
		return googleAnalyticsViewCount;
	}
	
	/**
	 * @see {@link ICommonManager#postChatBotFeedbackReportIssue(String)}
	 */
	@Override
	public String postChatBotFeedbackReportIssue(String jsonRequest) {
		String[] emailList = null;
		String feedbackEmailSendFrom = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		StringBuilder toEmail = new StringBuilder();
		try {
			Feedback feedback = new Gson().fromJson(jsonRequest, Feedback.class);
			if (feedback != null && feedback.getFeedbackName() != null
					&& !"".equals(feedback.getFeedbackName().trim())) {
                List<Lookup> lookups = commonAccessor.getLookupValues(Constants.FEEDBACK_TO_MAIL.getValue());
				for (Lookup lookup : lookups) {
					toEmail.append(",");
					toEmail.append(lookup.getValue());
				  }
				lookups = commonAccessor.getLookupValues(Constants.BUSINESS_CC_CHATBOT.getValue());
				for (Lookup lookup : lookups) {
					toEmail.append(",");
					toEmail.append(lookup.getValue());
				  }
				lookups = commonAccessor.getLookupValues(Constants.FEEDBACK_FROM_MAIL.getValue());
				for (Lookup lookup : lookups) {
					feedbackEmailSendFrom = lookup.getValue();
				  }
				emailList = DCUtils.convertStringToArray(toEmail.toString());
				String body = new StringBuilder().append(Constants.FEEDBACK_EMAIL_BODY.getValue()).append("\r\n\r\n")
						.append("Question Asked: ")
						.append(feedback.getReportcomment()).append("\r\n\r\n")
                        .append(Constants.FEEDBACK_USER.getValue()).append("\r\n First Name : ")
						.append(feedback.getFirstName()).append("\r\n Last Name : ").append(feedback.getLastName())
						.append("\r\n Company Name : ").append(feedback.getCompanyName()).append("\r\n User Id : ")
						.append(feedback.getUserId()).append("\r\n Email Id : ").append(feedback.getEmailId())
						.append("\r\n\n Date : ").append(dateFormat.format(new Date()))
						.append("\r\n\r\n Your engageDrilling Support Team").append("\r\n Powered by Baker Hughes \r\n")
						.append(System.getenv(MessageUtil.APP_URL)).toString();
			    this.commonAccessor.emailFeedback(emailList, body, Constants.CHATBOT_FEEDBACK_EMAIL_SUBJECT.getValue(), null,
						feedbackEmailSendFrom, null);
            } else {
				throw new IllegalArgumentException("Feedback content cann't be Empty");
			    }
		} catch (BadRequestException bre) {
			throw new BadRequestException("Feedback content cann't be Empty");
		} catch (Exception e) {
			LOGGER.error("Exception while posting feedback : {}", e);
			throw new SystemException(e.getMessage(), e);
		}
		return Constants.CHATBOT_FEEDBACK_SUCCESS.getValue();
	}	
}