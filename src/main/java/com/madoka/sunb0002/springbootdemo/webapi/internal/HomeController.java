/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.webapi.internal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.jms.JMSException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.madoka.sunb0002.springbootdemo.common.aop.LogAnno;
import com.madoka.sunb0002.springbootdemo.common.dtos.UserDTO;
import com.madoka.sunb0002.springbootdemo.common.exceptions.ServiceException;
import com.madoka.sunb0002.springbootdemo.common.utils.DateUtils;
import com.madoka.sunb0002.springbootdemo.common.utils.JwtHelper;
import com.madoka.sunb0002.springbootdemo.common.utils.Validators;
import com.madoka.sunb0002.springbootdemo.services.MailService;
import com.madoka.sunb0002.springbootdemo.services.RestClient;
import com.madoka.sunb0002.springbootdemo.services.UserService;
import com.madoka.sunb0002.springbootdemo.services.jms.Producer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sun Bo
 *
 */
@Api
@RestController
@RequestMapping("/home")
@Slf4j
public class HomeController {

	@Autowired
	private UserService userService;
	@Autowired
	private MailService mailService;
	@Autowired
	private Producer mqProducer;
	@Autowired
	private RestClient restClient;

	@Value("${app.name}")
	private String appName;

	@Value("${app.jwt.secret}")
	private String jwtKey;

	@Deprecated
	@ApiOperation(value = "allHail", notes = "Get successful message", tags = { "Internal" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Everything ok.", response = HomeResponse.class),
			@ApiResponse(code = 403, message = "You'll get forbidden.", response = HomeResponse.class), })
	@GetMapping("/json200")
	@LogAnno
	public HomeResponse allHail() { // NOSONAR
		log.info("waifu here info."); // NOSONAR
		log.error("waifu here error.");
		log.debug("waifu here debug.");
		return new HomeResponse(200, appName, "All Hail Madoka");
	}

	@ApiOperation(value = "test403", notes = "Get unsuccessful message", tags = { "Internal" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Everything ok.", response = HomeResponse.class),
			@ApiResponse(code = 404, message = "Got forbidden.", response = HomeResponse.class),
			@ApiResponse(code = 500, message = "Unexpected Error occurred", response = HomeResponse.class) })
	@GetMapping("/json403")
	public HomeResponse test403() {
		throw new ServiceException(HttpStatus.FORBIDDEN.value(), "forbidden liao");
	}

	@ApiOperation(value = "testMail", notes = "Test sending email", tags = { "Internal" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Everything ok.", response = HomeResponse.class),
			@ApiResponse(code = 403, message = "You'll get forbidden.", response = HomeResponse.class), })
	@GetMapping("/json200Mail")
	public HomeResponse testMail() {
		mailService.sendSimpleMail();
		return new HomeResponse(200, appName, "Mail has been sent.");
	}

	@ApiOperation(value = "testMQ", notes = "Test Message Queue", tags = { "Internal" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Everything ok.", response = HomeResponse.class),
			@ApiResponse(code = 403, message = "You'll get forbidden.", response = HomeResponse.class), })
	@GetMapping("/json200MQ")
	public HomeResponse testMQ() throws JMSException {
		mqProducer.sendStrMsg("Test StringMsg sent at: " + new Date());
		mqProducer.sendDtoMsg(null);
		List<UserDTO> randomUsers = userService.getRandomUser();
		UserDTO sendUser = Validators.isEmpty(randomUsers) ? new UserDTO(999L, "S0000111Z", "Hug-tan!")
				: randomUsers.get(0);
		mqProducer.sendDtoMsg(sendUser);
		return new HomeResponse(200, appName, "All MQ messages have been sent.");
	}

	@ApiOperation(value = "testAnything", notes = "Test anything", tags = { "Internal" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Everything ok.", response = HomeResponse.class),
			@ApiResponse(code = 403, message = "You'll get forbidden.", response = HomeResponse.class), })
	@GetMapping("/json200MK2")
	@LogAnno("Anno-testAnything")
	public HomeResponse testAnything() throws InterruptedException, ExecutionException {

		// userService.asyncTask(); //NOSONAR
		log.info("AsyncTask start: {}", new Date());
		Future<String> futureStr = userService.asyncTaskWithFuture();

		// Can also use "while futureStr.isDone()" below.
		String result = futureStr.get();

		log.info("AsyncTask done: {}", new Date());
		return new HomeResponse(200, appName, "All tests done: " + result);
	}

	@ApiOperation(value = "generateJwt", notes = "Generate Jwt", tags = { "Internal" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Everything ok.", response = HomeResponse.class) })
	@PostMapping("/generateJWT")
	public HomeResponse generateJwt(
			@ApiParam(value = "User name in JWT payload.", required = true) @RequestBody String userName)
			throws UnsupportedEncodingException {

		Date expireAt = DateUtils.getDateAfterMinutes(5);
		String jwtToken = JwtHelper.createToken(userName, expireAt, jwtKey);
		return new HomeResponse(200, jwtToken,
				"Jwt generated with username=" + userName + ", expires at=" + expireAt.toString());
	}

	@ApiOperation(value = "makeHttpsRequest", notes = "Make a https request", tags = { "Internal" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Everything ok.", response = HomeResponse.class) })
	@GetMapping("/json200HTTPS")
	public HomeResponse makeHttpsRequest() throws URISyntaxException, JSONException, IOException {

		URI uri = new URI("https://jsonplaceholder.typicode.com/posts/3"); // NOSONAR
		HttpHeaders headers = new HttpHeaders();
		try {
			ResponseEntity<PlaceHolderPost> resp = restClient.getForGeneric(uri, headers, PlaceHolderPost.class);
			log.info("json200HTTPS-GET response body: {}", resp.getBody());
		} catch (RestClientException e) {
			if (e.getRootCause() instanceof SocketTimeoutException
					|| e.getRootCause() instanceof ConnectTimeoutException) {
				throw new ServiceException(HttpStatus.GATEWAY_TIMEOUT.value(),
						"Connection timeout: failed to get response from 3rd party endpoint.");
			} else {
				throw new ServiceException(HttpStatus.SERVICE_UNAVAILABLE.value(),
						"Unable to reach 3rd party endpoint.");
			}
		}

		uri = new URI("https://jsonplaceholder.typicode.com/posts/"); // NOSONAR
		PlaceHolderPost newPost = new PlaceHolderPost(1, null, "Title", "Content");
		ResponseEntity<PlaceHolderPost> resp2 = restClient.postForGeneric(uri, headers, newPost, PlaceHolderPost.class);
		log.info("json200HTTPS-POST response body: {}", resp2.getBody());

		return new HomeResponse(200, appName, "All tests done: " + resp2.getBody());
	}

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class PlaceHolderPost {
	private Integer userId;
	private Integer id;
	private String title;
	private String body;

}
