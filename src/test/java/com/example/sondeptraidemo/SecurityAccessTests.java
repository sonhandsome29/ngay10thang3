package com.example.sondeptraidemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityAccessTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void userCanLoginAndOpenProducts() throws Exception {
        MockHttpSession session = login("user", "123456");

        mockMvc.perform(get("/products").session(session))
                .andExpect(status().isOk());
    }

    @Test
    void adminCanOpenAddProductPage() throws Exception {
        MockHttpSession session = login("admin", "admin123");

        mockMvc.perform(get("/products/add").session(session))
                .andExpect(status().isOk());
    }

    @Test
    void userCannotOpenAddProductPage() throws Exception {
        MockHttpSession session = login("user", "123456");

        mockMvc.perform(get("/products/add").session(session))
                .andExpect(status().isForbidden());
    }

    @Test
    void userCanOpenOrderPage() throws Exception {
        MockHttpSession session = login("user", "123456");

        mockMvc.perform(get("/order").session(session))
                .andExpect(status().isOk());
    }

    @Test
    void adminCannotOpenOrderPage() throws Exception {
        MockHttpSession session = login("admin", "admin123");

        mockMvc.perform(get("/order").session(session))
                .andExpect(status().isForbidden());
    }

    private MockHttpSession login(String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(formLogin().user(username).password(password))
                .andExpect(authenticated().withUsername(username))
                .andReturn();

        return (MockHttpSession) result.getRequest().getSession(false);
    }
}
