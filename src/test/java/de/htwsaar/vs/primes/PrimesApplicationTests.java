package de.htwsaar.vs.primes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.http.MediaType.TEXT_PLAIN;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PrimesApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testPlainText() {
        webTestClient
                .get().uri("/primes?n=10")
                .accept(TEXT_PLAIN)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("2 3 5 7 11 13 17 19 23 29");
    }
}
