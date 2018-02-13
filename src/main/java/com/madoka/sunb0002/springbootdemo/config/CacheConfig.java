/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

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
	// See manual configurations in old Spring project.
}
