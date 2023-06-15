package com.bh.drillingcommons.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DCUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(DCUtils.class);

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

	private DCUtils() {
	}

	/**
	 * Get {@link String[]} object from {@link String}
	 * 
	 * @param notificationEmailList {@link String}
	 * 
	 * @return Returns a {@link String[]}
	 */
	public static String[] convertStringToArray(String notificationEmailList) {
		String[] emailArray = null;
		List<String> result = new ArrayList<>();
		try {
			emailArray = notificationEmailList.replaceAll(";", ",").replaceAll(" ", "").split(",");
			for (String email : emailArray) {
				if (email != null && email.length() > 0) {
					result.add(email);
				}
			}
		} catch (Exception e) {
			return emailArray;
		}

		return result.toArray(new String[] {});
	}

	/**
	 * Verify Object is Null or Empty
	 * 
	 * @param obj {@link Object}
	 * 
	 * @return Returns a {@link Boolean}
	 */
	public static boolean isNullOrEmpty(Object obj) {
		boolean result = false;
		if (obj != null) {
			if (obj instanceof String) {
				if (((String) obj).trim().equalsIgnoreCase("")) {
					result = true;
				}
			} else if (obj instanceof Collection<?> && ((Collection<?>) obj).isEmpty()) {
				result = true;
			} else if (obj instanceof Map<?, ?> && ((Map<?, ?>) obj).isEmpty()) {
				result = true;
			}
		} else {
			result = true;
		}
		return result;
	}

	/**
	 * check Empty
	 * 
	 * @param strValue {@link String}
	 * 
	 * @return Returns {@link String}
	 */
	public static String checkEmpty(String strValue) {
		String retVal = "";
		try {
			if (!isNullOrEmpty(strValue))
				retVal = strValue;
		} catch (Exception e) {
			// ignore exception
		}
		return retVal;
	}

	/**
	 * Get Value As String
	 * 
	 * @param object {@link Object}
	 * 
	 * @return Returns a {@link String}
	 */
	public static String getValueAsString(Object object) {
		return object == null ? null : object.toString();
	}

	public static String[] convertEmailIdStringToArray(String notificationEmailList) {

		String[] emailArray = null;
		ArrayList<String> result = new ArrayList<>();
		try {
			emailArray = notificationEmailList.replaceAll(";", ",").replaceAll(" ", "").split(",");

			for (String email : emailArray) {
				if (email != null && email.length() > 0) {
					result.add(email);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception in convertEmailIdStringToArray()::", e);
			return emailArray;
		}

		return result.toArray(new String[] {});
	}

	public static boolean validFileExtensionType(String fileName) {
		List<String> allowedTypes = Arrays.asList("pdf", "gif", "png", "jpg", "jpge", "xls", "xlsx", "doc", "docx",
				"txt", "ppt", "pptx", "rtf");
		String locFileExt = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
		if (allowedTypes.indexOf(locFileExt.toLowerCase()) != -1) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String capitalize(String str) {
	    if(str == null || str.isEmpty()) {
	        return str;
	    }

	    return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
}
