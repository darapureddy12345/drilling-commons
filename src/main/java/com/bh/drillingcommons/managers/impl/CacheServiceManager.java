package com.bh.drillingcommons.managers.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Cache Service Manager helps to Manage Cached Objects 
 * 
 * @author 212578665
 *
 */
@Service
public class CacheServiceManager {

    @Autowired
    private CacheManager cacheManager;

    public void flushAllCache(){
        cacheManager.getCacheNames().parallelStream().forEach(
                name -> Objects.requireNonNull(cacheManager.getCache(name)).clear());
    }
}
