package exception;

import exception.data.JEnteCommonErrors;

public class JEnteRequestCtxNotFoundException extends JEnteRuntimeException{
    public JEnteRequestCtxNotFoundException(String detailMessage) {
        super(JEnteCommonErrors.JENTE_REQUEST_CTX_NOT_FOUND.getStatus().getStatusCode(),
                detailMessage,
                JEnteCommonErrors.JENTE_REQUEST_CTX_NOT_FOUND.getTitle(),
                JEnteCommonErrors.JENTE_REQUEST_CTX_NOT_FOUND.getErrorCode(),null,
                JEnteCommonErrors.JENTE_REQUEST_CTX_NOT_FOUND.getErrorType());
    }
}
