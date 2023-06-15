package com.bh.drillingcommons.managers;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bh.drillingcommons.entity.oracle.GALookup;
import com.bh.drillingcommons.entity.oracle.GEServiceLocation;
import com.bh.drillingcommons.entity.oracle.LookupValue;
import com.bh.drillingcommons.models.GoogleAnalyticsModel;
import com.bh.drillingcommons.models.GoogleAnalyticsTotalViewCount;
import com.bh.drillingcommons.models.Region;

public interface ICommonManager {

	/**
	 * Get Service Locations
	 * 
	 * @param NIL
	 * 
	 * @return Returns a {@link List<GEServiceLocation>}
	 */
	List<GEServiceLocation> getServiceLocations();

	/**
	 * Get Google analytics lookup values
	 * 
	 * @param NIL
	 * 
	 * @return Returns a {@link Map<String, Map<String, GALookup>>}
	 */
	Map<String, Map<String, GALookup>> getGoogleAnalyticsLookUp();

	/**
	 * Fetch Lookup values
	 * 
	 * @param lookupType {@link String}
	 * 
	 * @return Returns a {@link Map<String, List<LookupValue>>}
	 */
	Map<String, List<LookupValue>> getLookupValues(String lookupType);

	/**
	 * Fetch multiple lookup values
	 * 
	 * @param lookupTypes {@link String}
	 * 
	 * @return Returns a {@link Map<String, List<LookupValue>>}
	 */
	Map<String, List<LookupValue>> getMultipleLookupValues(String lookupTypes);

	/**
	 * Fetch multiple lookup values
	 * 
	 * @param NIL
	 * 
	 * @return Returns a {@link List<LookupValue>}
	 */
	List<LookupValue> getLicenseeORRegionList();

	/**
	 * Fetch list of regions
	 * 
	 * @param NIL
	 * 
	 * @return Returns a {@link Map<String, List<Region>>}
	 */
	Map<String, List<Region>> getRegionList();

	/**
	 * Fetch list of regions
	 * 
	 * @param jsonRequest,fileName,inputStream {@link String,String,InputStream}
	 * 
	 * @return Returns a {@link String}
	 */
	String postFeedbackReportIssue(String jsonRequest, String fileName, InputStream inputStream);

	/**
	 * Get View Count by Event Category and date range
	 * 
	 * @param eventCategory
	 * 
	 * @param startDate
	 * 
	 * @param endDate
	 * 
	 * @return Returns a Collection of {@link GoogleAnalyticsModel}
	 */
	List<GoogleAnalyticsModel> getViewCount(String eventCategory, Date startDate, Date endDate);

	/**
	 * Get All View Count by Event Category
	 * 
	 * @param eventCategory
	 * 
	 * @return Returns a Collection of {@link GoogleAnalyticsTotalViewCount}
	 */
	List<GoogleAnalyticsTotalViewCount> getAllViewCount(String eventCategory);
	
	/**
	 * @see {@link ICommonManager#postFeedbackReportIssue(String,String,InputStream)}
	 */
	String postChatBotFeedbackReportIssue(String jsonRequest);
}
