/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.webapi.internal;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.madoka.sunb0002.springbootdemo.common.aop.LogAnno;
import com.madoka.sunb0002.springbootdemo.common.exceptions.ServiceException;
import com.madoka.sunb0002.springbootdemo.services.MailService;
import com.madoka.sunb0002.springbootdemo.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Sun Bo
 *
 */
@Api
@RestController
@RequestMapping("/")
public class HomeController {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private MailService mailService;

	@Value("${app.name}")
	private String appName;

	@ApiOperation(value = "allHail", notes = "Get successful message", tags = { "Internal" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Everything ok.", response = HomeResponse.class),
			@ApiResponse(code = 403, message = "You'll get forbidden.", response = HomeResponse.class), })
	@GetMapping("/json200")
	@LogAnno
	public HomeResponse allHail() {
		LOGGER.info("waifu here info.");
		LOGGER.error("waifu here error.");
		LOGGER.debug("waifu here debug.");
		return new HomeResponse(200, appName, "All Hail Madoka");
	}

	@ApiOperation(value = "test", notes = "Get unsuccessful message", tags = { "Internal" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Everything ok.", response = HomeResponse.class),
			@ApiResponse(code = 404, message = "Got forbidden.", response = HomeResponse.class),
			@ApiResponse(code = 500, message = "Unexpected Error occurred", response = HomeResponse.class) })
	@GetMapping("/json403")
	public HomeResponse test() {
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

	@ApiOperation(value = "testAnything", notes = "Test anything", tags = { "Internal" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Everything ok.", response = HomeResponse.class),
			@ApiResponse(code = 403, message = "You'll get forbidden.", response = HomeResponse.class), })
	@GetMapping("/json200MK2")
	@LogAnno("Anno-testAnything")
	public HomeResponse testAnything() throws InterruptedException, ExecutionException {

		// userService.asyncTask(); //NOSONAR
		LOGGER.info("AsyncTask start: {}", new Date());
		Future<String> futureStr = userService.asyncTaskWithFuture();

		// Can also use "while futureStr.isDone()" below.
		String result = futureStr.get();

		LOGGER.info("AsyncTask done: {}", new Date());
		return new HomeResponse(200, appName, "All tests done: " + result);
	}

}
