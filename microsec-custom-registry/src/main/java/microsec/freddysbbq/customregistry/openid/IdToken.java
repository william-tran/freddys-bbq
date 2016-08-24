package microsec.freddysbbq.customregistry.openid;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdToken {

	@JsonProperty("iss")
	private String issuer; 
	
	@JsonProperty("sub")
	private String subject;
	
	@JsonProperty("user_name")
	private String username; 
	
	@JsonProperty("given_name")
	private String givenName;
	
	@JsonProperty("family_name")
	private String familyName;
	
	@JsonProperty("aud")
	private String audience;
	
	@JsonProperty("exp")
	private Long expireAt;
	
	@JsonProperty("iat")
	private Long issuedAt;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("scope")
	private List<String> scope;

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	public Long getExpireAt() {
		return expireAt;
	}

	public void setExpireAt(Long expireAt) {
		this.expireAt = expireAt;
	}

	public Long getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(Long issuedAt) {
		this.issuedAt = issuedAt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getScope() {
		return scope;
	}

	public void setScope(List<String> scope) {
		this.scope = scope;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	} 
	
}
