package com.codecool.solarwatch.integration;

import com.codecool.solarwatch.entity.User;
import com.codecool.solarwatch.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class UserProfileIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setupMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @BeforeEach
    void cleanDb() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "profile_user", roles = "USER")
    void profile_withAuthenticatedUser_returnsProfile() throws Exception {
        User u = new User();
        u.setUsername("profile_user");
        u.setEmail("profile_user@example.com");
        u.setPassword("irrelevant");
        u.setRoles(Set.of("USER"));
        u.setEnabled(true);
        userRepository.save(u);

        mockMvc.perform(get("/api/user/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("profile_user"))
                .andExpect(jsonPath("$.email").value("profile_user@example.com"))
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));
    }
}

