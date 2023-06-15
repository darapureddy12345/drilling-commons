package com.bh.drillingcommons.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bh.drillingcommons.exceptions.ForbiddenException;
import com.bh.drillingcommons.managers.impl.CacheServiceManager;
import com.bh.drillingcommons.util.MessageUtil;

/**
 * Cache Controller to Clear All Cached Objects 
 * 
 * @author 212578665
 *
 */
@RestController
@RequestMapping("api/v1/cache")
public class CacheController {

    @Autowired
    private CacheServiceManager cacheServiceManager;

    @PostMapping("/flush")
    public ResponseEntity<Void> flushCache() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = (String) authentication.getPrincipal();
		if (!"SYSTEM".equalsIgnoreCase(principal)) {
			throw new ForbiddenException(MessageUtil.FORBIDDEN_MESSAGE);
		}
        cacheServiceManager.flushAllCache();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
