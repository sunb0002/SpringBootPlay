package com.madoka.sunb0002.springbootdemo.webapi.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ObjectUtils;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.madoka.sunb0002.springbootdemo.common.utils.JwtHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HomeControllerTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test(expected = SignatureVerificationException.class)
	public void testTestJwt() throws Exception {

		assertFalse(ObjectUtils.containsElement(null, ""));
		String goodKey = "good key";
		String payloadStr = "Sakura Kyouko";

		String jwt = JwtHelper.createToken(payloadStr, goodKey);
		assertEquals(payloadStr, JwtHelper.parseToken(jwt, goodKey).get("name").asString());
		log.info("Good key test passed.");

		String badJwt = JwtHelper.createToken("Qbey", "bad key");
		JwtHelper.parseToken(badJwt, goodKey);
		fail("Shouldn't reach here.");

	}

}
