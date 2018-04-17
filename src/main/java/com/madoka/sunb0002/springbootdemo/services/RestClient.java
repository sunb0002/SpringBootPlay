/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.services;

import java.io.IOException;
import java.net.URI;

import javax.annotation.PostConstruct;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.madoka.sunb0002.springbootdemo.common.utils.Validators;

import lombok.extern.slf4j.Slf4j;

/**
 * @author sunbo
 *
 */
@Service
@Slf4j
public class RestClient {

	private RestTemplate restTemplate;

	@PostConstruct
	private void init() {
		restTemplate = new RestTemplate();
	}

	/**
	 * 
	 * @param uri
	 * @param headers
	 * @return
	 * @throws JSONException
	 */
	public ResponseEntity<JSONObject> getForJsonRaw(URI uri, HttpHeaders headers) throws JSONException {

		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		log.info("------ Querying (GET) RESTful URI:{} ", uri);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		log.info("------ Query (GET) END. statusCode={}", response.getStatusCode().value());

		String body = response.getBody();
		return Validators.isEmpty(body) ? new ResponseEntity<>(response.getStatusCode())
				: new ResponseEntity<>(new JSONObject(body), response.getStatusCode());

	}

	/**
	 * 
	 * @param uri
	 * @param headers
	 * @param requestBody
	 * @return
	 * @throws JSONException
	 */
	public ResponseEntity<JSONObject> postForJsonRaw(URI uri, HttpHeaders headers, Object requestBody)
			throws JSONException {

		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);

		log.info("------ Querying (POST) RESTful URI:{} ", uri);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		log.info("------ Query (POST) END. statusCode={}", response.getStatusCode().value());

		String respBody = response.getBody();
		return Validators.isEmpty(respBody) ? new ResponseEntity<>(response.getStatusCode())
				: new ResponseEntity<>(new JSONObject(respBody), response.getStatusCode());

	}

	/**
	 * 
	 * @param uri
	 * @param headers
	 * @param clazz
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	public <T> ResponseEntity<T> getForGeneric(URI uri, HttpHeaders headers, Class<T> clazz)
			throws JSONException, IOException {

		ResponseEntity<JSONObject> respRaw = this.getForJsonRaw(uri, headers);
		HttpStatus statusCode = respRaw.getStatusCode();
		JSONObject body = respRaw.getBody();

		if (body != null) {
			T genericBody = new ObjectMapper().readValue(body.toString(), clazz);
			return new ResponseEntity<>(genericBody, statusCode);
		} else {
			return new ResponseEntity<>(statusCode);
		}

	}

	/**
	 * 
	 * @param uri
	 * @param headers
	 * @param requestBody
	 * @param respClazz
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	public <T> ResponseEntity<T> postForGeneric(URI uri, HttpHeaders headers, Object requestBody, Class<T> respClazz)
			throws JSONException, IOException {

		ResponseEntity<JSONObject> respRaw = this.postForJsonRaw(uri, headers, requestBody);
		HttpStatus statusCode = respRaw.getStatusCode();
		JSONObject body = respRaw.getBody();

		if (body != null) {
			T genericBody = new ObjectMapper().readValue(body.toString(), respClazz);
			return new ResponseEntity<>(genericBody, statusCode);
		} else {
			return new ResponseEntity<>(statusCode);
		}

	}

}
