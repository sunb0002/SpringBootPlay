/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.common.aop;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(METHOD)
/**
 * @author Sun Bo
 *
 */
public @interface LogAnno {
	public String value() default "Default-AOP-log";
}
