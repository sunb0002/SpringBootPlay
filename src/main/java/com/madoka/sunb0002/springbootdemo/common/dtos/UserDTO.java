/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.common.dtos;

import java.io.Serializable;

/**
 * @author Sun Bo
 *
 */
public class UserDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3979794478820717171L;

	private Long userId;
	private String nric;
	private String name;

	public UserDTO() {
		super();
	}

	public UserDTO(Long userId, String nric, String name) {
		super();
		this.userId = userId;
		this.nric = nric;
		this.name = name;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNric() {
		return nric;
	}

	public void setNric(String nric) {
		this.nric = nric;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "UserDTO [userId=" + userId + ", nric=" + nric + ", name=" + name + "]";
	}

}
