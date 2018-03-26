/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.common.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author sunbo
 *
 */
@Data
@AllArgsConstructor
public class AdminDTO {

	private Long id;
	private String name;
	private Date expireAt;

}
