package de.htwsaar.vs.primes;

import org.apache.commons.math3.primes.Primes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.*;

@Component
public class PrimesHandler {

    public Mono<ServerResponse> primes(ServerRequest request) {
        final int n = PrimesUtil.parseQueryParam(request.queryParam("n").orElse(""));
        final String format = request.queryParam("format").orElse("");

        Flux<Integer> primes = generatePrimesFlux().take(n);

        switch (format) {
            case "string":
            default:
                return handlePrimesStringCase(primes);
            case "array":
                return handlePrimesArrayCase(primes);
            case "combined":
                return handlePrimesCombinedCase(primes);
        }
    }

    public Mono<ServerResponse> primesStream(ServerRequest request) {
        final int n = PrimesUtil.parseQueryParam(request.queryParam("n").orElse(""));
        final int delay = PrimesUtil.parseQueryParam(request.queryParam("delay").orElse(""), 250);

        Flux<Integer> primes = generatePrimesFlux()
                .delayElements(Duration.ofMillis(delay))
                .take(n);

        return ServerResponse.ok()
                .contentType(TEXT_EVENT_STREAM)
                .body(primes, Integer.class);
    }

    private static Flux<Integer> generatePrimesFlux() {
        return Flux.generate(
                () -> 2,
                (state, sink) -> {
                    int prime = Primes.nextPrime(state);
                    sink.next(prime);

                    return prime + 1;
                });
    }

    private static <T> Mono<String> convertFluxToString(Flux<T> flux) {
        return flux
                .map(T::toString)
                .collect(Collectors.joining(" "));
    }

    private Mono<ServerResponse> handlePrimesStringCase(Flux<Integer> primes) {
        return ServerResponse.ok()
                .contentType(TEXT_PLAIN)
                .body(convertFluxToString(primes), String.class);
    }

    private Mono<ServerResponse> handlePrimesArrayCase(Flux<Integer> primes) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(primes, Integer.class);
    }

    private Mono<ServerResponse> handlePrimesCombinedCase(Flux<Integer> primes) {
        Mono<String> primesString = convertFluxToString(primes);
        Mono<List<Integer>> primesArray = primes.collectList();

        Mono<CombinedPrimes> combinedPrimes = primesString
                .zipWith(primesArray, CombinedPrimes::new);

        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(combinedPrimes, CombinedPrimes.class);
    }
}
