package com.madoka.sunb0002.springbootdemo.common.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ValidatorsTest {

	@Test
	public void testAllMatches() {

		assertTrue(Validators.allMatches(null, null));
		assertFalse(Validators.allMatches(null, "2"));
		assertTrue(Validators.allMatches(Arrays.asList("2", "2"), "2"));
		assertTrue(Validators.allMatches(Arrays.asList("", ""), ""));

		assertFalse(Validators.allMatches(Arrays.asList("1", "2"), null));
		assertFalse(Validators.allMatches(Arrays.asList("1", "2"), "3"));

		List<String> list = Arrays.asList("aaa", null, "bbb");
		assertFalse(Validators.allMatches(list, null));
		assertFalse(Validators.allMatches(list, "aaa"));

		list = Arrays.asList(null, null, null);
		assertTrue(Validators.allMatches(list, null));
		assertFalse(Validators.allMatches(list, "aaa"));

	}

	@Test
	public void testContainsAny() {

		assertTrue(Validators.containsAny(null, null));
		assertFalse(Validators.containsAny(null, "2"));
		assertTrue(Validators.containsAny(Arrays.asList("2", "2"), "2"));
		assertTrue(Validators.containsAny(Arrays.asList("", "2"), ""));

		assertFalse(Validators.containsAny(Arrays.asList("1", "2"), null));
		assertFalse(Validators.containsAny(Arrays.asList("1", "2"), "3"));

		List<String> list = Arrays.asList("aaa", null, "bbb");
		assertTrue(Validators.containsAny(list, null));
		assertTrue(Validators.containsAny(list, "aaa"));

		list = Arrays.asList(null, null, null);
		assertTrue(Validators.containsAny(list, null));
		assertFalse(Validators.containsAny(list, "aaa"));

	}

	@Test
	public void testMatchRegex() {
		final String testRegex = "^[A-Za-z0-9 ]{0,14}$";
		assertTrue(Validators.matchRegex("   ", testRegex));
		assertTrue(Validators.matchRegex("kaname madoka", testRegex));
		assertFalse(Validators.matchRegex("sign the contract", testRegex));
		assertFalse(Validators.matchRegex("<incubator>", testRegex));
		assertTrue(Validators.matchRegex("<incubator>", null));
		assertFalse(Validators.matchRegex(null, testRegex));
	}

}
