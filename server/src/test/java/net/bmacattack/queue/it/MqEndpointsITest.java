package net.bmacattack.queue.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bmacattack.queue.persistence.RoleEnum;
import net.bmacattack.queue.persistence.dao.UserRepository;
import net.bmacattack.queue.persistence.model.User;
import net.bmacattack.queue.web.dto.QueueDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

public abstract class MqEndpointsITest extends BaseItTest{

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @MockBean
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    // use springSecurityFilterChain
    @Autowired
    private Filter springSecurityFilterChain;

    User user;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).addFilters(springSecurityFilterChain).build();
        user = new User("dev", "test", passwordEncoder.encode("dev"), RoleEnum.ADMIN);
        Mockito.when(userRepository.getUserByUsername(ArgumentMatchers.anyString()))
                .thenReturn(user);
    }

    abstract HttpHeaders getAuthentication() throws Exception;

    @Test
    public void testQueueEndToEnd() throws Exception {
        HttpHeaders authentication = getAuthentication();
        QueueDto queueDto = new QueueDto();
        queueDto.setDestination("test");
        queueDto.setMessage("test");
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/queue")
                .content(objectMapper.writeValueAsBytes(queueDto)).headers(authentication)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
        byte[] responce = mockMvc.perform(MockMvcRequestBuilders.get("/api/queue/" + queueDto.getDestination())
                    .headers(authentication))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn().getResponse().getContentAsByteArray();
        QueueDto[] responces = objectMapper.readValue(responce, QueueDto[].class);
        Assert.assertEquals(1, responces.length);
        Assert.assertEquals(queueDto, responces[0]);
    }

}
