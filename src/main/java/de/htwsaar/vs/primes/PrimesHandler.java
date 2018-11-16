package de.htwsaar.vs.primes;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;

@Component
public class PrimesHandler {

    public Mono<ServerResponse> primes(ServerRequest request) {
        var n = request.queryParam("n").orElse("empty");
        Assert.isTrue(n.matches("\\d+"), "Parameter 'n' must be a sequence of numbers!");

        var format = request.queryParam("format").orElse("text");

        var primes = PrimesUtil.findFirstPrimes(Integer.parseInt(n));

        if (format.equals("json")) {
            return ServerResponse.ok()
                    .contentType(APPLICATION_JSON)
                    .body(BodyInserters.fromObject(primes));
        }

        return ServerResponse.ok()
                .contentType(TEXT_PLAIN)
                .body(BodyInserters.fromObject(PrimesUtil.convertListToString(primes)));
    }
}
