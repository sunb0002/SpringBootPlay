/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.services;

import java.util.List;

import com.madoka.sunb0002.springbootdemo.common.dtos.UserDTO;
import com.madoka.sunb0002.springbootdemo.repositories.entities.User;

/**
 * @author Sun Bo
 *
 */
public interface DtoParserService {

	/**
	 * 
	 * @param user
	 * @return
	 */
	UserDTO parseUser(User user);

	/**
	 * 
	 * @param userDto
	 * @return
	 */
	User parseUserDTO(UserDTO userDto);

	/**
	 * 
	 * @param users
	 * @return
	 */
	List<UserDTO> parseUsers(List<User> users);

}
