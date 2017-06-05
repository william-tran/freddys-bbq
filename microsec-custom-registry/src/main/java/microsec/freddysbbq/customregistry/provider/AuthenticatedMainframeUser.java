package microsec.freddysbbq.customregistry.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import microsec.freddysbbq.customregistry.mainframe.MainframeUser;

public final class AuthenticatedMainframeUser implements Authentication {

	private static final long serialVersionUID = 1L;

	private final String username; 	
	private final String firstName; 
	private final String lastName;
	private final String email; 
	private final Collection<GrantedAuthority> authorities;

	
	AuthenticatedMainframeUser(MainframeUser user) {
		
		this.username = user.getUsername();
		this.firstName=user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		
		List<GrantedAuthority> rights = new ArrayList<>();
		if(user.isEmployee()) {
			rights.add( new SimpleGrantedAuthority("employee"));
		} else {
			rights.add( new SimpleGrantedAuthority("customer"));
		}
		
		this.authorities = Collections.unmodifiableList(rights);
	}
	
	@Override
	public String getName() {
		return this.getFirstName() + " " + this.getLastName();
	}

	@Override
	public Object getPrincipal() {
		return this.getUsername();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}


	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	@Override
	public Object getCredentials() {
		return null;
	}
	
	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		throw new UnsupportedOperationException();
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getUsername() {
		return username;
	}

}
