package restapi;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.vps.restapi.Api;
import com.vps.restapi.model.User;

public class ApiTest {
	private String url = "http://localhost:8080/user";

	@Test
	public void CheckUserGet() {
		RestTemplate client = new RestTemplate();
		List<User> response = client
				.exchange(url + "active", HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
				}).getBody();
		System.out.println(response);

	}

	@RunWith(PowerMockRunner.class)
	@PrepareForTest(Api.class)
	@Test
	public void test() {
		PowerMockito.mockStatic(ClassWithStatics.class);

		when(ClassWithStatics.getString()).thenReturn("Hello!");

		System.out.println("String: " + ClassWithStatics.getString());
		System.out.println("Int: " + ClassWithStatics.getInt());
	}

}
