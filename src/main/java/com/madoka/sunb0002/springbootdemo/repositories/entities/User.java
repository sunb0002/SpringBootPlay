/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.repositories.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Sun Bo
 *
 */
@Entity
@Table(schema = "precure", name = "SBSHOP_USER")
public class User extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9019585072757573100L;

	// MySQL doesn't support Sequence and SpringBoot cannot simulate like normal
	// Spring, hence have to use GenerationType.AUTO
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_USER")
	@SequenceGenerator(name = "SEQ_USER", sequenceName = "SEQ_USER", schema = "precure", allocationSize = 1)
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
