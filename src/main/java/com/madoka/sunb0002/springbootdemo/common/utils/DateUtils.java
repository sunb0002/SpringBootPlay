/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author sunbo
 * @version Note: Java7 compliant
 * 
 */
public class DateUtils {

	private DateUtils() {
	}

	public static Date now() {
		return Calendar.getInstance().getTime();
	}

	public static int compare(Date date) {
		return compare(date, now());
	}

	public static int compare(Date date1, Date date2) {
		return date1.compareTo(date2);
	}

	public static Date getDateAfterMinutes(int minutes) {
		return getDateAfterMinutes(minutes, now());
	}

	public static Date getDateAfterMinutes(int minutes, Date from) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(from);
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}

	public static String dateToStringWithFormat(Date date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

}
