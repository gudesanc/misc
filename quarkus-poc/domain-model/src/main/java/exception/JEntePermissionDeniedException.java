package exception;

import exception.data.JEnteCommonErrors;

public class JEntePermissionDeniedException extends JEnteRuntimeException{
    public JEntePermissionDeniedException(String detailMessage) {
        super(JEnteCommonErrors.JENTE_PERMISSION_DENIED.getStatus().getStatusCode(),
                detailMessage,
                JEnteCommonErrors.JENTE_PERMISSION_DENIED.getTitle(),
                JEnteCommonErrors.JENTE_PERMISSION_DENIED.getErrorCode(),null,
                JEnteCommonErrors.JENTE_PERMISSION_DENIED.getErrorType());
    }
}
