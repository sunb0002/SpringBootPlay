/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.config;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author Sun Bo
 *
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private int corePoolSize = 2; // Concurrent active running threads, make new threads waiting
	private int queueCapacity = 20; // Maximum waiting threads
	private int maxPoolSize = 20; // Maximum total number of threads

	// Default executor, method name from AsyncConfigurer
	@Bean("Async-Executor1")
	@Primary
	public Executor getAsyncExecutor() {
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
		ExecutorService es = Executors.newFixedThreadPool(queueCapacity, myThreadFactory); // NOSONAR
		// Want to play with ExecutorService before return.
		return es;
	}

	@Bean("Test-Conditional-Executor")
	@ConditionalOnProperty(prefix = "spring", name = "profiles.active", havingValue = "dev1")
	public Executor executor3() {
		logger.debug("Condition is met, creating executor3 bean.");
		return Executors.newCachedThreadPool();
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new MyAsUncaughtExHandler();
	}

}

/**
 * When an @Async method has a Future typed return value, it is easy to manage
 * an exception that was thrown during the method execution as this exception
 * will be thrown when calling get on the Future result. With a void return type
 * however, the exception is uncaught and cannot be transmitted. For those
 * cases, an AsyncUncaughtExceptionHandler can be provided to handle such
 * exceptions.
 * 
 * @author Sun Bo
 *
 */
class MyAsUncaughtExHandler implements AsyncUncaughtExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void handleUncaughtException(Throwable arg0, Method arg1, Object... arg2) {
		logger.error("Async Uncaught Exception, ExMsg={}, MethodName={}", arg0.getMessage(), arg1.getName());
	}

}
