package de.htwsaar.vs.primes;

import org.apache.commons.math3.primes.Primes;
import org.springframework.util.Assert;

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
}
