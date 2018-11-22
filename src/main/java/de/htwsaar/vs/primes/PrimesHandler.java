package de.htwsaar.vs.primes;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
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
        var n = request.queryParam("n").orElse("empty");
        Assert.isTrue(n.matches("\\d+"), "Parameter 'n' must be a sequence of numbers!");

        var format = request.queryParam("format").orElse("string");

        var primes = PrimesUtil.findFirstPrimes(Integer.parseInt(n));

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
                        .body(BodyInserters.fromObject(new Primes(primes)));
        }
    }

    public Mono<ServerResponse> primesStream(ServerRequest request) {
        var n = request.queryParam("n").orElse("empty");
        Assert.isTrue(n.matches("\\d+"), "Parameter 'n' must be a sequence of numbers!");

        var primes = Flux.<Integer, Integer>generate(
                () -> 2,
                (state, sink) -> {
                    var prime = org.apache.commons.math3.primes.Primes.nextPrime(state);
                    sink.next(prime);

                    return prime + 1;
                });

        return ServerResponse.ok()
                .contentType(TEXT_EVENT_STREAM)
                .body(primes.delayElements(Duration.ofMillis(250)).take(Integer.parseInt(n)), Integer.class);
    }
}
