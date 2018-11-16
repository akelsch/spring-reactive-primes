package de.htwsaar.vs.primes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class PrimesRouter {

    @Bean
    public RouterFunction<ServerResponse> route(PrimesHandler primesHandler) {
        return RouterFunctions
                .route(GET("/primes"), primesHandler::primes);
    }
}
