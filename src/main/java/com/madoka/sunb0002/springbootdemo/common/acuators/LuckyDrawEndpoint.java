/**
 * 
 */
package com.madoka.sunb0002.springbootdemo.common.acuators;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.madoka.sunb0002.springbootdemo.common.dtos.UserDTO;
import com.madoka.sunb0002.springbootdemo.common.utils.Validators;
import com.madoka.sunb0002.springbootdemo.services.UserService;

/**
 * @author Sun Bo
 * @note: Expose a custom endpoint /luckydraw
 * 
 */
@ConfigurationProperties(prefix = "endpoints.lucky", ignoreUnknownFields = true)
@Service
public class LuckyDrawEndpoint extends AbstractEndpoint<Map<String, Object>> {

	@Autowired
	private UserService userSvc;

	public LuckyDrawEndpoint() {
		super("luckydraw", false);
	}

	@Override
	public Map<String, Object> invoke() {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("Hello", "Qbey " + new Date());

		List<UserDTO> dtos = userSvc.getRandomUser();
		resultMap.put("Is the Order a Rabbit Magical Girl?", dtos);
		if (Validators.isEmpty(dtos)) {
			resultMap.put("Hmmm sorry no magic girl at home", "Try again next time~");
		}

		return resultMap;
	}

}
