package net.bmacattack.queue.it;

import com.google.common.collect.Sets;
import net.bmacattack.queue.auth.TestAuthorizationServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

public class OauthMqEndpointsITest extends MqEndpointsITest {
    @Autowired
    private AuthorizationServerTokenServices tokenServices;

    @Override
    HttpHeaders getAuthentication() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer " + getAccessToken("dev", "dev"));
        return headers;
    }

    private String getAccessToken(String username, String password) {
        OAuth2Request oAuth2Request = new OAuth2Request(null, TestAuthorizationServerConfig.TEST_CLIENT_ID,
                null, true, Sets.newHashSet("Read", "Write"), null, null, null, null);
        Authentication userAuth = new TestingAuthenticationToken(username, password, (String)"Test");
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, userAuth);
        OAuth2AccessToken accessToken = tokenServices.createAccessToken(oAuth2Authentication);
        return accessToken.getValue();
    }
}
