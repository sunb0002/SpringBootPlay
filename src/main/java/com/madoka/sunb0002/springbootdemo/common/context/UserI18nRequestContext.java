package com.madoka.sunb0002.springbootdemo.common.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class UserI18nRequestContext extends UserRequestContext {

	private String userName;
	private String rank;
	private Skill skill;

	@Data
	public static class Skill {
		private String name;
		private Integer damage;
	}

}
