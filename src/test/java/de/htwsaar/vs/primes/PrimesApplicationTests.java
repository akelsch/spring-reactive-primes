package de.htwsaar.vs.primes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.springframework.http.MediaType.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class PrimesApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void test10PrimesWithDefaultFormat() {
        webTestClient
                .get().uri("/primes?n=10")
                .accept(TEXT_PLAIN)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(TEXT_PLAIN)
                .expectBody(String.class).isEqualTo("2 3 5 7 11 13 17 19 23 29");
    }

    @Test
    void test10PrimesWithStringFormat() {
        webTestClient
                .get().uri("/primes?n=10&format=string")
                .accept(TEXT_PLAIN)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(TEXT_PLAIN)
                .expectBody(String.class).isEqualTo("2 3 5 7 11 13 17 19 23 29");
    }

    @Test
    void test10PrimesWithArrayFormat() {
        webTestClient
                .get().uri("/primes?n=10&format=array")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody(String.class).isEqualTo("[2,3,5,7,11,13,17,19,23,29]");
    }

    @Test
    void test10PrimesWithCombinedFormat() {
        webTestClient
                .get().uri("/primes?n=10&format=combined")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.string").isEqualTo("2 3 5 7 11 13 17 19 23 29")
                .jsonPath("$.array").isArray();
    }

    @Test
    void test10PrimesStream() {
        FluxExchangeResult<Integer> result = webTestClient
                .get().uri("/stream?n=10&delay=0")
                .accept(TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(TEXT_EVENT_STREAM)
                .returnResult(Integer.class);

        StepVerifier.create(result.getResponseBody())
                .expectNext(2)
                .expectNext(3)
                .expectNext(5)
                .expectNext(7)
                .expectNext(11)
                .expectNext(13)
                .expectNext(17)
                .expectNext(19)
                .expectNext(23)
                .expectNext(29)
                .verifyComplete();
    }

    @Test
    void testPrimesWithEmptyParam() {
        webTestClient
                .get().uri("/primes")
                .accept(TEXT_PLAIN)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testPrimesWithNonNumericParam() {
        webTestClient
                .get().uri("/primes?n=abc")
                .accept(TEXT_PLAIN)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testPrimesWithOutOfRangeParam() {
        webTestClient
                .get().uri("/primes?n=2147483648")
                .accept(TEXT_PLAIN)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
