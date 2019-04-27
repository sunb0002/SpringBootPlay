package com.madoka.sunb0002.springbootdemo.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import com.google.common.cache.CacheBuilder;
import com.madoka.sunb0002.springbootdemo.config.Constants.LocalCache;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Sun Bo
 * @see For SpringBoot, this file is actually not needed since
 *      application.properties can set everything, but only 1 global cache
 *      policy can be set. And if to use "refreshAfterWrite", a CacheLoader bean
 *      is still required to be created manually.
 * @see Note: Caffeine is not yet the default cache implementation before
 *      SpringBoot2. Still need to import both starter-cache and Guava
 *      dependency, to make the cache auto configuration work.
 */
@Configuration
@EnableCaching
@Slf4j
public class CacheConfig {

	@Value("${app.cache.custom-spec.short}")
	private String specShort;
	@Value("${app.cache.custom-spec.long}")
	private String specLong;

	/**
	 * Guava in-memory cache.
	 * 
	 * @return
	 */
	@Primary
	@Bean("GuavaCache")
	public CacheManager guavaCacheMgr() {

		SimpleCacheManager cacheMgr = new SimpleCacheManager();
		List<GuavaCache> cacheList = new ArrayList<>();

		cacheList.add(new GuavaCache(LocalCache.SHORT, CacheBuilder.from(specShort).build()));
		cacheList.add(new GuavaCache(LocalCache.LONG, CacheBuilder.from(specLong).build()));

		cacheMgr.setCaches(cacheList);
		return cacheMgr;
	}

	/**
	 * Redis cache config (Simply cache the keys at top level). If to customize with
	 * HKEY or StringSerializer etc also possible.
	 * 
	 * @param redisTemplate
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean("RedisCache")
	public CacheManager redisCacheManager(@Qualifier("redisTemplate") Optional<RedisTemplate> rawRedisTemplate) {

		RedisTemplate<String, Serializable> redisTemplate = rawRedisTemplate.orElse(null);
		if (redisTemplate == null) {
			log.debug("Redis dependency is not ready, skip.");
			return null;
		}

		RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
		redisCacheManager.setUsePrefix(true);

		// Global default expire limit
		redisCacheManager.setDefaultExpiration(60L);
		Map<String, Long> expires = new HashMap<>();
		expires.put("UserDTO", 40L);
		redisCacheManager.setExpires(expires);

		return redisCacheManager;
	}
}
