/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.madoka.sunb0002.springbootdemo.common.exceptions.ServiceException;

/**
 * @author Sun Bo
 *
 */
@Aspect
@Service
public class LogAspect {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Pointcut("within(com.madoka..services..*)")
	private void isService() {
	};

	/*
	 * If to retrieve value from Annotations in AOP, do not declare PointCut
	 * separately.
	 * 
	 * @Pointcut("@annotation(logAnno)") private void isLogAnno(LogAnno logAnno)
	 * { };
	 */

	@Pointcut("execution(public * com.madoka..HomeController.allH*(..))")
	private void isInternalApi200() {
	};

	@Pointcut("execution(* com.madoka..HomeController.test())")
	private void isInternalApi403() {
	};

	@Pointcut("execution(public com.madoka..ProfileResponseUser com.madoka..ProfileController.*(..))")
	private void isProfileApiOneUser() {
	};

	@Pointcut("within(com.madoka..profile.ProfileController) && execution(public com.madoka..ProfileResponseUserList *(..))")
	private void isProfileApiMultiUsers() {
	};

	@Before("isService() && @annotation(anno)")
	public void doBefore1(JoinPoint jp, LogAnno anno) {
		LOGGER.debug("AOP-doBefore1: annoValue={}", anno.value());
		LOGGER.debug("AOP-doBefore1: signature={}, args={}", jp.getSignature(), jp.getArgs());
		// LOGGER.debug("AOP-doBefore1: sigShortStr={}, sigName={},
		// targetClass={}, args={}", jp.getSignature().toShortString(),
		// jp.getSignature().getName(), jp.getTarget().getClass().getName(),
		// jp.getArgs());
		// AOP-doBefore1:
		// sigShortStr=UserService.getSomeUsersWithSimilarName(..),
		// sigName=getSomeUsersWithSimilarName,
		// targetClass=com.madoka.sunb0002.springbootdemo.services.impl.UserServiceImpl,
		// args=[sa]
	}

	@Before("isProfileApiMultiUsers()")
	public void doBefore2(JoinPoint jp) {
		LOGGER.debug("AOP-doBefore2: signature={}, args={}", jp.getSignature(), jp.getArgs());
	}

	@After("isProfileApiOneUser()")
	public void doAfter(JoinPoint jp) {
		LOGGER.debug("AOP-doAfter: signature={}, args={}", jp.getSignature(), jp.getArgs());
	}

	@AfterThrowing(pointcut = "isInternalApi403()", throwing = "se")
	public void doAfterThrow(JoinPoint jp, ServiceException se) {
		LOGGER.debug("AOP-doAfterThrow: signature={}, exMsg={}", jp.getSignature(), se.getMessage());
	}

	@Around("isInternalApi200()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		LOGGER.debug("AOP-doAround: signature={}", pjp.getSignature());
		Object retVal = pjp.proceed();
		LOGGER.debug("AOP-doAround: retVal={}", retVal);
		return retVal;
	}

}