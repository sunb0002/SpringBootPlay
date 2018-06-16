package com.madoka.sunb0002.springbootdemo.config;

/**
 * @author Sun Bo
 *
 */
public class Constants {

	private Constants() {
	}

	public class LocalMessageQueue {
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

}
