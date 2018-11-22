package de.htwsaar.vs.primes;

import org.apache.commons.math3.primes.Primes;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class PrimesUtil {

    private PrimesUtil() {
    }

    public static List<Integer> findFirstPrimes(int n) {
        Assert.isTrue(n > 0, "Prime number count must be a positive natural number!");

        var primes = new ArrayList<Integer>();
        primes.add(2);

        for (int i = 0; i < n - 1; i++) {
            primes.add(Primes.nextPrime(primes.get(i) + 1));
        }

        return primes;
    }

    public static <T> String convertListToString(List<T> list) {
        return list.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" "));
    }

    public static int requireIntQueryParam(ServerRequest request, String name) {
        final var param = request.queryParam(name).orElse("");

        Assert.isTrue(!param.isEmpty(), String.format("Parameter '%s' is empty", name));
        Assert.isTrue(param.matches("\\d+"), String.format("Parameter '%s' is not numeric", name));

        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Parameter '%s' outside of range of int", name));
        }
    }

    public static int getIntQueryParam(ServerRequest request, String name, int fallback) {
        try {
            return requireIntQueryParam(request, name);
        } catch (IllegalArgumentException e) {
            return fallback;
        }
    }
}
