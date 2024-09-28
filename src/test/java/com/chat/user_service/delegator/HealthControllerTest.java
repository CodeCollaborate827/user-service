package com.chat.user_service.delegator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = HealthController.class)
@TestPropertySource(properties = "spring.application.name=test-application")
class HealthControllerTest {

    @Autowired
    private WebTestClient webTestClient;


    private final String applicationName = "test-application";  // Use the same value as defined in TestPropertySource

    @Test
    void testGetHealth() {
        webTestClient.get()
                .uri("/api/user/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(applicationName)
                .jsonPath("$.timestamp").exists();
    }

    @Test
    void testPostHealth() {
        webTestClient.post()
                .uri("/api/user/health")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo(applicationName)
                .jsonPath("$.timestamp").exists();
    }
}