package de.htwsaar.vs.primes;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.http.MediaType.*;

@Component
public class PrimesHandler {

    public Mono<ServerResponse> primes(ServerRequest request) {
        final var n = PrimesUtil.requireIntQueryParam(request, "n");
        final var format = request.queryParam("format").orElse("");

        var primes = PrimesUtil.generatePrimesFlux()
                .take(n);

        switch (format) {
            case "string":
            default:
                return ServerResponse.ok()
                        .contentType(TEXT_PLAIN)
                        .body(PrimesUtil.convertFluxToString(primes), String.class);
            case "array":
                return ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(primes, Integer.class);
            case "combined":
                var primesString = PrimesUtil.convertFluxToString(primes);
                var primesArray = primes.collectList();

                var combinedPrimes = primesString
                        .zipWith(primesArray, CombinedPrimes::new);

                return ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(combinedPrimes, CombinedPrimes.class);
        }
    }

    public Mono<ServerResponse> primesStream(ServerRequest request) {
        final var n = PrimesUtil.requireIntQueryParam(request, "n");
        final var delay = PrimesUtil.getIntQueryParam(request, "delay", 250);

        var primes = PrimesUtil.generatePrimesFlux()
                .delayElements(Duration.ofMillis(delay))
                .take(n);

        return ServerResponse.ok()
                .contentType(TEXT_EVENT_STREAM)
                .body(primes, Integer.class);
    }
}
