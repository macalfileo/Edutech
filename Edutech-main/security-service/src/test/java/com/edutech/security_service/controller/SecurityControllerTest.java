package com.edutech.security_service.controller;

import com.edutech.security_service.model.HashResponse;
import com.edutech.security_service.model.VerifyResponse;
import com.edutech.security_service.service.SecurityService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SecurityController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SecurityService securityService;

    @Test
    void generarHash_falla_porCampoVacio() throws Exception {
        String requestJson = "{\"plainText\": \"\"}";

        mockMvc.perform(post("/api/v1/security/hash")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isBadRequest());
    }

    @Test
    void verificarHash_falla_porCamposVacios() throws Exception {
        String requestJson = "{\"plainText\": \"\", \"hashed\": \"\"}";

        mockMvc.perform(post("/api/v1/security/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isBadRequest());
    }
}