/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.services.redis;

import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import com.madoka.sunb0002.springbootdemo.common.dtos.UserDTO;
import com.madoka.sunb0002.springbootdemo.config.Constants;
import com.madoka.sunb0002.springbootdemo.config.Constants.RedisCache.CacheHitStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * @author sunbo
 *
 */
@Service
@Slf4j
public class RedisManager {

	@Value("${app.redis.expiry-seconds:30}")
	private Long defaultExpirySeconds;

	// Most of the time, just use StringRedisTemplate, as RedisTemplate<String,
	// String>.
	// CANNOT SIMPLY @Autowired here, because autowired only injects by type and
	// won't find RedisTemplate<String, Serializable>. USE @Resource to inject by
	// name (but it's hard dependency, not optional)
	// docker run -d -p 6379:6379 redis
	private RedisTemplate<String, Serializable> redisTemplate;

	private HashOperations<String, String, UserDTO> userOps;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Autowired
	public RedisManager(@Qualifier("redisTemplate") Optional<RedisTemplate> rawRedisTemplate) {

		this.redisTemplate = rawRedisTemplate.orElse(null);
		if (this.redisTemplate == null) {
			log.debug("Redis dependency is not ready, skip.");
			return;
		}

		// This section is needed. Otherwise SpringBoot will put xac\xed characters in
		// key field.
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		this.redisTemplate.setKeySerializer(stringSerializer);
		this.redisTemplate.setHashKeySerializer(stringSerializer);
		// this.redisTemplate.setValueSerializer(stringSerializer);
		// this.redisTemplate.setHashValueSerializer(stringSerializer);
		this.redisTemplate.afterPropertiesSet();

		this.userOps = this.redisTemplate.opsForHash();

	}

	/**
	 * If the NRIC already exists, update the cache.
	 * 
	 * @param dto
	 * @return
	 */
	public String putUser(UserDTO dto) {
		userOps.put(Constants.RedisCache.USER_KEY, dto.getNric(), dto);
		this.redisTemplate.expire(Constants.RedisCache.USER_KEY, defaultExpirySeconds, TimeUnit.SECONDS);
		log.debug("Redis putUser: dto={}, expiry={}", dto,
				this.redisTemplate.getExpire(Constants.RedisCache.USER_KEY, TimeUnit.SECONDS));
		log.debug("Redis entries: {}, ", userOps.entries(Constants.RedisCache.USER_KEY));
		return Constants.RedisCache.CacheHitStatus.ADDED.name();
	}

	/**
	 * If the NRIC is not found, return a new DTO (as to fetch from other data
	 * source)
	 * 
	 * @param nric
	 * @return
	 */
	public UserDTO getUser(String nric) {
		UserDTO dto = userOps.get(Constants.RedisCache.USER_KEY, nric);
		log.debug("Redis getUser: dto={}, expiry={}", dto,
				this.redisTemplate.getExpire(Constants.RedisCache.USER_KEY, TimeUnit.SECONDS));
		return dto == null ? new UserDTO(-1L, "NRIC=" + nric + " is not found", CacheHitStatus.NOTFOUND.name()) : dto;
	}

}
