/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.common.utils;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.util.ObjectUtils;

/**
 * @author Sun Bo
 *
 */
public class Validators {

	private Validators() {
	}

	/** Check whether string s is null */
	public static boolean isNull(Object s) {
		return s == null;
	}

	/** Check whether strings is empty. */
	public static boolean isEmpty(String s) {
		return (s == null) || (s.length() == 0);
	}

	/** Check whether Object c is a String. */
	public static boolean isString(Object obj) {
		return (obj != null) && (obj instanceof java.lang.String);
	}

	/** Check whether a trimed String s is empty */
	public static boolean isTrimEmpty(String s) {
		return (s == null) || (s.trim().length() == 0);
	}

	/**
	 * Returns true if all characters are correct email format
	 */
	public static boolean isEmail(String email) {
		return email != null && email.matches("(\\d)|(\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)");
	}

	/** Check whether List list is empty */
	public static boolean isEmpty(List<?> list) {
		return ObjectUtils.isEmpty(list);
	}

	public static boolean isNotEmpty(List<?> list) {
		return !isEmpty(list);
	}

	/**
	 * Return true if all characters are correct URL format
	 */
	public static boolean isUrl(String url) {
		return url != null && url.matches("http://([w-]+.)+[w-]+(/[w- ./?%&=]*)?");
	}

	public static boolean isSubstring(String sub, String sup) {
		return isSubstring(sub, sup, false);
	}

	public static boolean isSubstringIgnoreCase(String sub, String sup) {
		return isSubstring(sub, sup, true);
	}

	/**
	 * @see {@link ValidatorsTest}
	 * @param sub
	 * @param sup
	 * @param ignoreCase
	 * @return
	 */
	public static boolean isSubstring(String sub, String sup, boolean ignoreCase) {

		if (isEmpty(sub)) {
			return true;
		} else if (isEmpty(sup)) {
			return false;
		}

		if (ignoreCase) {
			return sup.toLowerCase().indexOf(sub.toLowerCase()) > -1;
		} else {
			return sup.indexOf(sub) > -1;
		}

	}

	/**
	 * @see {@link ValidatorsTest}
	 * @param list
	 * @param target
	 * @return
	 */
	public static boolean containsAny(List<String> list, String target) {
		return isEmpty(list) ? isNull(target) : list.contains(target);
	}

	/**
	 * 
	 * @param list
	 * @param target
	 * @return
	 */
	public static boolean allMatches(List<String> list, String target) {

		if (isEmpty(list)) {
			return isNull(target);
		}

		for (String s : list) {
			if (isNull(s)) {
				if (!isNull(target)) {
					return false;
				}
			} else if (!s.equals(target)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	public static boolean allEmpty(List<String> list) {
		return allMatches(list, null) || allMatches(list, "");
	}

	/**
	 * @see {@link ValidatorsTest}
	 * @param s
	 * @param regex
	 * @return
	 */
	public static boolean matchRegex(String s, String regex) {
		return isNull(regex) || (isNull(s) ? false : Pattern.compile(regex).matcher(s).matches());
	}

}
