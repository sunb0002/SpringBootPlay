/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author Sun Bo
 *
 */
@Configuration
@EnableAsync
public class AsyncConfig {

	private int corePoolSize = 2; // Minimum
	private int queueCapacity = 2; // Active, make new threads waiting
	private int maxPoolSize = 10; // Maximum

	@Bean("Async-Executor1")
	public Executor executor1() {
		ThreadPoolTaskExecutor tpte = new ThreadPoolTaskExecutor();
		tpte.setThreadNamePrefix("EXEC1-");
		tpte.setCorePoolSize(corePoolSize);
		tpte.setQueueCapacity(queueCapacity);
		tpte.setMaxPoolSize(maxPoolSize);
		tpte.setThreadPriority(Thread.MIN_PRIORITY);
		return tpte;
	}

	@Bean("Async-Executor2")
	public Executor executor2() {
		ThreadFactory myThreadFactory = new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName("EXEC2-" + t.getId());
				t.setPriority(Thread.MIN_PRIORITY);
				return t;
			}
		};
		ExecutorService es = Executors.newFixedThreadPool(queueCapacity, myThreadFactory);
		// Want to play with ExecutorService.
		return es;
	}

}
