package de.htwsaar.vs.primes;

import org.apache.commons.math3.primes.Primes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@SpringBootApplication
public class PrimesApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrimesApplication.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions
                .route(GET("/primes").and(accept(TEXT_PLAIN)), this::primesHandler);
    }

    private Mono<ServerResponse> primesHandler(ServerRequest request) {
        var n = request.queryParam("n").orElse("empty");
        Assert.isTrue(n.matches("\\d+"), "Parameter 'n' must be a sequence of numbers!");

        var primes = getPrimes(Integer.parseInt(n));

        return ServerResponse.ok()
                .contentType(TEXT_PLAIN)
                .body(BodyInserters.fromObject(primes.toString()));
    }

    private static ArrayList<Integer> getPrimes(int count) {
        Assert.isTrue(count > 0, "Prime number count must be a positive natural number!");

        var primes = new ArrayList<Integer>();
        primes.add(2);

        for (int i = 0; i < count - 1; i++) {
            primes.add(Primes.nextPrime(primes.get(i) + 1));
        }

        return primes;
    }
}
