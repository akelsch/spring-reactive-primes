package de.htwsaar.vs.primes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class PrimesRouter {

    @Bean
    public RouterFunction<ServerResponse> route(PrimesHandler primesHandler) {
        return RouterFunctions
                .route(GET("/primes")
                        .and(accept(TEXT_PLAIN).or(accept(APPLICATION_JSON))), primesHandler::primes)
                .andRoute(GET("/stream")
                        .and(accept(TEXT_EVENT_STREAM)), primesHandler::primesStream);
    }
}
