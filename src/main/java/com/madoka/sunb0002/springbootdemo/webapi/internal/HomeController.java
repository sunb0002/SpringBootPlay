/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.webapi.internal;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.madoka.sunb0002.springbootdemo.common.aop.LogAnno;
import com.madoka.sunb0002.springbootdemo.common.dtos.UserDTO;
import com.madoka.sunb0002.springbootdemo.common.exceptions.ServiceException;
import com.madoka.sunb0002.springbootdemo.common.utils.Validators;
import com.madoka.sunb0002.springbootdemo.services.MailService;
import com.madoka.sunb0002.springbootdemo.services.UserService;
import com.madoka.sunb0002.springbootdemo.services.jms.Producer;

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

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;
	@Autowired
	private MailService mailService;
	@Autowired
	private Producer mqProducer;

	@Value("${app.name}")
	private String appName;

	@ApiOperation(value = "allHail", notes = "Get successful message", tags = { "Internal" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Everything ok.", response = HomeResponse.class),
			@ApiResponse(code = 403, message = "You'll get forbidden.", response = HomeResponse.class), })
	@GetMapping("/json200")
	@LogAnno
	public HomeResponse allHail() {
		logger.info("waifu here info.");
		logger.error("waifu here error.");
		logger.debug("waifu here debug.");
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
		logger.info("AsyncTask start: {}", new Date());
		Future<String> futureStr = userService.asyncTaskWithFuture();

		// Can also use "while futureStr.isDone()" below.
		String result = futureStr.get();

		logger.info("AsyncTask done: {}", new Date());
		return new HomeResponse(200, appName, "All tests done: " + result);
	}

}
