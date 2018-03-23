/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.repositories.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * @author Sun Bo
 *
 */
@Entity
@Audited
@Table(schema = "precure", name = "BOOT__SBSHOP_USER")
public class User extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9019585072757573100L;

	// MySQL doesn't support Sequence and SpringBoot Dialect cannot simulate
	// like normal Spring, hence have to use GenerationType.IDENTIY, TABLE or
	// AUTO.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;

	@Column(length = 9, nullable = false, unique = true)
	private String nric;

	@Column(length = 50, nullable = false)
	private String name;

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
		return "User [userId=" + userId + ", nric=" + nric + ", name=" + name + "]";
	}

}
