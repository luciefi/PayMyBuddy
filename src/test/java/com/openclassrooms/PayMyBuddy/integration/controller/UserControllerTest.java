package com.openclassrooms.PayMyBuddy.integration.controller;

import com.openclassrooms.PayMyBuddy.exception.EmailAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.exception.IncorrectCurrentPasswordException;
import com.openclassrooms.PayMyBuddy.exception.PasswordAndConfirmationNotIdenticalException;
import com.openclassrooms.PayMyBuddy.exception.UserNotFoundException;
import com.openclassrooms.PayMyBuddy.model.PasswordUpdateDto;
import com.openclassrooms.PayMyBuddy.model.ProfileDto;
import com.openclassrooms.PayMyBuddy.model.UserDto;
import com.openclassrooms.PayMyBuddy.service.UserService;
import org.junit.jupiter.api.Test;
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
public class UserControllerTest {

    @MockBean
    UserService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createProfileTest() throws Exception {
        mockMvc.perform(get("/createProfile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("createProfile"))
                .andExpect(content().string(containsString("Créer mon profil")));
    }

    @Test
    public void createProfilePostTest() throws Exception {
        mockMvc.perform(post("/createProfile")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("userDto.email=abc@de.com&passwordDto.password=password&passwordDto.passwordConfirmation=password&userDto" +
                                ".firstName=john&userDto.lastName=doe&userDto" +
                                ".address=myAddress&userDto.phone=0612345678")
                )
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
        verify(service, Mockito.times(1)).saveNewUser(any(ProfileDto.class));
    }

    @Test
    public void createProfilePostFormErrorTest() throws Exception {
        mockMvc.perform(post("/createProfile")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("email=&password=password&firstName=john&lastName=doe&address=myAddress&phone=0612345678")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("createProfile"));
        verify(service, Mockito.times(0)).saveNewUser(any(ProfileDto.class));

    }

    @Test
    public void createProfilePostAlreadyExistsTest() throws Exception {
        when(service.saveNewUser(any(ProfileDto.class))).thenThrow(new EmailAlreadyExistsException());
        mockMvc.perform(post("/createProfile")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("userDto.email=abc@de.com&passwordDto.password=password&passwordDto.passwordConfirmation=password&userDto" +
                                ".firstName=john&userDto.lastName=doe&userDto" +
                                ".address=myAddress&userDto.phone=0612345678")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("createProfile"));
        verify(service, Mockito.times(1)).saveNewUser(any(ProfileDto.class));
    }

    @Test
    public void createProfilePostPasswordAndConfirmationNotIdenticalExceptionTest() throws Exception {
        when(service.saveNewUser(any(ProfileDto.class))).thenThrow(new PasswordAndConfirmationNotIdenticalException());
        mockMvc.perform(post("/createProfile")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("userDto.email=abc@de.com&passwordDto.password=password&passwordDto.passwordConfirmation=password&userDto" +
                                ".firstName=john&userDto.lastName=doe&userDto" +
                                ".address=myAddress&userDto.phone=0612345678")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("createProfile"));
        verify(service, Mockito.times(1)).saveNewUser(any(ProfileDto.class));
    }

    @Test
    public void profileTest() throws Exception {
        when(service.getCurrentUserDto()).thenReturn(new UserDto());
        mockMvc.perform(get("/profile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(content().string(containsString("Mon profil")));
        verify(service, Mockito.times(1)).getCurrentUserDto();
    }

    @Test
    public void profilePostTest() throws Exception {
        mockMvc.perform(post("/profile")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("email=abc@de.com&password=password&firstName=john&lastName=doe&address=myAddress&phone=0612345678")
                )
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/profile"));
        verify(service, Mockito.times(1)).updateUser(any(UserDto.class));
    }

    @Test
    public void profilePostFormErrorTest() throws Exception {
        mockMvc.perform(post("/profile")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("email=&password=password&firstName=john&lastName=doe&address=myAddress&phone=0612345678")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
        verify(service, Mockito.times(0)).updateUser(any(UserDto.class));
    }

    @Test
    public void profilePostAlreadyExistsTest() throws Exception {
        when(service.updateUser(any(UserDto.class))).thenThrow(new EmailAlreadyExistsException());
        mockMvc.perform(post("/profile")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("email=abc@de.com&password=password&firstName=john&lastName=doe&address=myAddress&phone=0612345678")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
        verify(service, Mockito.times(1)).updateUser(any(UserDto.class));
    }

    @Test
    public void profilePostUserNotFoundTest() throws Exception {
        when(service.updateUser(any(UserDto.class))).thenThrow(new UserNotFoundException());
        mockMvc.perform(post("/profile")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("email=abc@de.com&password=password&firstName=john&lastName=doe&address=myAddress&phone=0612345678")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
        verify(service, Mockito.times(1)).updateUser(any(UserDto.class));
    }

    @Test
    public void passwordPostTest() throws Exception {
        mockMvc.perform(post("/password")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("oldPassword=oldPassword&password=password&passwordConfirmation=password")
                )
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/profile"));
        verify(service, Mockito.times(1)).updatePassword(any(PasswordUpdateDto.class));
        verify(service, Mockito.never()).getCurrentUserDto();
    }

    @Test
    public void passwordPostFormErrorTest() throws Exception {
        when(service.getCurrentUserDto()).thenReturn(new UserDto());
        mockMvc.perform(post("/password")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("oldPassword=oldPassword&password=password&passwordConfirmation=")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
        verify(service, Mockito.never()).updatePassword(any(PasswordUpdateDto.class));
        verify(service, Mockito.times(1)).getCurrentUserDto();
    }

    @Test
    public void passwordPostUserNotFoundTest() throws Exception {
        doThrow(new UserNotFoundException()).when(service).updatePassword(any(PasswordUpdateDto.class));
        when(service.getCurrentUserDto()).thenReturn(new UserDto());

        mockMvc.perform(post("/password")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("oldPassword=oldPassword&password=password&passwordConfirmation=password")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
        verify(service, Mockito.times(1)).updatePassword(any(PasswordUpdateDto.class));
        verify(service, Mockito.times(1)).getCurrentUserDto();
    }

    @Test
    public void passwordPostPasswordAndConfirmationNotIdenticalTest() throws Exception {
        doThrow(new PasswordAndConfirmationNotIdenticalException()).when(service).updatePassword(any(PasswordUpdateDto.class));
        when(service.getCurrentUserDto()).thenReturn(new UserDto());
        mockMvc.perform(post("/password")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("oldPassword=oldPassword&password=password&passwordConfirmation=password")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
        verify(service, Mockito.times(1)).updatePassword(any(PasswordUpdateDto.class));
        verify(service, Mockito.times(1)).getCurrentUserDto();
    }

    @Test
    public void passwordPostIncorrectCurrentPasswordTest() throws Exception {
        doThrow(new IncorrectCurrentPasswordException()).when(service).updatePassword(any(PasswordUpdateDto.class));
        when(service.getCurrentUserDto()).thenReturn(new UserDto());

        mockMvc.perform(post("/password")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("oldPassword=oldPassword&password=password&passwordConfirmation=password")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
        verify(service, Mockito.times(1)).updatePassword(any(PasswordUpdateDto.class));
        verify(service, Mockito.times(1)).getCurrentUserDto();
    }
}
