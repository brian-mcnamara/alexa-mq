package net.bmacattack.queue.auth;

import net.bmacattack.queue.persistence.RoleEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
@Order(1)
public class TestAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    public static final String TEST_CLIENT_ID = "mq-test";
    public static final String TEST_CLIENT_SECRET = "testSecret";

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(testClientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(testTokenStore());
    }

    @Bean
    @Primary
    public TokenStore testTokenStore() throws Exception {
        return new InMemoryTokenStore();
    }
    @Bean
    @Primary
    public ClientDetailsService testClientDetailsService() throws Exception {
        ClientDetailsServiceBuilder builder = new ClientDetailsServiceBuilder();
        return builder.inMemory().withClient(TEST_CLIENT_ID)
                .authorizedGrantTypes("authorization_code")
                .authorities(RoleEnum.ADMIN.toString())
                .scopes("Write", "Read")
                .secret(TEST_CLIENT_SECRET)
                .resourceIds("api")
                .accessTokenValiditySeconds(60)
                .refreshTokenValiditySeconds(120).and().build();
    }
}
