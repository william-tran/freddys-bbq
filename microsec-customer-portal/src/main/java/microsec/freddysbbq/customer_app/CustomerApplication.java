package microsec.freddysbbq.customer_app;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import microsec.common.Branding;
import microsec.common.DumpTokenEndpointConfig;
import microsec.common.MenuBootstrap;
import microsec.common.Targets;

@SpringBootApplication
@EnableOAuth2Sso
@EnableDiscoveryClient
@EnableCircuitBreaker
@Import(DumpTokenEndpointConfig.class)
public class CustomerApplication extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}

	@Autowired
	private SecurityProperties securityProperties;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		if (securityProperties.isRequireSsl()) {
			http.requiresChannel().anyRequest().requiresSecure();
		}
		http.authorizeRequests().anyRequest().authenticated();
	}

    @Bean
    public Targets targets() {
        return new Targets();
    }

	@Bean
	public Branding branding() {
		return new Branding();
	}

	@Bean
	public MenuBootstrap menuBootstrap() {
		return new MenuBootstrap();
	}

	@LoadBalanced
	@Bean
	public OAuth2RestTemplate loadBalancedOauth2RestTemplate(
			OAuth2ProtectedResourceDetails resource,
			OAuth2ClientContext oauth2Context,
			ObjectMapper objectMapper) {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.registerModule(new Jackson2HalModule());
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
		converter.setObjectMapper(objectMapper);
		OAuth2RestTemplate oauth2RestTemplate = new OAuth2RestTemplate(resource, oauth2Context);
		oauth2RestTemplate.setMessageConverters(Arrays.asList(converter));
		return oauth2RestTemplate;
	}

}