package de.htwsaar.vs.primes;

import java.util.List;

public class CombinedPrimes {

    private final String string;
    private final List<Integer> array;

    public CombinedPrimes(List<Integer> array) {
        string = PrimesUtil.convertListToString(array);
        this.array = array;
    }

    public String getString() {
        return string;
    }

    public List<Integer> getArray() {
        return array;
    }
}
