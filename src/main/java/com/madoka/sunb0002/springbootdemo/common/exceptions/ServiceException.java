/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.common.exceptions;

/**
 * @author Sun Bo
 *
 */
public class ServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6780199010096231613L;
	private Integer status;

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Integer status, String message) {
		super(message);
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ServiceException [status=" + status + "], message=[" + this.getMessage() + "]";
	}

}
