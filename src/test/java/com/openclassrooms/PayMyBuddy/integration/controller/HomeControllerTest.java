package com.openclassrooms.PayMyBuddy.integration.controller;

import com.openclassrooms.PayMyBuddy.controller.UserController;
import com.openclassrooms.PayMyBuddy.exception.IncorrectCredentialsException;
import com.openclassrooms.PayMyBuddy.model.LoginDto;
import com.openclassrooms.PayMyBuddy.service.UserService;
import com.openclassrooms.PayMyBuddy.utils.CurrentUserUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest {

    @MockBean
    UserService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void homeTest() throws Exception {
        when(service.getBalance()).thenReturn(10d);
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(content().string(containsString("Bienvenue sur PayMyBuddy !")));
        verify(service, Mockito.times(1)).getBalance();
    }

    @Test
    public void homeUnauthenticatedTest() throws Exception {
        try (MockedStatic<CurrentUserUtils> utilities = Mockito.mockStatic(CurrentUserUtils.class)) {
            utilities.when(CurrentUserUtils::getCurrentUserId).thenReturn(null);
            mockMvc.perform(get("/"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("unauthenticatedHome"))
                    .andExpect(content().string(containsString("Bienvenue sur PayMyBuddy !")));
        }

        verify(service, Mockito.never()).getBalance();
    }

    @Test
    public void sitemapTest() throws Exception {
        mockMvc.perform(get("/sitemap"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("sitemap"))
                .andExpect(content().string(containsString("Plan du site")));
    }

    @Test
    public void loginTest() throws Exception {
        mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(content().string(containsString("Pay my buddy")));
    }

    @Test
    public void loginPostTest() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("email=abc@de.com&password=password&isRememberMe=false")
                )
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
        verify(service, Mockito.times(1)).logUserIn(any(LoginDto.class));
    }

    @Test
    public void loginPostFormErrorTest() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("email=&password=&isRememberMe=false")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
        verify(service, Mockito.never()).logUserIn(any(LoginDto.class));
    }

    @Test
    public void incorrectLoginPostTest() throws Exception {
        doThrow(new IncorrectCredentialsException()).when(service).logUserIn(any(LoginDto.class));
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("email=abc@de.com&password=password&isRememberMe=false")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
        verify(service, Mockito.times(1)).logUserIn(any(LoginDto.class));
    }
}
