package microsec.test;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Copies the model of UAA's JWT token
 * 
 * @author wtran@pivotal.io
 *
 */
@JsonInclude(Include.NON_NULL)
public class UaaJwtToken {
    private String jti;
    private String sub;
    private Collection<String> authorities;
    private Collection<String> scope;
    private String client_id;
    private String cid;
    private String azp;
    private String grant_type;
    private String user_id;
    private String user_name;
    private String email;
    private long iat;
    private long exp;
    private String iss;
    private String zid;
    private Collection<String> aud;

    public static UaaJwtTokenBuilder builder() {
        return new UaaJwtTokenBuilder();
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public Collection<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<String> authorities) {
        this.authorities = authorities;
    }

    public Collection<String> getScope() {
        return scope;
    }

    public void setScope(Collection<String> scope) {
        this.scope = scope;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getAzp() {
        return azp;
    }

    public void setAzp(String azp) {
        this.azp = azp;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getIat() {
        return iat;
    }

    public void setIat(long iat) {
        this.iat = iat;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getZid() {
        return zid;
    }

    public void setZid(String zid) {
        this.zid = zid;
    }

    public Collection<String> getAud() {
        return aud;
    }

    public void setAud(Collection<String> aud) {
        this.aud = aud;
    }

    /**
     * Copies the behaviour of UAA's JWT token in relation to how some fields
     * affect others
     * 
     * @author wtran@pivotal.io
     *
     */
    public static class UaaJwtTokenBuilder {

        public static enum GrantType {
            implicit, client_credentials, authorization_code, password
        }

        private UaaJwtToken token;

        UaaJwtTokenBuilder() {
            token = new UaaJwtToken();
            token.setJti(UUID.randomUUID().toString());
            token.setIat(System.currentTimeMillis());
            token.setExp(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
        }

        public UaaJwtTokenBuilder scopes(String... scopes) {
            if (token.getScope() == null) {
                token.setScope(new LinkedHashSet<>());
            }
            token.getScope().addAll(Arrays.asList(scopes));
            return this;
        }

        public UaaJwtTokenBuilder clearScopes() {
            token.setScope(null);
            return this;
        }

        public UaaJwtTokenBuilder clientId(String clientId) {
            token.setClient_id(clientId);
            token.setCid(clientId);
            token.setAzp(clientId);
            return this;
        }

        public UaaJwtTokenBuilder grantType(GrantType grantType) {
            token.setGrant_type(grantType.name());
            return this;
        }

        public UaaJwtTokenBuilder user(String userId, String userName, String email) {
            token.setUser_id(userId);
            token.setUser_name(userName);
            token.setEmail(email);
            return this;
        }

        public UaaJwtTokenBuilder issuer(String iss) {
            token.setIss(iss);
            return this;
        }

        public UaaJwtTokenBuilder zoneId(String zid) {
            token.setZid(zid);
            return this;
        }

        public UaaJwtToken build() {
            LinkedHashSet<String> aud = new LinkedHashSet<>();
            aud.add(token.getCid());
            if (token.getScope() != null) {
                token.getScope().forEach(scope -> {
                    aud.add(scope.substring(0, scope.lastIndexOf('.') > 0 ? scope.lastIndexOf('.') : scope.length()));
                });
            }
            token.setAud(aud);
            if (token.getUser_id() != null) {
                token.setSub(token.getUser_id());
            } else {
                token.setSub(token.getCid());
            }

            if (GrantType.client_credentials.name().equals(token.getGrant_type())) {
                token.setAuthorities(token.getScope());
            }
            return token;
        }
    }

}