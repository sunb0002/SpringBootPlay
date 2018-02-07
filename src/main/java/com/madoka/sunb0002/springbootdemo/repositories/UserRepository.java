/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.madoka.sunb0002.springbootdemo.repositories.entities.User;

/**
 * @author Sun Bo
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {

	User findByNric(String nric);

	List<User> findAllbyNricByNameLike(String name);

	@Query("SELECT u from User u where u.name like %:name% ")
	List<User> findNricByNameLikeUsingQuery(@Param("name") String name);

	Long countByName(String name);

}
