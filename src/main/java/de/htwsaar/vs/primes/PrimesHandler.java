package de.htwsaar.vs.primes;

import org.apache.commons.math3.primes.Primes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.http.MediaType.*;

@Component
public class PrimesHandler {

    public Mono<ServerResponse> primes(ServerRequest request) {
        final var n = PrimesUtil.requireIntQueryParam(request, "n");
        final var format = request.queryParam("format").orElse("");

        var primes = PrimesUtil.findFirstPrimes(n);

        switch (format) {
            case "string":
            default:
                return ServerResponse.ok()
                        .contentType(TEXT_PLAIN)
                        .body(BodyInserters.fromObject(PrimesUtil.convertListToString(primes)));
            case "array":
                return ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(BodyInserters.fromObject(primes));
            case "combined":
                return ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(BodyInserters.fromObject(new CombinedPrimes(primes)));
        }
    }

    public Mono<ServerResponse> primesStream(ServerRequest request) {
        final var n = PrimesUtil.requireIntQueryParam(request, "n");
        final var delay = PrimesUtil.getIntQueryParam(request, "delay", 250);

        var primes = Flux.<Integer, Integer>generate(
                () -> 2,
                (state, sink) -> {
                    var prime = Primes.nextPrime(state);
                    sink.next(prime);

                    return prime + 1;
                });

        return ServerResponse.ok()
                .contentType(TEXT_EVENT_STREAM)
                .body(primes.delayElements(Duration.ofMillis(delay)).take(n), Integer.class);
    }
}
