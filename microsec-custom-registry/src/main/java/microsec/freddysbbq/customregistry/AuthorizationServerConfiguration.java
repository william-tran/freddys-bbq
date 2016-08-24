package microsec.freddysbbq.customregistry;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import microsec.freddysbbq.customregistry.openid.IdTokenEhnancer;

@Configuration 
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private IdTokenEhnancer enhancer;
	
	@Autowired
	private TokenStore tokenStore;

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtAccessTokenConverter accessTokenConverter;

	@Bean
	public JwtAccessTokenConverter accessTokenConverter(JwtTokenKeys jwtTokenKeys) {
		 JwtAccessTokenConverter convertor = new JwtAccessTokenConverter();
		 String signingKey = jwtTokenKeys.getSigningKey();
		 convertor.setSigningKey(signingKey);
		 convertor.setVerifierKey(jwtTokenKeys.getVerificationKey());
		 return convertor;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		// @formatter:off
		clients.inMemory()
		        .withClient("uaa")
		        .secret("password")
		        .authorizedGrantTypes("authorization_code")
				.authorities("ROLE_CLIENT")
				.scopes("openid")
				.autoApprove(true)
				.accessTokenValiditySeconds(60)
	            .refreshTokenValiditySeconds(3600);
		// @formatter:on
	}

	@Bean
	public TokenStore tokenStore(JwtAccessTokenConverter accessTokenConverter) {
		return new JwtTokenStore(accessTokenConverter);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
	TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		    tokenEnhancerChain.setTokenEnhancers(Arrays.asList(this.accessTokenConverter,this.enhancer));
		    
		endpoints.tokenStore(this.tokenStore)
		         .authenticationManager(this.authenticationManager)
		         .accessTokenConverter(this.accessTokenConverter)
				 .tokenEnhancer(tokenEnhancerChain);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("isAnonymous() || hasAuthority('ROLE_CLIENT')").checkTokenAccess(
				"hasAuthority('ROLE_CLIENT')");
	}

}