package net.bmacattack.queue.it;

import net.bmacattack.queue.persistence.model.UserAccessToken;
import org.springframework.http.HttpHeaders;

import java.util.Base64;

public class TokenMqEndpointsITest extends MqEndpointsITest {
    private static final String TOKEN_PASSWORD = "test";

    private UserAccessToken token;

    @Override
    public void init() {
        super.init();
        token = new UserAccessToken("test", TOKEN_PASSWORD, "Read,Write");
        user.addAccessToken(token);
    }

    @Override
    HttpHeaders getAuthentication() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization",
                "Basic " + Base64.getEncoder().encodeToString((user.getUsername() + ":" + TOKEN_PASSWORD).getBytes()));
        return headers;
    }
}
