/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.webapi;

import io.swagger.annotations.ApiModel;

/**
 * @author Sun Bo
 *
 */
@ApiModel(description = "Universal standard API response")
public class GeneralResponse extends AbstractResponse<String> {

	public GeneralResponse() {
		super();
	}

	public GeneralResponse(Integer status, String data, String msg) {
		super(status, data, msg);
	}

	@Override
	public String toString() {
		return "GeneralResponse [status=" + status + ", data=" + data + ", msg=" + msg + "]";
	}
}
