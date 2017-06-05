package microsec.freddysbbq.customregistry.util;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 
 * @author adib
 * 
 */
public class SpringSecurityUtils
{
	private static final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

	public static boolean isAuthenticated()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return !trustResolver.isAnonymous(authentication);
	}

	public static boolean isFullyAuthenticated()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return !trustResolver.isAnonymous(authentication) && !trustResolver.isRememberMe(authentication);
	}

	public static UserDetails getCurrentUserDetails()
	{
		Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (object instanceof UserDetails)
			return (UserDetails) object;
		return null;
	}
}
