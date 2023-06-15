package com.bh.drillingcommons.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bh.drillingcommons.entity.oracle.GALookup;
import com.bh.drillingcommons.entity.oracle.GEServiceLocation;
import com.bh.drillingcommons.entity.oracle.LookupValue;
import com.bh.drillingcommons.exceptions.BadRequestException;
import com.bh.drillingcommons.exceptions.SystemException;
import com.bh.drillingcommons.managers.ICommonManager;
import com.bh.drillingcommons.models.GoogleAnalyticsModel;
import com.bh.drillingcommons.models.GoogleAnalyticsTotalViewCount;
import com.bh.drillingcommons.models.Region;
import com.bh.drillingcommons.util.AuthorizationUtility;
import com.bh.drillingcommons.util.CommonMessage;
import com.bh.drillingcommons.util.DCUtils;
import com.bh.drillingcommons.util.MessageUtil;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping({ "api/common/v1/", "common/api/v1/" })
public class CommonController {

	@Autowired
	private ICommonManager commonManager;

	@ApiOperation(value = "getAllServiceLocations", nickname = "Get all service locations", produces = "application/json", notes = "This route will return a list of all service locations")
	@GetMapping("servicelocations")
	public ResponseEntity<List<GEServiceLocation>> getServiceLocations() {
		try {
			AuthorizationUtility.isAuthorized(5, null, null);
			return new ResponseEntity<>(this.commonManager.getServiceLocations(), HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "getGoogleAnalyticsLookup", nickname = "Get google analytics lookup", produces = "application/json", notes = "This route will return GA lookups")
	@GetMapping("googleAnalyticsLkp")
	public ResponseEntity<Map<String, Map<String, GALookup>>> getGoogleAnalyticsLookUp() {
		try {
			AuthorizationUtility.isAuthorized(null,null, null);
			return new ResponseEntity<>(this.commonManager.getGoogleAnalyticsLookUp(), HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "getLookupValues", nickname = "Get lookup values", produces = "application/json", notes = "This route will return lookup values")
	@GetMapping("lookup/{lookupType}")
	public ResponseEntity<Map<String, List<LookupValue>>> getLookupValues(
			@PathVariable("lookupType") String lookupType) {
		try {
			AuthorizationUtility.isAuthorized(null, null, null);
			String normalLookupType = Normalizer.normalize(lookupType, Form.NFKC);
			return new ResponseEntity<>(this.commonManager.getLookupValues(normalLookupType), HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "getMultipleLookupValues", nickname = "Get multiple lookup values", produces = "application/json", notes = "This route will return multiple lookup values")
	@GetMapping("lookup/multiple/{lookupTypes}")
	public ResponseEntity<Map<String, List<LookupValue>>> getMultipleLookupValues(
			@PathVariable("lookupTypes") String lookupTypes) {
		try {
			AuthorizationUtility.isAuthorized(null, null, null);
			String normalLookupTypes = Normalizer.normalize(lookupTypes, Form.NFKC);
			return new ResponseEntity<>(this.commonManager.getMultipleLookupValues(normalLookupTypes), HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "getLicenseeORRegionList", nickname = "Get Licensee OR Region List", produces = "application/json", notes = "This route will return Licensee OR Region List")
	@GetMapping("getLicenseeORRegionList")
	public ResponseEntity<List<LookupValue>> getLicenseeORRegionList() {
		try {
			AuthorizationUtility.isAuthorized(null, null,null);
			return new ResponseEntity<>(commonManager.getLicenseeORRegionList(), HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "getRegionList", nickname = "Get Region List", produces = "application/json", notes = "This route will return Region List")
	@GetMapping("getRegionList")
	public ResponseEntity<Map<String, List<Region>>> getRegionList() {
		try {
			AuthorizationUtility.isAuthorized(null, null, null);
			return new ResponseEntity<>(commonManager.getRegionList(), HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "postFeedbackReportIssue", nickname = "Post feedback report issue", produces = "application/json", notes = "This route will let users post feedback or report issue related to the portal.")
	@PostMapping("postfeedback/reportIssue")
	public ResponseEntity<String> postFeedbackReportIssue(@RequestParam(value = "json") String jsonRequest,
			@RequestParam(value = "fileName") String fileName,
			@RequestPart(value = "attachment") MultipartFile file) {
		try {
			if (!file.isEmpty()) {
				if (!DCUtils.validFileExtensionType(file.getOriginalFilename()) || !DCUtils.validFileExtensionType(fileName)) {
					throw new BadRequestException(CommonMessage.INVALID_FILE_EXTENSION_TYPE);
				}
				InputStream inputStream = file.getInputStream();
				return new ResponseEntity<>(this.commonManager.postFeedbackReportIssue(jsonRequest, fileName, inputStream),
						HttpStatus.OK);
			} else {
				throw new BadRequestException(MessageUtil.EMPTY_FILE_ERROR_MSG);
			}
		} catch (BadRequestException bre) {
			throw bre;
		} catch (IOException | SystemException e) {
			throw new SystemException(e.getMessage(), e);
		} 
	}

	@ApiOperation(value = "Get View Count", nickname = "Get View Count", produces = "application/json", notes = "This route will return view count")
	@GetMapping("viewcount/{eventCategory}")
	public ResponseEntity<List<GoogleAnalyticsModel>> getViewCount(@PathVariable("eventCategory") String eventCategory,
			@RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate) {
		try {
			AuthorizationUtility.isAuthorized(null, null, null);
			return new ResponseEntity<>(commonManager.getViewCount(eventCategory, startDate, endDate), HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException se) {
			throw se;
		}
	}

	@ApiOperation(value = "Get All View Count", nickname = "Get All View Count", produces = "application/json", notes = "This route will return all view count")
	@GetMapping("viewcount/all/{eventCategory}")
	public ResponseEntity<List<GoogleAnalyticsTotalViewCount>> getAllViewCount(
			@PathVariable("eventCategory") String eventCategory) {
		try {
			AuthorizationUtility.isAuthorized(null, null, null);
			return new ResponseEntity<>(commonManager.getAllViewCount(eventCategory), HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException se) {
			throw se;
		}
	}
	
	@ApiOperation(value = "chatbotpostFeedbackReportIssue", nickname = "Post chatbot feedback report issue", produces = "application/json", notes = "This route will let users post feedback or report issue related to the portal via Chatbot.")
	@PostMapping("chatbotpostfeedback/reportIssue")
	public ResponseEntity<String> postChatbotFeedbackReportIssue(@RequestParam(value = "json") String jsonRequest) {
		try {
			return new ResponseEntity<>(this.commonManager.postChatBotFeedbackReportIssue(jsonRequest), HttpStatus.OK);
		} catch (BadRequestException bre) {
			throw bre;
		} catch (SystemException e) {
			throw new SystemException(e.getMessage(), e);
		}
	}
}
