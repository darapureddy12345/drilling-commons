package com.bh.drillingcommons.util;


import java.io.IOException;

import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.bh.drillingcommons.exceptions.BadRequestException;
import com.bh.drillingcommons.exceptions.ForbiddenException;
import com.bh.drillingcommons.exceptions.SystemException;
import com.bh.drillingcommons.exceptions.UnauthorizedException;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

public class AuthorizationUtility {

	public AuthorizationUtility() {
	}

	private static final Logger logger = LoggerFactory.getLogger(AuthorizationUtility.class);

	public static void  isAuthorized(long feature, Long customer, String[] roles) {
		try {
			StringBuilder url =null;
			//get token to hit the API for userManagement
			String token = getUserToken();
			if(!("SYSTEM".equalsIgnoreCase(getUserDetails()))) {
				url =new StringBuilder(new AuthorizationUtility().getUserMgmtServiceURL());
				UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.toString())
						// Add query parameter
						.queryParam("featureId", feature==-1?null:feature)
						.queryParam("customerId", (customer==null||customer==-1)?null:customer)
						.queryParam("roles", roles!=null?String.join(",", roles):"");
				userManagementApiHit(token, builder);
				logger.info("Access Granted for - {}",userInfoLogFormat(feature, null, customer, roles, true));
			}
		} catch (UnauthorizedException ux) {
			logger.error("Unauthorized Exception for {} :  {}",userInfoLogFormat(feature, null, customer, roles, true), ux);
			throw new UnauthorizedException("Unauthorized");
		}catch (BadRequestException brx) {
			logger.error("BadRequestException For {} :  {}", userInfoLogFormat(feature, null, customer, roles, true), brx);
			throw new BadRequestException("Bad Request");
		}catch (ForbiddenException fx) {
			logger.error("ForbiddenException For {} :  {}",userInfoLogFormat(feature, null, customer, roles, true), fx);
			throw new ForbiddenException("FORBIDDEN"); 
		} catch (Exception e) {
			logger.error("Exception Occured in Method isAuthorized() for  {} :  {}",userInfoLogFormat(feature, null, customer, roles, true), e);
			throw new SystemException();
		}
	}

	
	public static void  isAuthorized(String feature, Long customer, String[] roles) {
		try {
			StringBuilder url =null;
			//get token to hit the API for userManagement
			String token = getUserToken();
			if(!"SYSTEM".equalsIgnoreCase(getUserDetails()) && !(feature==null&&customer==null&&roles==null)) {
				url =new StringBuilder(new AuthorizationUtility().getUserMgmtServiceURL());
				UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url.toString())
						// Add query parameter
						.queryParam("featureName", "-1".equals(feature)?null:feature)
						.queryParam("customerId", (customer==null||customer==-1)?null:customer)
						.queryParam("roles", roles!=null?String.join(",", roles):"");
				userManagementApiHit(token, builder);
				logger.info("Access Granted for - {}",userInfoLogFormat(0, feature, customer, roles, false));
			}
		} catch (UnauthorizedException ux) {
			logger.error("Unauthorized Exception for {} :  {}",userInfoLogFormat(0, feature, customer, roles, false), ux);
			throw new UnauthorizedException("Unauthorized");
		}catch (BadRequestException brx) {
			logger.error("BadRequestException For {} :  {}", userInfoLogFormat(0, feature, customer, roles, false), brx);
			throw new BadRequestException("Bad Request");
		}catch (ForbiddenException fx) {
			logger.error("ForbiddenException For {} :  {}",userInfoLogFormat(0, feature, customer, roles, false), fx);
			throw new ForbiddenException("FORBIDDEN"); 
		} catch (Exception e) {
			logger.error("Exception Occured in Method isAuthorized() for  {} :  {}",userInfoLogFormat(0, feature, customer, roles, false), e);
			throw new SystemException();
		}
	}

	private static void userManagementApiHit(String token, UriComponentsBuilder builder) throws Exception {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Authorization", token);
			RestTemplate restTemplate = new RestTemplate();
			((RestTemplate) restTemplate).setErrorHandler(new DefaultResponseErrorHandler() {
				@Override
				public void handleError(ClientHttpResponse response) throws IOException {
					if (response.getStatusCode()
							.series() == HttpStatus.Series.SERVER_ERROR) {
					} else if (response.getStatusCode()
							.series() == HttpStatus.Series.CLIENT_ERROR) {
						if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
							throw new NotFoundException();
						}else if(response.getStatusCode()==HttpStatus.UNAUTHORIZED) {
							throw new UnauthorizedException();
						}else if(response.getStatusCode()==HttpStatus.BAD_REQUEST) {
							throw new BadRequestException();
						}else if(response.getStatusCode()==HttpStatus.FORBIDDEN) {
							throw new ForbiddenException();
						}
					}	
				}
			});
		restTemplate.exchange(builder.buildAndExpand().toUri(), HttpMethod.GET, new HttpEntity<String>(headers), String.class);
		}catch (UnauthorizedException ux) {
			throw new UnauthorizedException("UNAUTHORIZED");
		}catch (BadRequestException brx) {
			throw new BadRequestException("BAD REQUEST");
		}catch (ForbiddenException fx) {
			throw new ForbiddenException("FORBIDDEN");
		}
	}

	public String getUserMgmtServiceURL() {
		return System.getenv("USERMANAGEMENT_URL");//USERMANAGEMENT SERVICE URL variable
	}
	
	public static String getUserToken() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getDetails() instanceof OAuth2AuthenticationDetails) {
			OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) authentication.getDetails();
			return oAuth2AuthenticationDetails.getTokenType()+" "+oAuth2AuthenticationDetails.getTokenValue();
		}
		else
			throw new UnauthorizedException("UNAUTHORIZED");
	}
	
	public static String getUserDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (String) authentication.getPrincipal();
	}
	
	public static String userInfoLogFormat(long featureId,String featureName, Long customer, String[] roles,boolean isFeatureId) {
		StringBuilder strBldr = new StringBuilder();
		String roleList="";
		strBldr.append(" User :"+getUserDetails());
		if(isFeatureId)
			strBldr.append(" | Feature ID :"+featureId);
		else
			strBldr.append(" | Feature Name :"+(featureName==null?"NA":featureName));
		
		strBldr.append(" | CustomerId :"+(customer==null?"NA":customer));
		if(roles==null)
			roleList="ANY";
		else if(roles.length==0)
			roleList="NONE";
		else
			roleList=String.join(",", roles);
		strBldr.append(" | Role :"+roleList);
		return strBldr.toString();
	}
}
