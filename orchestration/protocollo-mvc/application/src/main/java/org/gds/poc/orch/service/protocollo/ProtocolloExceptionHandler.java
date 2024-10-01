package org.gds.poc.orch.service.protocollo;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ProtocolloExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(IllegalArgumentException ex, WebRequest exchange) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                createProblemDetail(ex,HttpStatus.BAD_REQUEST, createDetail(ex), null,null,exchange));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ProblemDetail> handleBadRequestException(IllegalStateException ex, WebRequest exchange) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                createProblemDetail(ex,HttpStatus.BAD_REQUEST,  createDetail(ex), null,null,exchange)
                );
    }

    @ExceptionHandler(InternalError.class)
    public ResponseEntity<ProblemDetail> handleBadRequestException(UnsupportedOperationException ex, WebRequest exchange) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                createProblemDetail(ex,HttpStatus.INTERNAL_SERVER_ERROR,  createDetail(ex), null,null,exchange)
        );
    }

    private String createDetail(Throwable t){
        return "Error: "+t.getMessage()+" - TraceId: "+ MDC.get("traceId");
    }

}