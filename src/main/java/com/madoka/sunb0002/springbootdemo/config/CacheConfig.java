package com.madoka.sunb0002.springbootdemo.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.google.common.cache.CacheBuilder;
import com.madoka.sunb0002.springbootdemo.config.Constants.LocalCache;

/**
 * @author Sun Bo
 * @see In SpringBoot, this file is actually not needed since
 *      application.properties can set everything, but only 1 global cache
 *      policy can be set. And if to use "refreshAfterWrite", a CacheLoader bean
 *      is still required to be created manually.
 * @see Note: Caffeine is not yet the default cache implementation before
 *      SpringBoot2. Still need to import both starter-cache and Guava
 *      dependency, to make the cache auto configuration work.
 */
@Configuration
@EnableCaching
public class CacheConfig {

	@Value("${app.cache.custom-spec.short}")
	private String specShort;
	@Value("${app.cache.custom-spec.long}")
	private String specLong;

	@Primary
	@Bean
	public CacheManager guavaCacheMgr() {

		SimpleCacheManager cacheMgr = new SimpleCacheManager();
		List<GuavaCache> cacheList = new ArrayList<>();

		cacheList.add(new GuavaCache(LocalCache.SHORT, CacheBuilder.from(specShort).build()));
		cacheList.add(new GuavaCache(LocalCache.LONG, CacheBuilder.from(specLong).build()));

		cacheMgr.setCaches(cacheList);
		return cacheMgr;
	}
}
