/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * @author sunbo
 *
 */
public class JwtHelper {

	private JwtHelper() {
	}

	public static String createToken(String name, String key) throws UnsupportedEncodingException {
		return createToken(name, null, key);

	}

	public static String createToken(String name, Date expiry, String key) throws UnsupportedEncodingException {

		// HMAC
		Algorithm algo = Algorithm.HMAC256(key);

		// Header
		Map<String, Object> headerMap = new HashMap<>();
		headerMap.put("alg", "HS256");
		headerMap.put("typ", "JWT");

		// Payload claims
		return JWT.create().withHeader(headerMap).withClaim("name", name).withClaim("profession", "Mahou Shoujo")
				.withClaim("when", new Date()).withExpiresAt(expiry).withIssuer("Gen Urobuchi").sign(algo);

	}

	public static Map<String, Claim> parseToken(String token, String key) throws UnsupportedEncodingException {

		Algorithm algo = Algorithm.HMAC256(key);
		JWTVerifier verifier = JWT.require(algo).build();
		DecodedJWT jwt = verifier.verify(token);
		return jwt.getClaims();

	}

}
