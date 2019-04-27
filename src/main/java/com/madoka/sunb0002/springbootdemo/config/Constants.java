package com.madoka.sunb0002.springbootdemo.config;

/**
 * @author Sun Bo
 *
 */
public class Constants {

	private Constants() {
	}

	public static class LocalMessageQueue {
		public static final String HUGTTO_DESTINATION = "MiraiCrystal";

		private LocalMessageQueue() {
		}
	}

	public class LocalCache {
		public static final String LONG = "cache-long";
		public static final String SHORT = "cache-short";

		private LocalCache() {
		}
	}

	public static class RedisCache {
		public static final String USER_KEY = "USER";

		public enum CacheHitStatus {
			ADDED, UPDATED, NOTFOUND;
		}

		private RedisCache() {
		}
	}

}
