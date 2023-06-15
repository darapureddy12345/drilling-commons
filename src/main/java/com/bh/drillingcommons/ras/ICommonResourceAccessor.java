package com.bh.drillingcommons.ras;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import com.bh.drillingcommons.entity.oracle.GALookup;
import com.bh.drillingcommons.entity.oracle.GEServiceLocation;
import com.bh.drillingcommons.entity.oracle.GoogleAnalytics;
import com.bh.drillingcommons.entity.oracle.Lookup;

public interface ICommonResourceAccessor {

	/**
	 * Get Service Locations
	 * 
	 * @param NIL
	 * 
	 * @return Returns a {@link List<GEServiceLocation>}
	 */
	List<GEServiceLocation> getServiceLocations(String source);

	/**
	 * Get Google analytics lookup values
	 * 
	 * @param NIL
	 * 
	 * @return Returns a {@link List<GALookup>}
	 */
	List<GALookup> getGoogleAnalyticsLkp();

	/**
	 * Fetch Lookup values
	 * 
	 * @param lookupType {@link String}
	 * 
	 * @return Returns a {@link Map<String, List<LookupValue>>}
	 */
	List<Lookup> getLookupValues(String lookupType);

	/**
	 * Fetch multiple lookup values
	 * 
	 * @param lookupType {@link List<String>}
	 * 
	 * @return Returns a {@link List<LookupValue>}
	 */
	List<Lookup> getLookupValues(List<String> lookupType);

	/**
	 * Fetch list of regions
	 * 
	 * @param NIL
	 * 
	 * @return Returns a {@link List<Object[]>}
	 */

	List<Object[]> getRegionList();

	/**
	 * Send email on user feedback
	 * 
	 * @param NIL
	 * 
	 * @return Returns a {@link String}
	 */

	String emailFeedback(String[] to, String emailContent, String emailSubject, InputStream ip, String fromList,
			String fileName);

	/**
	 * Get View Count
	 * 
	 * @param eventCategory
	 * 
	 * @param startDate
	 * 
	 * @param endDate
	 * 
	 * @return Returns a Collection of {@link GoogleAnalytics}
	 */
	List<GoogleAnalytics> getViewCount(String eventCategory, Date startDate, Date endDate);

	/**
	 * Get All view count
	 * 
	 * @param eventCategory
	 * 
	 * @return Returns a {@link Object[]}
	 */
	List<Object[]> getAllViewCount(String eventCategory);

	/**
	 * Get Next Sequence Number
	 * 
	 * @param sequenceName
	 * 
	 * @return Returns a {@link Long}
	 */
	long getNextSeqNumber(String sequenceName);

	/**
	 * Get Lookup Values by lookup type and value
	 * 
	 * @param lookupType  {@link String}
	 * 
	 * @param lookupValue {@link String}
	 * @return Returns a {@link Lookup}
	 */
	Lookup getLookupValues(String lookupType, String lookupValue);
}
