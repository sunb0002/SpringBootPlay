/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.webapi.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.madoka.sunb0002.springbootdemo.common.exceptions.ServiceException;

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

	@Value("${app.name}")
	private String appName;

	@ApiOperation(value = "allHail", notes = "Get successful message", tags = { "Internal" })
	@ApiResponses(value = {
			@ApiResponse(code = 403, message = "You'll get forbidden.", response = HomeResponse.class), })
	@GetMapping("/json200")
	public HomeResponse allHail() {

		HomeResponse hr = new HomeResponse(200, appName, "All Hail Madoka");
		LOGGER.info("waifu here info.");
		LOGGER.error("waifu here error.");
		LOGGER.debug("waifu here debug.");
		return hr;
	}

	@ApiOperation(value = "test", notes = "Get unsuccessful message", tags = { "Internal" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Everything ok.", response = HomeResponse.class),
			@ApiResponse(code = 404, message = "Got forbidden.", response = HomeResponse.class),
			@ApiResponse(code = 500, message = "Unexpected Error occurred", response = HomeResponse.class) })
	@GetMapping("/json403")
	public HomeResponse test() throws ServiceException {
		throw new ServiceException(HttpStatus.FORBIDDEN.value(), "forbidden liao");
	}

}
