package microsec.freddysbbq.customregistry.openid;

import static java.util.stream.Collectors.toList;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import microsec.freddysbbq.customregistry.JwtTokenKeys;
import microsec.freddysbbq.customregistry.provider.AuthenticatedMainframeUser;

@Component
public class IdTokenEhnancer implements TokenEnhancer {
	
	private final RsaSigner rsaSigner;
	private final ObjectMapper mapper; 
	
	@Autowired
	public IdTokenEhnancer(JwtTokenKeys jwtTokenKeys) {
		this.rsaSigner = new RsaSigner(jwtTokenKeys.getSigningKey());
		this.mapper = new ObjectMapper();
	}
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

		IdToken idToken = createIdToken(accessToken, authentication);
		String jws = signToken(idToken);
		addIdToken(accessToken,jws);
		
		return accessToken;
	}
	
	private IdToken createIdToken(OAuth2AccessToken accessToken, OAuth2Authentication authentication)
	{
		AuthenticatedMainframeUser mainframeUser = (AuthenticatedMainframeUser) authentication.getUserAuthentication(); 
		
		Instant issuedAt = Instant.now();
		Instant expiresAt = issuedAt.plusSeconds(3600);
		
		IdToken idToken = new IdToken();
		idToken.setIssuer("http://localhost:8081/oauth/token");
		idToken.setAudience("uaa");
		idToken.setGivenName(mainframeUser.getFirstName());
		idToken.setFamilyName(mainframeUser.getLastName());
		idToken.setName(mainframeUser.getName());
		idToken.setSubject(mainframeUser.getUsername());
		idToken.setUsername(mainframeUser.getUsername());
		idToken.setEmail(mainframeUser.getEmail());
		idToken.setIssuedAt(issuedAt.toEpochMilli() / 1000);
		idToken.setExpireAt( expiresAt.toEpochMilli() / 1000);
		
		List<String> scopes = mainframeUser.getAuthorities().stream().map( o -> o.getAuthority()).collect(toList());
		
		scopes.add("order.me");
		scopes.add("menu.read");
		idToken.setScope(scopes);
		
		return idToken;
	}

	private String signToken(IdToken token) {
		String body = this.toJson(token);
		Jwt jwt = JwtHelper.encode(body, rsaSigner);
		String jws = jwt.getEncoded();
		return jws;
	}

	private void addIdToken(OAuth2AccessToken accessToken, String jws) {
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
		Map<String,Object> additionalInfo = new HashMap<>();
		additionalInfo.put("id_token", jws);
		token.setAdditionalInformation(additionalInfo);
	}
	
	private String toJson(Object object) {
		try {
			return this.mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}