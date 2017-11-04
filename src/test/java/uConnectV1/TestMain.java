package voyagerV1;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.cd.voyager.entities.Registration;

public class TestMain {
	
/*	
	public static void main(String[] args) {
		getEmployees();
		
	}

	private static void getEmployees()
	{
		final String uri = "http://localhost:8080/voyager/register";
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		Registration r  = new Registration();
		r.setEmail("test@123.com");
		
		Registration r2  = restTemplate.postForObject(uri, r, Registration.class);
		
		System.out.println(r2);
	}
*/
}
