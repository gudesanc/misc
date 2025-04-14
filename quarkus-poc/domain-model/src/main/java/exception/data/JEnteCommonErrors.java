package exception.data;


import exception.data.JEnteProblemDetail;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

import static exception.data.JEnteErrorType.JEnteCommonErrorType.*;

public enum JEnteCommonErrors {
    JENTE_REQUEST_CTX_NOT_FOUND(Response.Status.INTERNAL_SERVER_ERROR,"Request Context Not found",REQUEST_CTX_NOT_FOUND,"JEC-A001"),
    JENTE_PERMISSION_DENIED(Response.Status.FORBIDDEN,"Permission Denied",PERMISSION_DENIED,"JEC-A101"),
    JENTE_ENTITY_NOT_FOUND(Response.Status.NOT_FOUND,"Object Not found",ENTITY_NOT_FOUND_ERROR,"JEC-B001"),
    JENTE_UNSUPPORTED_OPERATION_IN_CTX(Response.Status.INTERNAL_SERVER_ERROR,"Configuration Error",INVALID_OPERATION_IN_CTX,"JEC-V001"),
    JENTE_CONFIGURATION_ERROR(Response.Status.INTERNAL_SERVER_ERROR,"Configuration Error",CONFIGURATION_ERROR,"JEC-Z001"),
    JENTE_VALIDATION_ERROR(Response.Status.BAD_REQUEST,"Validation Error",VALIDATION_ERROR,"JEC-X001"),
    JENTE_REMOTE_SERVICE_UNAVAILABLE(Response.Status.BAD_GATEWAY,"Remote Service Unavailable",REMOTE_SYSTEM_UNAVAILABLE,"JEC-Y001");
    private final Response.Status status;
    private final String title;
    private final JEnteErrorType.JEnteCommonErrorType errorType;
    private final String errorCode;

    JEnteCommonErrors(Response.Status status, String title, JEnteErrorType.JEnteCommonErrorType errorType, String errorCode) {
        this.status = status;
        this.title = title;
        this.errorType = errorType;
        this.errorCode = errorCode;
    }

    public JEnteErrorType.JEnteCommonErrorType getErrorType() {
        return errorType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Response.Status getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    /**
     * REstituisce il JEnteCommonErrors che ha la stessa uri,lo stesso status code e lo stesso error code
     * del problem detail in input
     * @param problemDetail il problem detail generato da quest jentecommonerror
     * @return il jentecommonerror se trovato
     */
    public static Optional<JEnteCommonErrors> getRelatedJEnteCommonErrors(JEnteProblemDetail problemDetail){
        if(problemDetail==null || problemDetail.getType()==null||problemDetail.getErrorCode()==null){
            return Optional.empty();
        }
        for(JEnteCommonErrors error: JEnteCommonErrors.values()){
            if(error.getErrorType().getType().equals(problemDetail.getType())
                    && error.getStatus().getStatusCode()==problemDetail.getStatus()
                    && error.getErrorCode().equals(problemDetail.getErrorCode())){
                return Optional.of(error);
            }
        }
        return Optional.empty();
    }
}
