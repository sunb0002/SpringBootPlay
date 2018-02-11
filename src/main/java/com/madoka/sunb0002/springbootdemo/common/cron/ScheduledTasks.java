/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.common.cron;

import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.madoka.sunb0002.springbootdemo.common.aop.LogAnno;
import com.madoka.sunb0002.springbootdemo.services.UserService;

/**
 * @author Sun Bo
 * @see @Scheduled is a simple cronjob setting. If you have multiple instances,
 *      still need to use Quartz.
 * @see fixedRate & fixedDelay both runs at specified interval. But fixedRate
 *      will not wait for previous execution to complete.
 *
 */
@Service
public class ScheduledTasks {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;

	@Scheduled(fixedRate = 10000)
	@LogAnno
	public void reportCurrentTime() {
		LOGGER.info("Current Time: {}", new Date());
	}

	@Scheduled(fixedDelay = 10000)
	@LogAnno
	public void getRandomUser() {
		Random r = new Random();
		char c = (char) (r.nextInt(26) + 'a');
		LOGGER.info("Trying to get user with char={}, result={}", c, userService.getSomeUsersWithSimilarName(c + ""));
	}

}
