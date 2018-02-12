/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madoka.sunb0002.springbootdemo.common.aop.LogAnno;
import com.madoka.sunb0002.springbootdemo.common.dtos.UserDTO;
import com.madoka.sunb0002.springbootdemo.common.exceptions.ServiceException;
import com.madoka.sunb0002.springbootdemo.repositories.UserRepository;
import com.madoka.sunb0002.springbootdemo.repositories.entities.User;
import com.madoka.sunb0002.springbootdemo.services.DtoParserService;
import com.madoka.sunb0002.springbootdemo.services.UserService;

/**
 * @author Sun Bo
 *
 */
@Service
public class UserServiceImpl implements UserService {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private DtoParserService dtoParserSvc;

	@Override
	@Transactional(value = "sbshop-txnmgr", readOnly = true)
	@LogAnno("Anno-Service-getSomeUsersWithSimilarName")
	public List<UserDTO> getSomeUsersWithSimilarName(String name) {
		LOGGER.info("Service is searching users with name like: {}", name);
		return dtoParserSvc.parseUsers(userRepo.findNricByNameLikeUsingQuery(name));
	}

	@Override
	@Transactional("sbshop-txnmgr")
	public UserDTO saveUserProfile(UserDTO userDto) throws ServiceException {

		User u = new User();
		Long uid = userDto.getUserId();
		if (uid != null) {
			u = userRepo.findOne(uid);
			if (u == null) {
				throw new ServiceException("User with id=" + uid + " is not found, unable to update.");
			}
			u.setModifiedDate(new Date());
		} else {
			u.setCreatedDate(new Date());
		}
		u.setNric(userDto.getNric());
		u.setName(userDto.getName());

		u = userRepo.save(u);
		return dtoParserSvc.parseUser(u);
	}

	@Override
	public List<UserDTO> getRandomUser() {
		Random r = new Random();
		char c = (char) (r.nextInt(26) + 'a');
		List<UserDTO> result = getSomeUsersWithSimilarName(c + "");
		LOGGER.info("Fetched user with char={}, result={}", c, result);
		return result;
	}

	@Override
	@Async("Async-Executor2")
	@LogAnno("Anno-Service-asyncTask")
	public void asyncTask() {
		LOGGER.info("AsyncTask reporting.");
		LOGGER.info("Retrieved {} users.", getRandomUser().size());
	}

	@Override
	@Async("Async-Executor1")
	@LogAnno("Anno-Service-asyncTaskWithFuture")
	public Future<String> asyncTaskWithFuture() throws InterruptedException {
		int result = getRandomUser().size();
		Thread.sleep(2500);
		return new AsyncResult<String>("Retrieved so many users: " + result);
	}

}
