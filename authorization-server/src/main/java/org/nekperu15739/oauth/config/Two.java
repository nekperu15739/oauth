package org.nekperu15739.oauth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerTokenServicesConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
//@Import(AuthorizationServerTokenServicesConfiguration.class)
public class Two {

    //    @Primary
//        (name = "jdbcTokenStore")
    @Bean
    public TokenStore tokenStore(final DataSource dataSource) {
        //TODO:
//        JwtTokenStore store = new JwtTokenStore();

        JdbcTokenStore tokenStore = new JdbcTokenStore(dataSource);
        return tokenStore;

//        return new JwtTokenStore(accessTokenConverter2());
    }

    private final AuthorizationServerProperties authorization;
    private final ApplicationContext context;

    @Bean
    public JwtAccessTokenConverter accessTokenConverter2() {
        Assert.notNull(this.authorization.getJwt().getKeyStore(), "keyStore cannot be null");
        Assert.notNull(this.authorization.getJwt().getKeyStorePassword(), "keyStorePassword cannot be null");
        Assert.notNull(this.authorization.getJwt().getKeyAlias(), "keyAlias cannot be null");

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        Resource keyStore = this.context.getResource(this.authorization.getJwt().getKeyStore());
        char[] keyStorePassword = this.authorization.getJwt().getKeyStorePassword().toCharArray();
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(keyStore, keyStorePassword);

        String keyAlias = this.authorization.getJwt().getKeyAlias();
        char[] keyPassword = Optional.ofNullable(
            this.authorization.getJwt().getKeyPassword())
            .map(String::toCharArray).orElse(keyStorePassword);
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair(keyAlias, keyPassword));

        return converter;
    }
}
