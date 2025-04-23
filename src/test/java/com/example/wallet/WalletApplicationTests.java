package com.example.wallet;

import com.example.wallet.controller.WalletController;
import com.example.wallet.service.BlockingWalletService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
class WalletApplicationTests extends FunctionalTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	BlockingWalletService blockingWalletService;

	@Test
	public void getAmountByUUID() throws Exception {
		var amount = new BigDecimal("123.23");
		Mockito.when(blockingWalletService.findWalletAmount("1")).thenReturn(amount);

		mockMvc.perform(get("/api/v1/wallets?wallet_id=1"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.value").value(amount));
	}

}
