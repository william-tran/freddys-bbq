package microsec.freddysbbq.customregistry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import microsec.freddysbbq.customregistry.mainframe.MainframeUserService;
import microsec.freddysbbq.customregistry.provider.MainframeAuthenticationProvider;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth, MainframeUserService mainframeUserService)
			throws Exception {
		auth.authenticationProvider(new MainframeAuthenticationProvider(mainframeUserService));
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// @formatter:off
		http.authorizeRequests()
			.antMatchers("/static/**", "/logout", "/lib/**", "/oauth/token_key").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin().loginPage("/login").permitAll()
			.and()
			.logout().permitAll()
			.and()
			.csrf().disable();
		
		// @formatter:on
	}
}
