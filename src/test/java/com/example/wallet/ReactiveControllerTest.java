/*
package com.example.wallet;

import com.example.wallet.controller.WalletController;
import com.example.wallet.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebFluxTest(WalletController.class)
public class ReactiveControllerTest {

   @Autowired
   MockMvc mockMvc;

    @MockitoBean
    WalletService walletService;

    @Test
    public void updateAmount() throws Exception
    {
        mockMvc.perform( MockMvcRequestBuilders
                        .post("/wallet")
                        .content(asJsonString(new EmployeeVO(null, "firstName4", "lastName4", "email4@mail.com")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.employeeId").exists());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
*/
