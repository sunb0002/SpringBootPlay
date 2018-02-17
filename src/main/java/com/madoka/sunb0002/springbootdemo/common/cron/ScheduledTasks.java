/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.common.cron;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.madoka.sunb0002.springbootdemo.common.aop.LogAnno;
import com.madoka.sunb0002.springbootdemo.services.UserService;

/**
 * @author Sun Bo
 * @see @Scheduled is a simple cronjob setting. If got multiple instances, still
 *      need to use Quartz.
 * @see fixedRate & fixedDelay both runs at specified interval. But fixedRate
 *      will not wait for previous execution to complete.
 *
 */
@Service
@Profile("!test")
public class ScheduledTasks {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;

	@Scheduled(initialDelay = 10 * 60 * 1000, fixedRate = 10 * 60 * 1000)
	@LogAnno
	public void reportCurrentTime() {
		logger.info("reportCurrentTime: {}", new Date());
	}

	@Scheduled(initialDelay = 10 * 60 * 1000, fixedDelay = 20 * 60 * 1000)
	public void printRandomUser() {
		reportCurrentTime();
		userService.getRandomUser();
	}

}
