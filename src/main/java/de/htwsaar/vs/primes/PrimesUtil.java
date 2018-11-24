package de.htwsaar.vs.primes;

import org.apache.commons.math3.primes.Primes;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

public final class PrimesUtil {

    private PrimesUtil() {
    }

    public static Flux<Integer> generatePrimesFlux() {
        return Flux.generate(
                () -> 2,
                (state, sink) -> {
                    var prime = Primes.nextPrime(state);
                    sink.next(prime);

                    return prime + 1;
                });
    }

    public static <T> Mono<String> convertFluxToString(Flux<T> flux) {
        return flux
                .map(T::toString)
                .collect(Collectors.joining(" "));
    }

    public static int requireIntQueryParam(ServerRequest request, String name) {
        final var param = request.queryParam(name).orElse("");

        Assert.isTrue(!param.isEmpty(), String.format("Parameter '%s' is empty", name));
        Assert.isTrue(param.matches("\\d+"), String.format("Parameter '%s' is not numeric", name));

        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, String.format("Parameter '%s' outside of range of int", name));
        }
    }

    public static int getIntQueryParam(ServerRequest request, String name, int fallback) {
        try {
            return requireIntQueryParam(request, name);
        } catch (ResponseStatusException e) {
            return fallback;
        }
    }

    private static final class Assert {
        static void isTrue(boolean expression, String message) {
            if (!expression) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
            }
        }
    }
}
