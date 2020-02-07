package org.nekperu15739.oauth.config;

import lombok.RequiredArgsConstructor;
import org.nekperu15739.oauth.component.CustomTokenEnhancer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * login on server
     */
    public static final String AUTHORIZATION_CODE = "authorization_code";
    public static final String PASSWORD = "password";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String CLIENT_CREDENTIALS = "client_credentials";
    public static final String IMPLICIT = "implicit";

    private final AuthenticationManager authenticationManager;
    private final KeyPair keyPair;

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * org.springframework.security.oauth2.core.AuthorizationGrantType
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
            .inMemory()
            .withClient("client-1234")
            .secret(passwordEncoder().encode("secret"))
            .authorizedGrantTypes(PASSWORD, AUTHORIZATION_CODE)
            .scopes("profile")
//            .redirectUris("http://localhost:8080/login/oauth2/code/ryanair")
//            .redirectUris("https://www.getpostman.com/oauth2/callback")
            .redirectUris("https://app.getpostman.com/oauth2/callback")
        ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        final List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(new CustomTokenEnhancer());
        delegates.add(accessTokenConverter());
        tokenEnhancerChain.setTokenEnhancers(delegates);

        endpoints
            .tokenEnhancer(tokenEnhancerChain)
            .accessTokenConverter(accessTokenConverter())
            .authenticationManager(this.authenticationManager)
            .tokenStore(tokenStore());
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(this.keyPair);
        return converter;
    }
}