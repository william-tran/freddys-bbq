package microsec.freddysbbq.customregistry.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import microsec.freddysbbq.customregistry.mainframe.MainframeUser;
import microsec.freddysbbq.customregistry.mainframe.MainframeUserService;

@Service
public class MainframeAuthenticationProvider implements AuthenticationProvider {

	private final MainframeUserService mainframeUserService;
	
	@Autowired
	public MainframeAuthenticationProvider(MainframeUserService mainframeUserService) {
		this.mainframeUserService = mainframeUserService;
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		
		MainframeUser user = getUser(token);
		validatePassword(token, user);
	
		AuthenticatedMainframeUser result =  new AuthenticatedMainframeUser(user);
		return result; 
	}

	private void validatePassword(UsernamePasswordAuthenticationToken token, MainframeUser user) {
		// In this sample we don't do any hashing because it is just a demo in the real word always use hashed password 
		// to defeat password cracking with GPU's see https://blog.codinghorror.com/your-password-is-too-damn-short/ 

		String password = (String) token.getCredentials();
		if(!password.equals(user.getPassword())){
			throw new BadCredentialsException("Wrong password");
		}
	}

	private MainframeUser getUser(UsernamePasswordAuthenticationToken token) {
		String username = (String) token.getPrincipal();
		return this.mainframeUserService.getUserDetails(username)
				                          .orElseThrow( () -> new UsernameNotFoundException("User '" + username + "' not found"));
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
