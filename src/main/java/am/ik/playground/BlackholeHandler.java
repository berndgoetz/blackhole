package am.ik.playground;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.accepted;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

public class BlackholeHandler {

    private static final Logger log = LoggerFactory.getLogger(BlackholeHandler.class);

    public RouterFunction<ServerResponse> routes() {
        return route(__ -> true, this::accept);
    }

    Mono<ServerResponse> accept(ServerRequest req) {
        ServerWebExchange exchange = req.exchange();
        ServerHttpRequest request = exchange.getRequest();
        return req.bodyToMono(String.class) //
            .switchIfEmpty(Mono.just("")) //
            .doOnNext(s -> {
                log.info("{}\t{}\t{}", request.getMethod(), request.getPath(), s);
            }) //
            .flatMap(__ -> ((request.getMethod() == HttpMethod.GET || request.getMethod() == HttpMethod.HEAD) ? ok() : accepted()).build());
    }
}
