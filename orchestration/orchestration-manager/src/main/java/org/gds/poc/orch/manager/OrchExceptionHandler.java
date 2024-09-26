package org.gds.poc.orch.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class OrchExceptionHandler extends ResponseEntityExceptionHandler {
    Logger LOG = LoggerFactory.getLogger(OrchExceptionHandler.class);
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(IllegalArgumentException ex, WebRequest exchange) {
        String detail =createDetail(ex);
        LOG.atWarn().setMessage("IllegalArgumentException due to bad parameters: {}").addArgument(detail).log();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                createProblemDetail(ex,HttpStatus.BAD_REQUEST, detail, null,null,exchange));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ProblemDetail> handleBadRequestException(IllegalStateException ex, WebRequest exchange) {
        String detail =createDetail(ex);
        LOG.atWarn().setMessage("IllegalStateException due to bad parameters: {}").addArgument(detail).log();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                createProblemDetail(ex,HttpStatus.BAD_REQUEST,  detail, null,null,exchange)
                );
    }

    @ExceptionHandler(InternalError.class)
    public ResponseEntity<ProblemDetail> handleBadRequestException(UnsupportedOperationException ex, WebRequest exchange) {
        String detail =createDetail(ex);
        LOG.atWarn().setMessage("Bad Request in input non validi: {}").addArgument(detail).log();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                createProblemDetail(ex,HttpStatus.INTERNAL_SERVER_ERROR,  detail, null,null,exchange)
        );
    }

    //MethodArgumentNotValidException.class gia' gestita dalla super classe.. facciamo ovverride del metodo per il messaggio
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        LOG.atWarn().setMessage("Parametri in input non validi: {}, detail: {}").addArgument(errors).addArgument(createDetail(ex)).log();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                createProblemDetail(ex,HttpStatus.BAD_REQUEST,  "Invalid input data: "+errors,  null,null,request)
        );
    }
    private String createDetail(Throwable t){
        Map<String,String> mdcCtx = MDC.getCopyOfContextMap();
        return "Error: "+t.getMessage()+" - TraceId: "+ MDC.get("traceId") ;
    }

}