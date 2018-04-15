/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.common.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Sun Bo
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3979794478820717171L;

	private Long userId;
	private String nric;
	private String name;

}
