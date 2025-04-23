package com.example.wallet;

import com.example.wallet.controller.WalletController;
import com.example.wallet.model.dto.OperationContext;
import com.example.wallet.service.BlockingWalletService;
import com.example.wallet.service.ReactiveWalletService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@WebFluxTest(WalletController.class)
class WalletApplicationTests {

	@Autowired
	private WebTestClient webClient;

	@MockitoBean
	ReactiveWalletService reactiveWalletService;

	@Test
	public void getAmountByUUID() throws Exception {
		var amount = new BigDecimal("123.23");
		Mockito.when(reactiveWalletService.reactiveFindWalletAmount("1")).thenReturn(Mono.just(amount));

		var actual = webClient.get()
				.uri("/api/v1/wallets?wallet_id=1")
				.exchange()
				.expectStatus().isOk()
				.expectBody(OperationContext.class)
				.returnResult().getResponseBody().getValue().toString();

		Assertions.assertEquals(actual, amount.toString());
	}
}
