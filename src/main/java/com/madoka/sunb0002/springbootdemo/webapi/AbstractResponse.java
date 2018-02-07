/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.webapi;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Sun Bo
 *
 */
@ApiModel(description = "Abstract standard API response")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class AbstractResponse<DataType> {

	@ApiModelProperty(required = true, value = "Specific HTTP status code for API request")
	@JsonProperty
	protected Integer status;
	@ApiModelProperty(value = "Object containing information for success response")
	@JsonProperty
	protected DataType data;
	@ApiModelProperty(value = "Any error or addtional message to be displayed")
	@JsonProperty
	protected String msg;

	public AbstractResponse() {
		super();
	}

	public AbstractResponse(DataType data) {
		super();
		this.status = HttpStatus.OK.value();
		this.data = data;
	}

	public AbstractResponse(Integer status, DataType data, String msg) {
		super();
		this.status = status;
		this.data = data;
		this.msg = msg;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public DataType getData() {
		return data;
	}

	public void setData(DataType data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
