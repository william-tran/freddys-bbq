package microsec.freddysbbq.customregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableAuthorizationServer
@EnableWebSecurity
public class CustomRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomRegistryApplication.class, args);
	}
}
