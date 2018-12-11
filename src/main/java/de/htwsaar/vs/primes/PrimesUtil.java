package de.htwsaar.vs.primes;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class PrimesUtil {

    private PrimesUtil() {
    }

    public static int parseQueryParam(String queryParam) {
        try {
            if (queryParam.startsWith("-"))
                throw new NumberFormatException(String.format("Illegal leading minus sign on string '%s'", queryParam));

            return Integer.parseInt(queryParam);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, String.format("Could not parse '%s' as a non-negative integer", queryParam));
        }
    }

    public static int parseQueryParam(String queryParam, int fallback) {
        try {
            return parseQueryParam(queryParam);
        } catch (ResponseStatusException e) {
            return fallback;
        }
    }
}
