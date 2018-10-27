package restapi;

import java.util.List;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.vps.restapi.model.User;

public class ApiTest {
	private String url = "http://localhost:8080/user";
	private String user = "{\"uid\":\"5bd48bfaf880d126d0ed2043\",\"email\":\"fchlebo1111111111111111111111111111111wski@gmail.com\",\"firstName\":\"Filip\",\"lastName\":\"Chlebowski\",\"password\":\"sasasassass\",\"active\":true,\"confirmed\":null,\"token\":\"f65e4920-95bd-429c-8e6b-98a1dd9d7663\"}";

	@Test
	public void CheckUserGet() {
		RestTemplate client = new RestTemplate();
		List<User> response = client
				.exchange(url + "active", HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
				}).getBody();
		System.out.println(response);

	}

	@Test
	public void CheckUserAdd() {
		RestTemplate client = new RestTemplate();
		List<User> response = client.postForEntity(url, user);
		System.out.println(response);

	}
}
