package microsec.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

public class TokenRelayingRestTemplate extends OAuth2RestTemplate {
	public TokenRelayingRestTemplate() {
		super(new BaseOAuth2ProtectedResourceDetails(), new DefaultOAuth2ClientContext() {
			private static final long serialVersionUID = 1L;

			@Override
			public OAuth2AccessToken getAccessToken() {
				Authentication principal = SecurityContextHolder.getContext()
						.getAuthentication();
				if (principal instanceof OAuth2Authentication) {
					OAuth2Authentication authentication = (OAuth2Authentication) principal;
					Object details = authentication.getDetails();
					if (details instanceof OAuth2AuthenticationDetails) {
						OAuth2AuthenticationDetails oauthsDetails = (OAuth2AuthenticationDetails) details;
						String token = oauthsDetails.getTokenValue();
						return new DefaultOAuth2AccessToken(token);
					}
				}
				return super.getAccessToken();
			}
		});
	}
}
