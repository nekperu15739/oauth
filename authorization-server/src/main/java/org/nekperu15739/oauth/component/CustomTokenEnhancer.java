package org.nekperu15739.oauth.component;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

import static org.nekperu15739.oauth.api.JwkSetEndpoint.HTTP_AUTH_SERVER_8090_OAUTH_TOKEN;

public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(final OAuth2AccessToken accessToken, final OAuth2Authentication authentication) {
        final DefaultOAuth2AccessToken cast = DefaultOAuth2AccessToken.class.cast(accessToken);
        final Map<String, Object> map = new HashMap<>();
        map.put(JwtClaimNames.ISS, HTTP_AUTH_SERVER_8090_OAUTH_TOKEN);
        cast.setAdditionalInformation(map);

        return cast;
    }
}
