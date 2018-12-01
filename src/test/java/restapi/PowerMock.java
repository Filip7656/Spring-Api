package restapi;

import static org.hamcrest.CoreMatchers.any;

import java.io.IOException;

import org.apache.commons.mail.EmailException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.vps.restapi.Api;
import com.vps.restapi.model.User;
import com.vps.restapi.utils.EmailSender;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Api.class)
public class PowerMock {

	@Mock
	private Api api;
	private EmailSender emailsender;

	@Test
	public void TestApi() throws TemplateNotFoundException, MalformedTemplateNameException, ParseException,
			EmailException, IOException, TemplateException {
		RestTemplate client = new RestTemplate();
		User request = new User();
		request.setEmail("12121@121.11");
		request.setPassword("12121");
		PowerMockito.when(api.create(request));

	}

	@Test
	public void checkRegister() {
		User request = new User();
		request.setEmail("12121@121.11");
		request.setPassword("12121");
		PowerMockito.when(EmailSender.newAccountEmail(request);		
		RestTemplate client = new RestTemplate();
		User request = new User();
		request.setFirstName("Piotrek");
		request.setLastName("Adamski");
		request.setEmail("test");
		request.setPassword("secret");
		ResponseEntity<User> postForEntity = client.postForEntity("http://localhost:8080/user", request, User.class);
		System.out.println(postForEntity);
	}
}
