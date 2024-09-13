package org.gds.poc.orch.domain.jpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class JPLExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleResourceNotFoundException(IllegalArgumentException ex, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                createProblemDetail(ex,HttpStatus.BAD_REQUEST, ex.getMessage(), null,null,exchange)
        ));
    }

    @ExceptionHandler(IllegalStateException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleBadRequestException(IllegalStateException ex, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                createProblemDetail(ex,HttpStatus.BAD_REQUEST, ex.getMessage(), null,null,exchange)
                ));
    }


}