package org.nekperu15739.oauth.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerTokenServicesConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.OAuth2AuthorizationServerConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.JdbcClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

/**
 * copy from OAuth2AuthorizationServerConfiguration
 */
@Configuration
@EnableConfigurationProperties(AuthorizationServerProperties.class)
public class One implements AuthorizationServerConfigurer {

    private final BaseClientDetails details;
    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenConverter tokenConverter;
    private final TokenStore tokenStore;
    private final AuthenticationManager authenticationManager;
    private final AuthorizationServerProperties properties;
    private final UserDetailsService userDetailsService;

    public One(final BaseClientDetails details, final AuthenticationConfiguration authenticationConfiguration,
               final ObjectProvider<TokenStore> tokenStore, final ObjectProvider<AccessTokenConverter> tokenConverter,
               final AuthorizationServerProperties properties, DataSource dataSource, PasswordEncoder passwordEncoder,
               UserDetailsService userDetailsService) throws Exception {
        this.details = details;
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
        this.tokenConverter = tokenConverter.getIfAvailable();
        this.tokenStore = tokenStore.getIfAvailable();
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
        this.properties = properties;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer security) throws Exception {
//        configuration.configure(security);
//        security.passwordEncoder(NoOpPasswordEncoder.getInstance());

        security.passwordEncoder(passwordEncoder);

        if (this.properties.getCheckTokenAccess() != null) {
            security.checkTokenAccess(this.properties.getCheckTokenAccess());
        }
        if (this.properties.getTokenKeyAccess() != null) {
            security.tokenKeyAccess(this.properties.getTokenKeyAccess());
        }
        if (this.properties.getRealm() != null) {
            security.realm(this.properties.getRealm());
        }

        //TODO:
        /*
        security
            .checkTokenAccess("isAuthenticated()")
            .tokenKeyAccess("permitAll()");
            */
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        clients
            .jdbc(dataSource)
            .passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        configuration.configure(endpoints);

        if (this.tokenConverter != null) {
            endpoints.accessTokenConverter(this.tokenConverter);
        }
        if (this.tokenStore != null) {
            endpoints.tokenStore(this.tokenStore);
        }
        if (this.details.getAuthorizedGrantTypes().contains("password")) {
            endpoints.authenticationManager(this.authenticationManager);
        }

        endpoints.userDetailsService(this.userDetailsService);
    }

    @Configuration
    @ConditionalOnMissingBean(BaseClientDetails.class)
    protected static class BaseClientDetailsConfiguration {

        private final OAuth2ClientProperties client;

        protected BaseClientDetailsConfiguration(OAuth2ClientProperties client) {
            this.client = client;
        }

        @Bean
        @ConfigurationProperties(prefix = "security.oauth2.client")
        public BaseClientDetails oauth2ClientDetails() {
            BaseClientDetails details = new BaseClientDetails();
            if (this.client.getClientId() == null) {
                this.client.setClientId(UUID.randomUUID().toString());
            }
            details.setClientId(this.client.getClientId());
            details.setClientSecret(this.client.getClientSecret());
            details.setAuthorizedGrantTypes(Arrays.asList("authorization_code",
                "password", "client_credentials", "implicit", "refresh_token"));
            details.setAuthorities(
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
            details.setRegisteredRedirectUri(Collections.<String>emptySet());
            return details;
        }
    }
}
