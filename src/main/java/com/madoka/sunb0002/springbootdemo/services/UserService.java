/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.services;

import java.util.List;

import com.madoka.sunb0002.springbootdemo.common.dtos.UserDTO;
import com.madoka.sunb0002.springbootdemo.common.exceptions.ServiceException;

/**
 * @author Sun Bo
 *
 */
public interface UserService {

	/**
	 * 
	 * @param name
	 * @return
	 */
	List<UserDTO> getSomeUsersWithSimilarName(String name);

	/**
	 * 
	 * @param userDto
	 * @return
	 */
	UserDTO saveUserProfile(UserDTO userDto) throws ServiceException;

}
