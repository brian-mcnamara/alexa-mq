package net.bmacattack.auth;

import net.bmacattack.queue.persistence.PrivilegeEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@Configuration
@EnableAuthorizationServer
public class TestAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    public static final String TEST_CLIENT_ID = "mq-test";
    public static final String TEST_CLIENT_SECRET = "testSecret";
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient(TEST_CLIENT_ID)
                .authorizedGrantTypes("authorization_code")
                .authorities(PrivilegeEnum.ADMIN.toString())
                .scopes("Write")
                .secret(TEST_CLIENT_SECRET)
                .accessTokenValiditySeconds(60)
                .refreshTokenValiditySeconds(120);
    }
}
