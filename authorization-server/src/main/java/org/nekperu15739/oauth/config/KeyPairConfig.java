package org.nekperu15739.oauth.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.security.KeyPair;

//@Configuration
public class KeyPairConfig {

    @Bean
    @SneakyThrows
    KeyPair keyPair() {
        final File file = ResourceUtils.getFile("classpath:.keystore-oauth2-demo");
        final FileSystemResource fileSystemResource = new FileSystemResource(file);
        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(fileSystemResource, "admin1234".toCharArray());
        return factory.getKeyPair("oauth2-demo-key");
    }
}
