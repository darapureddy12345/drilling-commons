package com.bh.drillingcommons.config;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.*;

@Primary
public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {

	private UserAuthenticationConverter userTokenConverter = new CustomUserAuthenticationConverter();
	
	private boolean includeGrantType;

	private String scopeAttribute = SCOPE;

	private String clientIdAttribute = CLIENT_ID;

	public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
		Map<String, String> parameters = new HashMap<String, String>();
		Set<String> scope = extractScope(map);
		Authentication user = userTokenConverter.extractAuthentication(map);
		String clientId = (String) map.get(clientIdAttribute);
		parameters.put(clientIdAttribute, clientId);
		if (includeGrantType && map.containsKey(GRANT_TYPE)) {
			parameters.put(GRANT_TYPE, (String) map.get(GRANT_TYPE));
		}
//		Set<String> resourceIds = new LinkedHashSet<String>(map.containsKey(AUD) ? getAudience(map) : Collections.<String>emptySet());
		Set<String> resourceIds = new LinkedHashSet<String>(map.containsKey("cid") ? getClient(map) : Collections.<String>emptySet());
		Collection<? extends GrantedAuthority> authorities = null;
		if (user==null && map.containsKey(AUTHORITIES)) {
			@SuppressWarnings("unchecked")
			String[] roles = ((Collection<String>)map.get(AUTHORITIES)).toArray(new String[0]);
			authorities = AuthorityUtils.createAuthorityList(roles);
		}
		OAuth2Request request = new OAuth2Request(parameters, clientId, authorities, true, scope, resourceIds, null, null,
				null);
		return new OAuth2Authentication(request, user);
	}

	private Collection<String> getClient(Map<String, ?> map) {
		Object cids = map.get("cid");
		if (cids instanceof Collection) {			
			@SuppressWarnings("unchecked")
			Collection<String> result = (Collection<String>) cids;
			return result;
		}
		return Collections.singleton((String)cids);
	}
	
	private Collection<String> getAudience(Map<String, ?> map) {
		Object auds = map.get(AUD);
		if (auds instanceof Collection) {			
			@SuppressWarnings("unchecked")
			Collection<String> result = (Collection<String>) auds;
			return result;
		}
		return Collections.singleton((String)auds);
	}

	private Set<String> extractScope(Map<String, ?> map) {
		Set<String> scope = Collections.emptySet();
		if (map.containsKey(scopeAttribute)) {
			Object scopeObj = map.get(scopeAttribute);
			if (String.class.isInstance(scopeObj)) {
				scope = new LinkedHashSet<String>(Arrays.asList(String.class.cast(scopeObj).split(" ")));
			} else if (Collection.class.isAssignableFrom(scopeObj.getClass())) {
				@SuppressWarnings("unchecked")
				Collection<String> scopeColl = (Collection<String>) scopeObj;
				scope = new LinkedHashSet<String>(scopeColl);	// Preserve ordering
			}
		}
		return scope;
	}

}
