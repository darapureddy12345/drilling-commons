package com.bh.drillingcommons.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Primary
public class CustomRemoteTokenService extends RemoteTokenServices {
	
	@Value("${security.oauth2.resource.tokenInfoUri}")
    private String tokenInfoUri;
	
	@Value("${security.oauth2.client.clientId}")
    private String clientId;
	
	@Value("${security.oauth2.client.clientSecret}")
    private String clientSecret;

    private RestOperations restTemplate;

    private AccessTokenConverter tokenConverter = new CustomAccessTokenConverter();

    @Autowired
    public CustomRemoteTokenService() {
        restTemplate = new RestTemplate();
        ((RestTemplate) restTemplate).setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            // Ignore 400
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400) {
                    super.handleError(response);
                }
            }
        });
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
    	StringBuilder cred = new StringBuilder(this.clientId).append(":").append(this.clientSecret);
    	String encodedCred = Base64.getEncoder().encodeToString(cred.toString().getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic "+encodedCred);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> map = executePost(this.tokenInfoUri+"?token=" + accessToken, headers);
        if (map == null || map.isEmpty() || map.get("error") != null) {
            throw new InvalidTokenException("Token not allowed");
        }else if(!(boolean)map.get("active")){
            throw new InvalidTokenException("Token invalid");
        }
        return tokenConverter.extractAuthentication(map);
	}

    private Map<String, Object> executePost(String path, HttpHeaders headers) {
        try {
            if (headers.getContentType() == null) {
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            }
            @SuppressWarnings("rawtypes")
            Map map = restTemplate.exchange(path, HttpMethod.POST, new HttpEntity<MultiValueMap<String, String>>(null, headers), Map.class).getBody();
            @SuppressWarnings("unchecked")
            Map<String, Object> result = map;
            return result;
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        return null;
    }

}