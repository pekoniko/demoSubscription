package com.example.demo;

import com.example.demo.dto.SubscriptionDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.Subscription;
import com.example.demo.model.User;
import com.example.demo.repository.SubscriptionRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    private User userGlobal = null;


    @BeforeEach
    public void createTestUser() {
        User user = new User("TestUser");
        userRepository.save(user);
        userGlobal = user;
    }

    @AfterEach
    public void deleteTestEmployee() {
        subscriptionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createUser() throws Exception {
        User user = new User("TestUserNew1");
        UserDTO userToSave = new UserDTO(user.getUserName());
        String requestUrl = "/users";
        mockMvc.perform(MockMvcRequestBuilders.post(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToSave)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void getUser() throws Exception {
        String requestUrl = "/users/" + userGlobal.getId();
        MvcResult result = getRequest(requestUrl);
        var jsonReturn = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);
        Assertions.assertEquals(jsonReturn.name(), userGlobal.getUserName());
    }

    @Test
    void updateUser() throws Exception {
        UserDTO newUserNamed = new UserDTO("newNewName");
        String request = "/users/" + userGlobal.getId();
        putRequest(request, objectMapper.writeValueAsString(newUserNamed));
    }

    @Test
    void deleteUser() throws Exception {
        String requestUrl = "/users/" + userGlobal.getId();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(requestUrl))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void addSubscriptionToUser() throws Exception {
        SubscriptionDTO userToSave = new SubscriptionDTO("NetFlex");
        String requestUrl = "/users/" + userGlobal.getId() + "/subscriptions";
        mockMvc.perform(MockMvcRequestBuilders.post(requestUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToSave)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void getSubscriptions() throws Exception {
        Subscription subToSave = new Subscription(userGlobal, "NetFlex");
        subscriptionRepository.save(subToSave);
        String requestUrl = "/users/" + userGlobal.getId() + "/subscriptions";
        MvcResult result = getRequest(requestUrl);
        var jsonReturn = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<SubscriptionDTO>>(){});
        Assertions.assertFalse(jsonReturn.isEmpty());
        Assertions.assertEquals("NetFlex", jsonReturn.get(0).subscribeName());
    }

    @Test
    void getToThree() throws Exception {
        subscriptionRepository.save( new Subscription(userGlobal, "NetFlex1"));
        subscriptionRepository.save( new Subscription(userGlobal, "NetFlex1"));
        subscriptionRepository.save( new Subscription(userGlobal, "NetFlex2"));
        subscriptionRepository.save( new Subscription(userGlobal, "NetFlex2"));
        subscriptionRepository.save( new Subscription(userGlobal, "NetFlex3"));
        subscriptionRepository.save( new Subscription(userGlobal, "NetFlex3"));
        subscriptionRepository.save( new Subscription(userGlobal, "NetFlex4"));
        String requestUrl = "/users/subscriptions/top";
        MvcResult result = getRequest(requestUrl);
        var jsonReturn = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<String>>(){});
        Assertions.assertEquals(3, jsonReturn.size());
        assertThat(jsonReturn, hasItems("NetFlex1", "NetFlex2", "NetFlex3"));
    }

    private MvcResult getRequest(String request) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(" ")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    private MvcResult putRequest(String request, String body) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

}
