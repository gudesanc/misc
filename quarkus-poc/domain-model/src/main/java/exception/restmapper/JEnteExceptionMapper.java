package exception.restmapper;

import io.opentelemetry.api.trace.Span;
import exception.JEnteRuntimeException;
import exception.data.JEnteErrorDetail;
import exception.data.JEnteErrorType;
import exception.data.JEnteProblemDetail;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


@Provider
public class JEnteExceptionMapper implements ExceptionMapper<JEnteRuntimeException> {
    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(JEnteRuntimeException ex) {
        URI type = JEnteErrorType.JEnteCommonErrorType.GENERIC_ERROR.getType();
        if(ex.getJEnteErrorType() != null) {
            type = ex.getJEnteErrorType().getType();
        }
        URI instance = null;
        try {
            instance = new URI(uriInfo.getRequestUri().getPath());
        } catch (URISyntaxException e) {
            //Ignore :)
        }
        String title = ex.getTitle();
        int status = ex.getStatus();
        JEnteProblemDetail bodyRespose = createProblemDetail(type,instance,status,title,ex.getErrorMessage(),ex.getErrorCode(),ex.getDetails());
        return Response
                .status(status)
                .entity(bodyRespose).build();
    }

    private JEnteProblemDetail createProblemDetail(URI type ,
                                                   URI instance,
                                                   int status, String title,
                                                   String errorMessage,
                                                   String errorCode,
                                                   List<JEnteErrorDetail> details) {

        String traceId = Span.current().getSpanContext().getTraceId();
        String correlationId = Span.current().getSpanContext().getSpanId();
        return new JEnteProblemDetail(type, title, status, errorMessage, instance, traceId, correlationId,
                errorCode,details);
    }


}
