package org.gds.poc.orch.service.protocollo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class ProtocolloExceptionHandler extends ResponseEntityExceptionHandler {

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

    @ExceptionHandler(InternalError.class)
    public Mono<ResponseEntity<ProblemDetail>> handleBadRequestException(UnsupportedOperationException ex, ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                createProblemDetail(ex,HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null,null,exchange)
        ));
    }

}