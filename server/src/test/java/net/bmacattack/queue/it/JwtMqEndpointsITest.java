package net.bmacattack.queue.it;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class JwtMqEndpointsITest extends MqEndpointsITest {
    @Override
    HttpHeaders getAuthentication() throws Exception {
        HttpHeaders jwt = new HttpHeaders();
        jwt.add("authorization", "Bearer " + getJwt());
        return jwt;
    }

    @Test
    public void testFetchJwt() throws Exception {
        String jwt = getJwt();
        Assert.assertNotNull(jwt);

    }

    @Test
    public void testFetchJwtBadCredentials() throws Exception {
        JSONObject login = new JSONObject();
        login.put("username", "dev");
        login.put("password", "badPassword");
        String jwt = mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(login.toString().getBytes()))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.UNAUTHORIZED.value())).andReturn().getResponse().getHeader("authorization");
        Assert.assertNull(jwt);

    }

    private String getJwt() throws Exception {
        JSONObject login = new JSONObject();
        login.put("username", "dev");
        login.put("password", "dev");
        return mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(login.toString().getBytes()))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn().getResponse().getHeader("authorization");
    }
}
