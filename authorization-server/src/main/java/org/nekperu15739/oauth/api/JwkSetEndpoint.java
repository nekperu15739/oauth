package org.nekperu15739.oauth.api;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import static org.nekperu15739.oauth.config.JwkSetEndpointConfiguration.TOKEN_KEYS;
import static org.nekperu15739.oauth.config.JwkSetEndpointConfiguration.WELL_KNOWN_OPENID_CONFIGURATION;

@Slf4j
//@FrameworkEndpoint
@RequiredArgsConstructor
public class JwkSetEndpoint {

    public static final String HTTP_AUTH_SERVER_8090_OAUTH_TOKEN = "http://auth-server:8090/oauth/token";
    private final KeyPair keyPair;

    @GetMapping(WELL_KNOWN_OPENID_CONFIGURATION)
    @ResponseBody
    public Map<String, Object> getKey() {
        log.info("info");
        final Map<String, Object> out = new HashMap<>();
        out.put("issuer", HTTP_AUTH_SERVER_8090_OAUTH_TOKEN);
        out.put("jwks_uri", "http://localhost:8090/token_keys");
        return out;
    }

    @GetMapping(TOKEN_KEYS)
    @ResponseBody
    public Map<String, Object> keys() {
        log.info("keys");
        RSAPublicKey publicKey = (RSAPublicKey) this.keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }
}
