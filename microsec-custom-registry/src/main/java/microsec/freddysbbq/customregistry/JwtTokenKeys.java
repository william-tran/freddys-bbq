package microsec.freddysbbq.customregistry;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt.token")
public class JwtTokenKeys {

	private String verificationKey;

	private String signingKey;

	public String getVerificationKey() {
		return verificationKey;
	}

	public void setVerificationKey(String verificationKey) {
		this.verificationKey = verificationKey;
	}

	public String getSigningKey() {
		return signingKey;
	}

	public void setSigningKey(String signingKey) {
		this.signingKey = signingKey;
	}
}
