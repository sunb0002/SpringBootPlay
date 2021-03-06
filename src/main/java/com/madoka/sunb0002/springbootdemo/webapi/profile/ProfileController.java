/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.webapi.profile;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.madoka.sunb0002.springbootdemo.common.aop.LogAnno;
import com.madoka.sunb0002.springbootdemo.common.dtos.AdminDTO;
import com.madoka.sunb0002.springbootdemo.common.dtos.UserDTO;
import com.madoka.sunb0002.springbootdemo.common.exceptions.ServiceException;
import com.madoka.sunb0002.springbootdemo.common.utils.Validators;
import com.madoka.sunb0002.springbootdemo.repositories.UserRepository;
import com.madoka.sunb0002.springbootdemo.repositories.entities.User;
import com.madoka.sunb0002.springbootdemo.services.DtoParserService;
import com.madoka.sunb0002.springbootdemo.services.UserService;
import com.madoka.sunb0002.springbootdemo.webapi.AbstractResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author Sun Bo
 *
 */
@Api
@RestController
@RequestMapping("/profile")
public class ProfileController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private DtoParserService dtoParserSvc;

	@ApiOperation(value = "getUserById", notes = "Get User by Id", tags = { "Profile" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "User is found.", response = ProfileResponseUser.class),
			@ApiResponse(code = 404, message = "User is not found.", response = ProfileResponseUser.class),
			@ApiResponse(code = 500, message = "Unexpected Error occurred", response = ProfileResponseUser.class) })
	@GetMapping("/user/{id}")
	@LogAnno("Anno-getUserById")
	public ProfileResponseUser getUserById(@PathVariable Long id) {
		logger.info("Getting users with id: {}", id);
		User u = userRepo.findOne(id);
		if (u == null) {
			throw new ServiceException(HttpStatus.NOT_FOUND.value(), "No magical girl with id=" + id);
		}
		return new ProfileResponseUser(dtoParserSvc.parseUser(u));
	}

	@ApiOperation(value = "searchUsersByName", notes = "Search Users with name", tags = { "Profile" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Users are found.", response = ProfileResponseUserList.class),
			@ApiResponse(code = 404, message = "Users are not found.", response = ProfileResponseUserList.class),
			@ApiResponse(code = 500, message = "Unexpected Error occurred", response = ProfileResponseUserList.class) })
	@GetMapping("/user")
	@LogAnno("Anno-searchUsersByName")
	public ProfileResponseUserList searchUsersByName(@RequestParam(value = "name", required = true) String name) {
		logger.info("Getting users with name like: {}", name);
		List<UserDTO> users = userService.getSomeUsersWithSimilarName(name);
		if (Validators.isEmpty(users)) {
			throw new ServiceException(HttpStatus.NOT_FOUND.value(), "No magical girl(s) with name like: " + name);
		}
		return new ProfileResponseUserList(users);
	}

	@ApiOperation(value = "getAllUsers", notes = "Get all users", tags = { "Profile" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Users are found.", response = ProfileResponseUserList.class),
			@ApiResponse(code = 404, message = "Users are not found.", response = ProfileResponseUserList.class),
			@ApiResponse(code = 500, message = "Unexpected Error occurred", response = ProfileResponseUserList.class) })
	@GetMapping("/allusers")
	@LogAnno("Anno-getAllUsers")
	public ProfileResponseUserList getAllUsers() {
		List<User> users = userRepo.findAll();
		if (Validators.isEmpty(users)) {
			throw new ServiceException(HttpStatus.NOT_FOUND.value(), "No magical girl(s) in database.");
		}
		return new ProfileResponseUserList(dtoParserSvc.parseUsers(users));
	}

	@ApiOperation(value = "saveUserProfile", notes = "Create or update user profile", tags = { "Profile" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Profile has been updated.", response = ProfileResponseUser.class),
			@ApiResponse(code = 500, message = "Unexpected Error occurred", response = ProfileResponseUser.class) })
	@PostMapping("/user")
	@LogAnno("Anno-saveUserProfile")
	public ProfileResponseUser saveUserProfile(
			@ApiParam(value = "Request body to save user profile.", required = true) @RequestBody UserDTO userDto) {
		logger.info("To SaveOrUpdate user profile: {}", userDto);
		try {
			userDto = userService.saveUserProfile(userDto);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		}
		return new ProfileResponseUser(userDto);
	}

	@ApiOperation(value = "getAdminDetails", notes = "Get admin details from jwt", tags = { "Profile" })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Admin profile has been retrieved.", response = AdminResponse.class),
			@ApiResponse(code = 500, message = "Unexpected Error occurred", response = AdminResponse.class) })
	@GetMapping("/admin")
	public AdminResponse getAdminDetails(@ApiIgnore @RequestAttribute(name = "adminName") String adminName,
			@ApiIgnore @RequestAttribute(name = "adminExpireAt") Date adminExpiry) {
		logger.debug("Admin name={}, expiryAt={}", adminName, adminExpiry);
		return new AdminResponse(new AdminDTO(null, adminName, adminExpiry));
	}

}

@ApiModel(description = "Profile API response: single user")
class ProfileResponseUser extends AbstractResponse<UserDTO> {
	public ProfileResponseUser(UserDTO data) {
		super(data);
	}

	public ProfileResponseUser(Integer status, UserDTO data, String msg) {
		super(status, data, msg);
	}
}

@ApiModel(description = "Profile API response: multiple users")
class ProfileResponseUserList extends AbstractResponse<List<UserDTO>> {
	public ProfileResponseUserList(List<UserDTO> data) {
		super(data);
	}
}

@ApiModel(description = "Profile API response: admin details")
class AdminResponse extends AbstractResponse<AdminDTO> {
	public AdminResponse(AdminDTO data) {
		super(data);
	}

	public AdminResponse(Integer status, AdminDTO data, String msg) {
		super(status, data, msg);
	}

	@Override
	public String toString() {
		return "AdminResponse [status=" + status + ", data=" + data + ", msg=" + msg + "]";
	}

}
