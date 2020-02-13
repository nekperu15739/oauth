package org.nekperu15739.oauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;

//@Import(AuthorizationServerEndpointsConfiguration.class)
//@Configuration
public class JwkSetEndpointConfiguration extends AuthorizationServerSecurityConfiguration {

    public static final String WELL_KNOWN_JWKS_JSON = "/.well-known/jwks.json";
    public static final String WELL_KNOWN_OPENID_CONFIGURATION = "/.well-known/oauth-authorization-server/oauth/token";
    public static final String TOKEN_KEYS = "token_keys";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http
            .requestMatchers()
            .mvcMatchers(WELL_KNOWN_OPENID_CONFIGURATION)
            .and()
            .authorizeRequests()
            .mvcMatchers(WELL_KNOWN_OPENID_CONFIGURATION).permitAll()
            .and()
            .requestMatchers()
            .mvcMatchers(TOKEN_KEYS)
            .and()
            .authorizeRequests()
            .mvcMatchers(TOKEN_KEYS).permitAll()
        ;
    }
}
