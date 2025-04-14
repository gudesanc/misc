package exception;

import exception.data.JEnteCommonErrors;

public class JEnteEntityNotFoundException extends JEnteRuntimeException{
    public JEnteEntityNotFoundException(String detailMessage) {
        this(detailMessage,null,null);
    }
    public JEnteEntityNotFoundException(String detailMessage, String entityType, String queryString) {
        super(JEnteCommonErrors.JENTE_ENTITY_NOT_FOUND.getStatus().getStatusCode(),
                detailMessage,
                JEnteCommonErrors.JENTE_ENTITY_NOT_FOUND.getTitle(),
                JEnteCommonErrors.JENTE_ENTITY_NOT_FOUND.getErrorCode(),null,
                JEnteCommonErrors.JENTE_ENTITY_NOT_FOUND.getErrorType());
        addDetail(entityType,queryString);
    }
}
