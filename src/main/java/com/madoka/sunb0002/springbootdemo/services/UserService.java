/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.services;

import java.util.List;
import java.util.concurrent.Future;

import com.madoka.sunb0002.springbootdemo.common.dtos.UserDTO;

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
	UserDTO saveUserProfile(UserDTO userDto);

	/**
	 * 
	 * @return
	 */
	List<UserDTO> getRandomUser();

	/**
	 * 
	 */
	void asyncTask() throws InterruptedException;

	/**
	 * 
	 * @return
	 */
	Future<String> asyncTaskWithFuture() throws InterruptedException;

}
